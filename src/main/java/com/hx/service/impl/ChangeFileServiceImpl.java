package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/19 15:58
 *@功能:
 */

import com.hx.service.ChangeFileService;
import com.hx.util.GetTimeToFileName;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.hx.change.ChangeFile.pdfToTiff;
import static com.hx.change.ChangeFile.pdfToTiffByWord;
import static com.hx.change.ChangeFile.wordToPDF;
import static com.hx.common.StaticFinal.TEMPDIR;

@Service("changeFileService")
public class ChangeFileServiceImpl implements ChangeFileService {
    private static Logger logger=Logger.getLogger(ChangeFileServiceImpl.class);
    //转换文件格式为tiff
    @Override
    public String changeFileSend(MultipartFile file) {
        String message="文件转换失败";//默认为0,0为不支持文件格式,1为文件转换成功,-1为转换失败,-2为上传文件为空
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
                String tifPath=TEMPDIR+"\\"+GetTimeToFileName.GetTimeToFileName()+".tif";
                if(suffix.equals( ".doc" )||suffix.equals( ".docx" )){
                    back=mkdirDir(file,fileType,tifPath);
                }else if(suffix.equals( ".pdf" )){
                    fileType=1;
                    back=mkdirDir(file,fileType,tifPath);
                }else{
                    message="不支持该文件格式";
                    return message;
                }
                if(back){
                    message=tifPath;
                    return message;
                }
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
        return message;
    }
    //进行存放tiff文件目录创建,然后判断文件为word还是PDF转换方式
    public static boolean mkdirDir(MultipartFile file,int fileType,String tifPath){
        boolean resultBack=false;
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try {
            inputStream=file.getInputStream();
            //先判断目录名是否存在
            File fileDir=new File( TEMPDIR );
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            outputStream= new FileOutputStream(tifPath);
            if(fileType==0){
                //进行Word文档转换
                String pdfPath=wordToPDF(file);
                //再进行PDF文档转换为tiff文件
                File pdfFile=new File(pdfPath);
                resultBack=pdfToTiffByWord(pdfFile,outputStream);
            }else{
                //进行PDF文档转换
                resultBack=pdfToTiff(file,outputStream);
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
}
