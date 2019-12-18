package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/7 10:45
 *@功能:
 */

public class ChStateCode {
    public static String getStateMsgByNot7(int code){
        String msg="";
        switch(code){
            case 1:
                msg="摘机";
                break;
            case 2:
                msg="振铃";
                break;
            case 3:
                msg="通话";
                break;
            case 4:
                msg="等待拨号音";
                break;
            case 5:
                msg="拨号";
                break;
            case 6:
                msg="等待拨号结果";
                break;
            case 8:
                msg="线路断开状态";
                break;
            case 9:
                msg="等候被叫用户摘机";
                break;
        }
        return msg;
    }
    public static String getStateMsgBy7(int code){
        String msg="";
        switch(code){
            case 0:
                msg="未检测到拨号音";
                break;
            case 1:
                msg="对方繁忙";
                break;
            case 2:
                msg="检测到回铃音后，线路上保持静默，驱动程序无法判别被叫是否摘机";
                break;
            case 3:
                msg="被叫用户未应答";
                break;
            case 4:
                msg="对端用户挂机";
                break;
            case 5:
                msg="未在线路上检测到任何语音信号，无法判断对端是否摘机";
                break;
        }
        return msg;
    }
    public static String getStateMsgByFaxCh(int code){
        String msg="";
        switch(code){
            case 50:
                msg="状态转移过程中";
                break;
            case 51:
                msg="传真呼叫建立";
                break;
            case 52:
                msg="传真报文前处理";
                break;
            case 54:
                msg="传真报文传输前传输训练";
                break;
            case 55:
                msg="传真报文传输中";
                break;
            case 56:
                msg="传真报文后处理";
                break;
            case 57:
                msg="传真报文传输下一页";
                break;
            case 58:
                msg="传真发送中报文传输结束";
                break;
            case 59:
                msg="传真呼叫释放";
                break;
            case 60:
                msg="复位MODEM";
                break;
            case 61:
                msg="初始化MODEM";
                break;
            case 62:
                msg="传真接收，接收发方的DCS信号";
                break;
            case 63:
                msg="传真接收，发送训练失败信号FTT";
                break;
            case 64:
                msg="传真接收，发送可接受的证实信号CFR";
                break;
            case 65:
                msg="传真进行后续协商";
                break;
            case 66:
                msg="传真发送PPS后接收PPR信号";
                break;
            case 67:
                msg="传真进行数据重发";
                break;
            case 68:
                msg="4次重发后,进行肯定协商";
                break;
            case 69:
                msg="要求发送方重新发送数据";
                break;
            case 300:
                msg="传真多次重发后，进行否定处理";
                break;
            case 301:
                msg="接收方忙";
                break;
            case 302:
                msg="接收报文否定及重新训练";
                break;
            case 303:
                msg="下页发送需要从PhaseB开始，重新训练";
                break;
            case 400:
                msg="处在V.8训练阶段";
                break;
        }
        return msg;
    }
    public static String checkChCode(int code){
        String msg="";
        switch(code){
            case -1:
                msg="检测失败";
                break;
            case 0:
                msg="线路正常";
                break;
            case 7:
                msg="挂起状态";
                break;
            case 8:
                msg="线路断开状态";
                break;
            case 11:
                msg="通道不可用";
                break;
            case 12:
                msg="呼出锁定";
                break;
            case 20:
                msg="本地闭塞";
                break;
            default:
                msg="线路正常";
                break;
        }
        return msg;
    }
}
