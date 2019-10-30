package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/23 9:52
 *@功能:
 */

import com.hx.common.Fax;
import com.hx.dao.DeviceDao;
import com.hx.modle.Device_Setting;
import com.hx.service.SendFaxService;
import com.hx.util.GetTimeToFileName;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

import static com.hx.change.ChangeFile.baseToPdf;
import static com.hx.change.ChangeFile.pdfToTiffBase64;
import static com.hx.common.StaticFinal.TEMPDIR;


@Service("sendFaxService")
public class SendFaxServiceImpl implements SendFaxService {
    private static Logger logger=Logger.getLogger( SendFaxServiceImpl.class);
    @Autowired
    private DeviceDao deviceDao;
    //查询座机号下拉列表框
    @Override
    public List<String> selectSeatNumber() {
        List<String> numbers=deviceDao.selectSeatNumber();
        return numbers;
    }
    //初始化板卡时查询通道类型
    @Override
    public List<Device_Setting> selectChType() {
        List<Device_Setting> list=deviceDao.selectChType();
        return list;
    }

    @Override
    public String baseToTif(String base64) {
        String pdfPath=baseToPdf(base64);
        File file=new File( pdfPath );
        String tifPath=TEMPDIR+"\\"+GetTimeToFileName.GetTimeToFileName()+".tif";
        try {
            OutputStream os=new FileOutputStream( new File( tifPath ) );
            boolean flag=pdfToTiffBase64(file,os);
            if(!flag){
                tifPath="文件转换错误";
                if(file.isFile()){
                    file.delete();
                }
            }else{
                //删除临时的PDF文件
                if(file.isFile()){
                    file.delete();
                }
            }
        } catch (FileNotFoundException e) {
            logger.error( e.toString() );
        } catch (IOException e) {
            logger.error( e.toString() );
        }
        return tifPath;
    }

    @Override
    public String sendFax(String tifPath, String base64, String courtName, String receiveNumber, String sendNumber, int isBack,int ch) {
        //查询数据库是否有前缀0,
        //获取本地号码前缀和前缀长度,查看接收方号码是否是同区号码,同区号码,去除接收方区号
        receiveNumber=getReceiveNumber(sendNumber,receiveNumber);
        int isNumber=sendNumber.length();
        String message="未找到空闲通道";
        if(isNumber>0){
            //通过指定号码发送,然后查询通道是否空闲
            int isFree=Fax.INSTANCE.SsmGetChState(ch);
            if(isFree==0){
                //进行发送
                String Msg=sendFreeCh(receiveNumber,ch);
                if(Msg.equals( "通话中" )){
                    message=faxSendStart(ch,tifPath,base64,isBack);
                }else{
                    message=Msg;
                }
            }
        }else{
            //随机选择一个号码发送,查询空闲通道,然后查询该通道是否支持发送
            for(int i=0;i<4;i++){
                int isFree=Fax.INSTANCE.SsmGetChState(i);
                if(isFree==0){
                    sendNumber=deviceDao.selectSeatNumberByCh(i);
                    isNumber=sendNumber.length();
                    if(isNumber>0){
                        //进行发送
                        String Msg=sendFreeCh(receiveNumber,i);
                        if(Msg.equals( "通话中" )){
                            message=faxSendStart(ch,tifPath,base64,isBack);
                        }else{
                            message=Msg;
                        }
                        break;
                    }
                }
            }
        }
        return message;
    }
    //对接收方的号码进行更改
    public String getReceiveNumber(String sendNumber,String receiveNumber){
        Device_Setting deviceSetting=deviceDao.selectPrefix(sendNumber);
        int prefixLength=deviceSetting.getPrefix().length();
        if(prefixLength!=0){
            String code=deviceSetting.getAreaCode();
            int codeLength=code.length();
            String codeR=receiveNumber.substring(0,codeLength);
            if(codeR.equals(code)){
                receiveNumber=deviceSetting.getPrefix()+receiveNumber.substring( codeLength, receiveNumber.length());
            }
        }else{
            String code=deviceSetting.getAreaCode();
            int codeLength=code.length();
            String codeR=receiveNumber.substring(0,codeLength);
            if(codeR.equals(code)){
                receiveNumber=receiveNumber.substring( codeLength, receiveNumber.length());
            }
        }
        return receiveNumber;
    }
    public String sendFreeCh( String receiveNumber,int ch){
        //摘机
        int isPick=Fax.INSTANCE.SsmPickup(ch);
        String errMsg="成功";
        if(isPick==0){
            int isDial=Fax.INSTANCE.SsmAutoDial(ch,receiveNumber);
            if(isDial==0){
                try {
                    errMsg=checkState(ch);
                } catch (Exception e) {
                    logger.error("去话呼叫失败原因:"+errMsg);
                }
            }else{
                //调用失败
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("去话呼叫失败原因:"+errMsg);
            }
        }else{
            errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
            logger.error("摘机失败原因:"+errMsg);
        }
        return errMsg;
    }


