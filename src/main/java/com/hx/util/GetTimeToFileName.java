package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/23 10:58
 *@功能:
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetTimeToFileName {
    public static String GetTimeToFileName(){
        SimpleDateFormat sFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Calendar calendar=Calendar.getInstance();
        //获取系统当前时间并将其转换为string类型
        String fileName=sFormat.format(calendar.getTime());
        return fileName;
    }
    public static String GetTimeToFileNameToBar(){
        SimpleDateFormat sFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar=Calendar.getInstance();
        //获取系统当前时间并将其转换为string类型
        String fileName=sFormat.format(calendar.getTime());
        return fileName;
    }
}
