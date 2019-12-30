package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/19 15:58
 *@功能:
 */

import com.hx.dao.ElecSignMapper;
import com.hx.modle.AgainSend;
import com.hx.modle.Elec_Sign;
import com.hx.service.ChangeFileService;
import com.hx.util.GetTimeToFileName;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.PNGEncodeParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hx.change.ChangeFile.*;
import static com.hx.change.ExcelToPdf.excelToPdf;
import static com.hx.change.TxtToPdf.text2pdf;
import static com.hx.common.StaticFinal.SCHTASK;
import static com.hx.common.StaticFinal.TEMPDIR;
import static com.hx.util.TempDir.fileTemp;
import static com.hx.util.TempDir.schTask;
import static com.hx.util.TiffToJPEG.readerTiff;

@Service("changeFileService")
public class ChangeFileServiceImpl implements ChangeFileService {
    private static Logger logger=Logger.getLogger(ChangeFileServiceImpl.class);
    @Autowired
    private ElecSignMapper elecSignMapper;
    //转换文件格式为tiff
    @Override
    public Map<String,Object> changeFileSend(MultipartFile file) {
        Map<String,Object> map=new HashMap<>(  );
        String message="文件转换失败";//默认为0,0为不支持文件格式,1为文件转换成功,-1为转换失败,-2为上传文件为空
        boolean flag=false;
        try{
            //判断文件是否为空
            boolean IsEmpty=file.isEmpty();
            int fileType=0;//用来判断文件是Word还是PDF,默认为word 0,1为pdf
            if(!IsEmpty){
                //先判断文件后缀名
                String fileName=file.getOriginalFilename();
                int begin = fileName.indexOf(".");
                int last = fileName.length();
                String suffix = fileName.substring(begin, last);
                boolean back=false;
                String tifPath=schTask();
                if(suffix.equals( ".doc" )||suffix.equals( ".docx" )){
                    back=mkdirDir(file,fileType,tifPath);
                }else if(suffix.equals( ".pdf" )){
                    fileType=1;
                    back=mkdirDir(file,fileType,tifPath);
                }else if(suffix.equals( ".txt" )){
                    fileType=2;
                    back=mkdirDir(file,fileType,tifPath);
                }else if(suffix.equals( ".xls" )||suffix.equals( ".xlsx" )){
                    fileType=3;
                    back=mkdirDir(file,fileType,tifPath);
                }else{
                    message="暂不支持该文件格式";
                }
                if(back){
                    flag=true;
                    message=tifPath;
                }
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
        map.put( "message",message );
        map.put( "flag",flag );
        return map;
    }


    //进行存放tiff文件目录创建,然后判断文件为word还是PDF转换方式
    public static boolean mkdirDir(MultipartFile file,int fileType,String tifPath){
        boolean resultBack=false;
        ImageOutputStream outputStream=null;
        String pdfPath="";
        InputStream is=null;
        try {
            //先判断目录名是否存在
            File fileDir=new File( SCHTASK );
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            outputStream= new FileImageOutputStream(new File( tifPath ));
            if(fileType==0){
                //进行Word文档转换
                pdfPath=wordToPDF(file);
                if(null != pdfPath){
                    //再进行PDF文档转换为tiff文件
                    File pdfFile=new File(pdfPath);
                    is=new FileInputStream( pdfFile );
                    resultBack=PdfToTiff(is,outputStream);
                }
            }else if(fileType==1){
                is=file.getInputStream();
                //进行PDF文档转换
                resultBack=PdfToTiff(is,outputStream);
            }else if(fileType==3){
                //先进行Excel转换PDF
                pdfPath=excelToPdf(file);
                if(null != pdfPath){
                    //再进行PDF文档转换为tiff文件
                    File pdfFile=new File(pdfPath);
                    is=new FileInputStream( pdfFile );
                    resultBack=PdfToTiff(is,outputStream);
                }
            }else if(fileType==4){
                //先将图片转换成 tif
                String pngOrJpgPath=pngToTif(file);
                if(null != pngOrJpgPath){
                    List<String> list=new ArrayList<>(  );
                    list.add( pngOrJpgPath );
                    resultBack=tiffMerge(list,outputStream);
                }
            }else{
                //进行Word文档转换
                is=file.getInputStream();
                pdfPath=text2pdf(is);
                if(null != pdfPath){
                    //再进行PDF文档转换为tiff文件
                    File pdfFile=new File(pdfPath);
                    is=new FileInputStream( pdfFile );
                    resultBack=txtPdfToTiff(is,outputStream);
                }
            }
        } catch (IOException e) {
            logger.error("IO关闭异常:"+e);
        }finally {
            try {
                if (is != null){
                    is.close();
                }
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                logger.error("IO关闭异常:"+e);
            }
        }
        return resultBack;
    }

    @Override
    public Map<String,Object> getFileList(String tifPath) {
        Map<String,Object> map=new HashMap<>(  );
        File file=new File( tifPath);
        if(!file.exists()){
            map.put( "message","文件不存在" );
            return map;
        }else{
            List<String> pathList=readerTiff(tifPath);
            if(null==pathList){
                map.put( "message","文件预览失败" );
            }else{
                List<AgainSend> againSends=new ArrayList<>(  );
                for(int i=0;i<pathList.size();i++){
                    AgainSend againSend=new AgainSend();
                    againSend.setImgUrl( pathList.get( i ) );
                    againSends.add( againSend );
                }
                map.put( "message","成功" );
                map.put( "pathList",againSends );
            }
            return map;
        }
    }
    //生成电子签章
    @Override
    public boolean addNewElecSign(String signName,String type) {
        boolean flag=false;
        Date date=new Date(  );
        SimpleDateFormat sdf=new SimpleDateFormat( "yyyy年MM月dd日" );
        String year=sdf.format( date );
        BufferedImage image=startGraphics2D(signName,year,type);
        String filePath = schTask();//签章保存路径
        try {
            ImageIO.write(image, "tif", new File(filePath)); //将其保存在D:\\下，得有这个目录
            Elec_Sign elec_sign=new Elec_Sign();
            Date create_time=new Date(  );
            elec_sign.setCreate_time(create_time);
            elec_sign.setSignName( signName );
            elec_sign.setSavePath( filePath );
            elec_sign.setSignType( type );
            elecSignMapper.insertElecSign(elec_sign);
            flag=true;
        } catch (Exception e) {
            logger.error( e.toString() );
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean delElecSign(Integer id, String savePath) {
        //删除数据库
        int count=elecSignMapper.deleteSignById(id);
        if(count==0){
            return false;
        }else{
            File file=new File( savePath );
            if(file.exists()){
                file.delete();
            }
            return true;
        }
    }

    @Override
    public List<Elec_Sign> selectElecSign() {
        List<Elec_Sign> list=elecSignMapper.selectElecSign();
        for(Elec_Sign elec_sign:list){
            String savePath=elec_sign.getSavePath();
            if(null !=savePath){
                String jpgList= null;
                jpgList = tifToPng(savePath);
                if(null!=jpgList){
                    elec_sign.setJpgPath( jpgList );
                }
            }
        }
        return list;
    }


    public static String tifToPng(String savePath){
        String pngPath=TEMPDIR+"/"+GetTimeToFileName.GetTimeToFileName()+".png";
        RenderedOp ro = JAI.create("fileload", savePath);
        OutputStream os = null;
        try {
            os = new FileOutputStream(pngPath);
            PNGEncodeParam param = new PNGEncodeParam.RGB();
            ImageEncoder ie = ImageCodec.createImageEncoder("PNG", os, param);
            ie.encode(ro);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            logger.error( e.toString() );
        } catch (IOException e) {
            logger.error( e.toString() );
        }
        return pngPath;
    }

    @Override
    public List<Elec_Sign> selectElecSignName() {
        List<Elec_Sign> elec_signs=elecSignMapper.selectElecSignName();
        return elec_signs;
    }

    @Override
    public boolean sealOnFile(String tifPath,Integer id) {
        boolean over=false;
        ImageOutputStream os=null;
        try {
            File file=new File( tifPath);
            List<String> list=makeSingleTif(file);
            //将新的李斯特集合的第一条信息拿出来,进行电子签章合并
            String firstPage=list.get( 0 );
            //查询电子签章的路径
            String savePath=elecSignMapper.selectElecSignById(id);
            if(null == savePath || "".equals( savePath )){
                over=false;
            }else{
                String topath=fileTemp()+".tif";//盖了章的第一页保存路径
                boolean flag=mergeBothImageRightbottom(firstPage,savePath,topath);//进行盖章操作
                if(flag){
                    list.set( 0,topath );
                    File file2=new File( tifPath );
                    os=new FileImageOutputStream( file2 );
                    boolean f=tiffMerge(list,os);
                    if(f){
                        over=true;
                    }else{
                        over=false;
                    }
                }else{
                    over=false;
                }
            }
        } catch (IOException e) {
            logger.error( e.toString() );
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
        }
        return over;
    }

}
