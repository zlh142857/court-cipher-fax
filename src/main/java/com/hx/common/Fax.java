package com.hx.common;


import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Fax extends Library {
    Fax INSTANCE=(Fax) Native.loadLibrary("SHP_A3",Fax.class);
    //初始化板卡
    int SsmStartCti(String lpSsmCfgFileName,String lpIndexCfgFileName);
    //关闭驱动程序
    int SsmCloseCti();
    //获取通道当前的状态
    int SsmGetChState(int ch);
    //获取最新一次调用API函数失败时的错误信息
    String SsmGetLastErrMsgA();
    //摘机
    int SsmPickup(int ch);
    //获取来话呼叫的主叫方号码信息。
    String SsmGetCallerIdA(int ch);
    //通道连接
    int SsmTalkWith(int ch1,int ch2);
    //开启接收传真
    int SsmFaxStartReceive(int ch,String pszFileName);
    //获取收发的传真速率
    int SsmFaxGetSpeed(int ch);
    //获取已经发送或者接收完成的页数
    int SsmFaxGetPages(int ch);
    //断开连接
    int SsmStopTalkWith(int ch1,int ch2);
    //挂机,回归空闲通道
    int SsmHangup(int ch);
    //发送传真文件
    int SsmFaxStartSend(int ch,String filename);
    //去电呼叫
    int SsmAutoDial(int ch,String szPhoNum);
    //获取通道进入S_CALL_PENDING状态的具体原因。通道挂起原因
    int SsmGetPendingReason(int ch);
    //发送多份文件
    int SsmFaxSendMultiFile(int ch, String szPath, String szFile);
    //终止发送或者接收
    int SsmFaxStop(int ch);
    //发送传真时，获取当前传真发送页所包含的总字节数。
    int SsmFaxGetAllBytes(int ch);
    //发送传真时，获取当前发送页中已经完成发送的字节数。
    int SsmFaxGetSendBytes(int ch);
    //接收传真时，获取当前接收页中已经完成接收的字节数
    int SsmFaxGetRcvBytes(int ch);
    //设置发送或接收的速率
    int SsmFaxSetChSpeed(int ch, int speed);
    //检测电压
    int SsmGetLineVoltage(int ch);
    //放音
    int SsmPlayFile(int ch, String pszFileName, int nFormat, int dwStartPos, int dwLen);
    //停止放音
    int SsmStopPlayFile(int ch);


}
