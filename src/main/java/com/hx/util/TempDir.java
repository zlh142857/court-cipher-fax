package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/24 10:40
 *@功能:
 */


import java.io.File;

import static com.hx.common.StaticFinal.*;

//获取前台传到服务器端的Word文档,将Word文档临时保存到服务器文件夹内
public class TempDir {
    //待发送的文件夹路径
    public static String fileTemp(){
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
