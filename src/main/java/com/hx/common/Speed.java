package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/12 14:30
 *@功能:
 */

public class Speed {
    public static int getSpeed(int speedFlag){
        int msg=0;
        switch(speedFlag){
            case -1:
                msg=-1;
                break;
            case 24:
                msg=2400;
                break;
            case 48:
                msg=4800;
                break;
            case 72:
                msg=96;
                break;
            case 120:
                msg=12000;
                break;
            case 144:
                msg=14400;
                break;
            case 168:
                msg=16800;
                break;
            case 192:
                msg=19200;
                break;
            case 216:
                msg=21600;
                break;
            case 240:
                msg=24000;
                break;
            case 264:
                msg=26400;
                break;
            case 288:
                msg=28800;
                break;
            case 312:
                msg=31200;
                break;
            case 336:
                msg=33600;
                break;
        }
        return msg;
    }
}
