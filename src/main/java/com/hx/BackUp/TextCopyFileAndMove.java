package com.hx.BackUp;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/24 15:29
 *@功能:
 */

import org.apache.log4j.Logger;

import java.io.File;

public class TextCopyFileAndMove {
    private static Logger logger=Logger.getLogger( TextCopyFileAndMove.class );
    //start:待转移的文件夹    aim:转移后的文件夹
    public static boolean fileMove(String start, String aim){
        boolean flag=false;
        try {
            File dir = new File(start);
            if(!dir.exists()){
                return false;
            }
            File[] files = dir.listFiles();// 将文件或文件夹放入文件集
            if (files == null)// 判断文件集是否为空
                return false;
            File moveDir = new File(aim);// 创建目标目录
            if (!moveDir.exists()) {// 判断目标目录是否存在
                moveDir.mkdirs();// 不存在则创建
            }
            for (int i = 0; i < files.length; i++) {// 遍历文件集
                if (files[i].isDirectory()) {// 如果是文件夹或目录,则递归调用fileMove方法，直到获得目录下的文件
                    fileMove(files[i].getPath(), aim + "\\" + files[i].getName());// 递归移动文件
                    files[i].delete();// 删除文件所在原目录
                }
                File moveFile = new File(moveDir.getPath() + "\\"// 将文件目录放入移动后的目录
                        + files[i].getName());
                if (moveFile.exists()) {// 目标文件夹下存在的话，删除
                    moveFile.delete();
                }
                files[i].renameTo(moveFile);// 移动文件
            }
            dir.delete();
            flag=true;
        } catch (Exception e) {
            flag=false;
            logger.error( e.toString() );
        }
        return flag;
    }
}