    public String checkState(int ch){
        int state=Fax.INSTANCE.SsmGetChState(ch);
        String Msg="成功";
        if(state==3){
            Msg="通话中";
            return Msg;
        }else if(state==7){
            //挂起状态
            int pend=Fax.INSTANCE.SsmGetPendingReason(ch);
            switch(pend){
                case -1:
                    Msg="发送失败(调用失败)";
                    break;
                case 0:
                    Msg="发送失败(未检测到拨号音)";
                    break;
                case 1:
                    Msg="发送失败(对方繁忙)";
                    break;
                case 2:
                    Msg="发送失败(检测到回铃音后，线路上保持静默，驱动程序无法判别被叫是否摘机)";
                    break;
                case 3:
                    Msg="发送失败(对方未接听)";
                    break;
                case 4:
                    Msg="发送失败(对端挂机)";
                    break;
                case 5:
                    Msg="发送失败(未在线路上检测到回铃音和任何其它的语音信号，驱动程序无法判断被叫用户是否摘机)";
                    break;
            }
        }else{
            try {
                Thread.sleep( 1000 );
                checkState(ch);
            } catch (InterruptedException e) {
                logger.error("Thread.sleep异常:"+e.toString());
            }
        }
        return Msg;
    }
    public String faxSendStart(int ch,String tifPath,String base64,int isBack){
        String errMsg="成功";
        int j=8;
        //查询空闲的传真通道
        if(ch==0){
            j=8;
        }else if(ch==1){
            j=9;
        }else if(ch==2){
            j=10;
        }else{
            j=11;
        }
        int stateJ=Fax.INSTANCE.SsmGetChState(j);
        if(stateJ==0){
            //建立模拟中继线和传真资源通道连接
            int linkOk=Fax.INSTANCE.SsmTalkWith(ch,j);
            if(linkOk==0){
                int sendOk=Fax.INSTANCE.SsmFaxStartSend(j,tifPath);
                if(sendOk==0){
                    errMsg=sendIng(base64,j,ch,isBack);
                    return errMsg;
                }else{
                    errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                    logger.error( "发送失败:"+errMsg );
                }
            }else{
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error( "建立连接失败:"+errMsg );
            }
        }
        return errMsg;
    }
    public String sendIng(String base64,int j,int ch,int isBack){
        int jState=Fax.INSTANCE.SsmGetChState(j);
        String errMsg="成功";
        //发送完成状态
        if(jState==0){
            //正文以及回执页
            if(isBack==0){
                if(base64!=null){
                    //第二次发送
                    System.out.println("开始发送第二份");
                    int sendOk=Fax.INSTANCE.SsmFaxStartSend(j,base64);
                    if(sendOk==0){
                        errMsg=sendCheckEndAgain(j,ch);
                        return errMsg;
                    }else{
                        errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                        logger.error( "发送失败:"+errMsg );
                    }
                }else{
                    errMsg="发送失败(回执文件为空)";
                }
            }else if(isBack==1){
                //仅正文
                int isStop=stopAndHungUp(ch,j);
                if(isStop==0){
                    //将文件存入发件箱
                    logger.info( "发送成功" );
                }else{
                    errMsg="发送失败(断开连接失败)";
                }
            }else{
                //仅回执页
                int isStop=stopAndHungUp(ch,j);
                if(isStop==0){

                    //将文件存入发回执箱
                    logger.info( "发送成功" );
                }else{
                    errMsg="发送失败(断开连接失败)";
                }
            }
        }else{
            try {
                Thread.sleep( 1000 );
                errMsg=sendIng(base64,j,ch,isBack);
            } catch (InterruptedException e) {
                logger.error( "Thread.sleep线程异常:"+e.toString() );
            }
        }
        return errMsg;
    }
    public String sendCheckEndAgain(int j,int ch){
        String msg="成功";
        int jState=Fax.INSTANCE.SsmGetChState(j);
        if(jState==0){
            int isStop=stopAndHungUp(ch,j);
            if(isStop==0){
                //将文件存入发回执箱以及发件箱
                logger.info( "发送成功" );
            }else{
                msg="失败(断开连接失败)";
                logger.error( "断开连接失败" );
            }
        }else{
            try {
                Thread.sleep( 1000 );
                msg=sendCheckEndAgain(j,ch);
            } catch (InterruptedException e) {
                logger.error( "Thread.sleep线程异常:"+e.toString() );
            }
        }
        return msg;
    }
    public int stopAndHungUp(int ch,int j){
        int flag=-1;
        String errMsg="";
        int isStop=Fax.INSTANCE.SsmStopTalkWith(ch,j);
        if(isStop==0){
            int hangup=Fax.INSTANCE.SsmHangup(ch);
            if(hangup==0){
                flag=0;
            }else{
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("挂机失败:"+errMsg);
            }
        }else{
            errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
            logger.error("断开连接失败:"+errMsg);
        }
        return flag;
    }
}
