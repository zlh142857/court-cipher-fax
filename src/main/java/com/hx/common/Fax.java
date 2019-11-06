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
    //取得指定通道是否支持接收主叫号码信息的操作。
    int SsmChkOpCallerId (int ch);
    //获取来话呼叫的主叫方号码信息。
    String SsmGetCallerIdA(int ch);
    //通道连接
    int SsmTalkWith(int ch1,int ch2);
    //开启接收传真
    int SsmFaxStartReceive(int ch,String pszFileName);
    //获取收发的传真速率
    int SsmFaxGetSpeed(int ch);
    //获取返回的事件
    int SsmWaitForEvent(int dwTimeOut,MESSAGE_INFO pEvent);
    //获取已经发送或者接收完成的页数
    int SsmFaxGetPages(int ch);
    //断开连接
    int SsmStopTalkWith(int ch1,int ch2);
    //挂机,回归空闲通道
    int SsmHangup(int ch);

    //设置本端的电话号码
    int SsmFaxSetID(int ch,String szID);
    //发送传真文件
    int SsmFaxStartSend(int ch,String filename);

    //去电呼叫
    int SsmAutoDial(int ch,String szPhoNum);
    //启动事件
    int SsmSetEvent(int wEvent, int nReference, boolean bEnable, PEVENT_SET_INFO pEventSet);
    int SsmChkAutoDial(int ch);
    //获取去电呼叫失败原因
    int SsmGetAutoDialFailureReason(int ch);
    //获取通道进入S_CALL_PENDING状态的具体原因。通道挂起原因
    int SsmGetPendingReason(int ch);

    int SsmSetTxCallerId(int ch, String pszTxCallerId);
    //获取传真进程音检测器的检测结果。
    int SsmGetVocFxFlag(int ch, int nSelFx, boolean bClear);
    //获取接收到的文件编码的格式
    int SsmFaxGetCodeMode(int ch, int dwReserver);

    int SsmRecToFile(int ch, String pszFileName, int nFormat, int dwStartPos, int dwBytes, int dwTime, int nMask);
    int SsmFaxCheckEnd(int ch);
    //发送多份文件
    int SsmFaxSendMultiFile(int ch, String szPath, String szFile);







}
