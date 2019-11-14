package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/24 10:40
 *@功能:
 */

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.hx.common.StaticFinal.*;

//获取前台传到服务器端的Word文档,将Word文档临时保存到服务器文件夹内
public class TempDir {
    //发送前要保存的临时文件夹文件路径
    public static String makeTempDir(MultipartFile file) throws IOException {
        String FilePath=TEMPDIR+"/"+file.getOriginalFilename();
        File fileDir=new File( TEMPDIR );
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File fileSave=new File( FilePath );
        file.transferTo( fileSave );
        return FilePath;
    }
    //待发送的文件夹路径
    public static String fileTemp() throws IOException {
        File fileDir=new File( TEMPDIR );
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String tifFilePath=TEMPDIR+"/"+GetTimeToFileName.GetTimeToFileName();
        return tifFilePath;
    }
    //返回要保存的接收的传真的路径
    public static String tifDir(){
        File fileDir=new File( TiffDir );
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String tifFilePath=TiffDir+"/"+GetTimeToFileName.GetTimeToFileName()+".tif";
        return tifFilePath;
    }
    //返回要保存的接收的传真的路径
    public static String schTask(){
        File fileDir=new File( SCHTASK );
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String tifFilePath=SCHTASK+"/"+GetTimeToFileName.GetTimeToFileName()+".tif";
        return tifFilePath;
    }
}
