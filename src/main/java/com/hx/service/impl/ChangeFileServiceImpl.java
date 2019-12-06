package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/19 15:58
 *@功能:
 */

import com.hx.service.ChangeFileService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hx.change.ChangeFile.PdfToTiff;
import static com.hx.change.ChangeFile.makeSingleTif;
import static com.hx.change.ChangeFile.wordToPDF;
import static com.hx.common.StaticFinal.SCHTASK;
import static com.hx.util.TempDir.schTask;

@Service("changeFileService")
public class ChangeFileServiceImpl implements ChangeFileService {
    private static Logger logger=Logger.getLogger(ChangeFileServiceImpl.class);
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
                }else{
                    message="不支持该文件格式";
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
        InputStream inputStream=null;
        ImageOutputStream outputStream=null;
        String pdfPath="";
        try {
            inputStream=file.getInputStream();
            //先判断目录名是否存在
            File fileDir=new File( SCHTASK );
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            outputStream= new FileImageOutputStream(new File( tifPath ));
            if(fileType==0){
                //进行Word文档转换
                pdfPath=wordToPDF(file);
                //再进行PDF文档转换为tiff文件
                File pdfFile=new File(pdfPath);
                InputStream is=new FileInputStream( pdfFile );
                resultBack=PdfToTiff(is,outputStream);
            }else{
                InputStream is=file.getInputStream();
                //进行PDF文档转换
                resultBack=PdfToTiff(is,outputStream);
            }
        } catch (IOException e) {
            logger.error("IO关闭异常:"+e);
        }finally {
            try {
                if (inputStream != null){
                    inputStream.close();
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
        }else{
            List<String> pathList=makeSingleTif(file);
            if(null==pathList){
                map.put( "message","文件预览失败" );
            }else{
                map.put( "message","成功" );
                map.put( "pathList",pathList );
            }
        }
        return map;
    }
}
