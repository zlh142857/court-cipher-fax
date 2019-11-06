package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/23 9:52
 *@功能:
 */

import com.hx.common.Fax;
import com.hx.dao.DeviceDao;
import com.hx.dao.InboxMapper;
import com.hx.dao.OutboxMapper;
import com.hx.dao.SendReceiptMapper;
import com.hx.modle.Device_Setting;
import com.hx.modle.Outbox;
import com.hx.modle.Send_Receipt;
import com.hx.service.SendFaxService;
import com.hx.util.GetTimeToFileName;
import com.spire.barcode.BarCodeGenerator;
import com.spire.barcode.BarCodeType;
import com.spire.barcode.BarcodeSettings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.List;

import static com.hx.change.ChangeFile.baseToPdf;
import static com.hx.change.ChangeFile.pdfToTiffByWord;
import static com.hx.util.TempDir.fileTemp;


@Service("sendFaxService")
public class SendFaxServiceImpl implements SendFaxService {
    private static Logger logger=Logger.getLogger( SendFaxServiceImpl.class);
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private OutboxMapper outboxMapper;
    @Autowired
    private SendReceiptMapper SendReceiptMapper;
    @Autowired
    private InboxMapper inboxMapper;
    //查询座机号下拉列表框
    @Override
    public List<Device_Setting> selectSeatNumber() {
        List<Device_Setting> numbers=deviceDao.selectSeatNumber();
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
        String tifPath="";
        OutputStream os=null;
        try {
            tifPath=fileTemp()+".tif";
            String pdfPath=baseToPdf(base64);
            File file=new File( pdfPath );
            os=new FileOutputStream( new File( tifPath ) );
            boolean flag=pdfToTiffByWord(file,os);
            if(!flag){
                tifPath=null;
            }
            return tifPath;
        } catch (FileNotFoundException e) {
            logger.error( e.toString() );
            return null;
        } catch (IOException e) {
            logger.error( e.toString() );
            return null;
        } catch (Exception e){
            logger.error( e.toString() );
            return null;
        }finally {
            try {
                if(os!=null){
                    os.close();
                }
            } catch (IOException e) {
                logger.error( e.toString() );
            }
        }

    }

    @Override
    public String createBarCode() throws IOException {
        String uuid=GetTimeToFileName.GetTimeToFileName();
        BarcodeSettings settings = new BarcodeSettings();//创建BarcodeSettings实例
        settings.setType(BarCodeType.Code_128);//指定条码类型
        settings.setData(uuid);//设置条码数据
        settings.setData2D(uuid);//设置条码显示数据
        settings.setShowTextOnBottom(true);//设置数据文本显示在条码底部
        settings.setX(0.8f);//设置黑白条宽度
        settings.setImageHeight(50);//设置生成的条码图片高度
        settings.setImageWidth(70);//设置生成的条码图片宽度
        settings.setBackColor(new Color(250,255,255));//设置条码背景色
        BarCodeGenerator barCodeGenerator = new BarCodeGenerator(settings);//创建BarCodeGenerator实例
        BufferedImage bufferedImage = barCodeGenerator.generateImage();//根据settings生成图像数据，保存至BufferedImage实例
        String barPath=fileTemp()+".png";
        File file=new File(barPath);
        ImageIO.write(bufferedImage, "png",file);//保存条码为PNG图片
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }

    @Override
    public String sendFax(String tifPath, String base64, String courtName, String receiveNumber,
                          String sendNumber, String isBack,String filename,String id,String ch) {
        receiveNumber=getReceiveNumber(sendNumber,receiveNumber);
        int isNumber=sendNumber.length();
        String message="成功";
        if(isNumber>0){
            //通过指定号码发送,然后查询通道是否空闲
            int isFree=Fax.INSTANCE.SsmGetChState( Integer.parseInt( ch ) );
            System.out.println("isFree:"+isFree);
            if(isFree==0){
                //进行发送
                String Msg=sendFreeCh(receiveNumber,Integer.parseInt( ch ));
                if(Msg.equals( "通话中" )){
                    message=faxSendStart(Integer.parseInt( ch ),tifPath,base64,Integer.parseInt( isBack ));
                    //已经断开连接,根据isBack判断发送的文件,然后存入数据库
                    if(Integer.parseInt( isBack )==2){
                        //isBack==2,说明只有回执页,所以存入数据到发回执箱
                        //在发回执箱增加记录
                        //发回执更改收件箱是否已回执,改成1,已回执
                        insertDataReceipt( message,receiveNumber,filename,sendNumber,courtName );
                        if(id.length()>0){
                            inboxMapper.updateIsReceiptById(Integer.parseInt( id ));
                        }else{
                            logger.error( "id为空,无法修改是否已回执" );
                        }
                    }else{
                        //isBack==1/0,说明只有正文或者两个文件,所以存入数据到发件箱
                        insertDataOutBox( message,receiveNumber,filename,sendNumber,courtName );
                    }
                }else{

                    message=Msg;
                    if(Integer.parseInt( isBack )==2){
                        //发送失败,不更改已发送回执状态
                        insertDataReceipt( message,receiveNumber,filename,sendNumber,courtName );
                    }else{
                        insertDataOutBox( message,receiveNumber,filename,sendNumber,courtName );
                    }
                }
            }else if(isFree==7){
                Fax.INSTANCE.SsmHangup( Integer.parseInt( ch ) );
            }else{
                message="模拟中继线通道不为空闲状态";
            }
        }else{
            //随机选择一个号码发送,查询空闲通道,然后查询该通道是否支持发送
            for(int i=0;i<4;i++){
                int isFree=Fax.INSTANCE.SsmGetChState(i);
                System.out.println("isFree:"+isFree);
                if(isFree==0){
                    sendNumber=deviceDao.selectSeatNumberByCh(i);
                    isNumber=sendNumber.length();
                    if(isNumber>0){
                        //进行发送
                        String Msg=sendFreeCh(receiveNumber,i);
                        if(Msg.equals( "通话中" )){
                            message=faxSendStart(Integer.parseInt( ch ),tifPath,base64,Integer.parseInt( isBack ));
                            if(Integer.parseInt( isBack )==2){
                                insertDataReceipt( message,receiveNumber,filename,sendNumber,courtName );
                                inboxMapper.updateIsReceiptById(Integer.parseInt( id ));
                            }else{
                                insertDataOutBox( message,receiveNumber,filename,sendNumber,courtName );
                            }
                        }else{
                            message=Msg;
                            if(Integer.parseInt( isBack )==2){
                                insertDataReceipt( message,receiveNumber,filename,sendNumber,courtName );
                            }else{
                                insertDataOutBox( message,receiveNumber,filename,sendNumber,courtName );
                            }
                        }
                        break;
                    }
                }else if(isFree==7){
                    Fax.INSTANCE.SsmHangup( Integer.parseInt( ch ) );
                }else{
                    message="模拟中继线通道不为空闲状态";
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
                    errMsg=checkState(ch,errMsg);
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


    public String checkState(int ch,String Msg){
        int state=Fax.INSTANCE.SsmGetChState(ch);
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
                Msg=checkState(ch,Msg);
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
                int sendFlag=-1;
                if(isBack==0){
                    //两份文件
                    String szFile=getfileList(tifPath,base64);
                    sendFlag=Fax.INSTANCE.SsmFaxSendMultiFile(j,"D:\\\\tempDir\\\\",szFile);
                }else if(isBack==1){
                    //仅正文
                    sendFlag=Fax.INSTANCE.SsmFaxStartSend(j,tifPath);
                }else{
                    //回执页
                    int baseLength=base64.length();
                    System.out.println(base64);
                    if(baseLength>0){
                        sendFlag=Fax.INSTANCE.SsmFaxStartSend(j,base64);
                    }else{
                        errMsg="获取回执文件失败";
                    }
                }
                if(sendFlag==0){
                    //等待通道为空闲状态
                    sendEndFor0(j);
                    logger.info("发送成功");
                }else{
                    String e=Fax.INSTANCE.SsmGetLastErrMsgA();
                    logger.error( "发送失败:"+e );
                    errMsg="发送失败";
                }
                //空闲后挂起
                stopAndHungUp(ch,j);
            }else{
                String e=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error( "建立连接失败:"+e );
                errMsg="建立连接失败";
            }
        }else{
            errMsg="没有空闲通道";
        }
        return errMsg;
    }
    public static int sendEndFor0(int i){
        int end=Fax.INSTANCE.SsmGetChState(i);
        if(end!=0){
            try {
                Thread.sleep( 1000 );
                end=sendEndFor0(i);
            } catch (InterruptedException e) {
                logger.error( "Thread.sleep异常:"+e.toString() );
            }
        }
        return end;
    }
    public void stopAndHungUp(int ch,int j){
        String errMsg="";
        int isStop=Fax.INSTANCE.SsmStopTalkWith(ch,j);
        if(isStop==0){
            int hangup=Fax.INSTANCE.SsmHangup(ch);
            int isFree=Fax.INSTANCE.SsmGetChState( ch);
            if(hangup!=0){
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("挂机失败:"+errMsg);
            }
        }else{
            errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
            logger.error("断开连接失败:"+errMsg);
        }
    }
    public void insertDataOutBox(String message,String receiveNumber,String filename,String sendNumber,String courtName){
        Outbox outbox=new Outbox();
        outbox.setReceivenumber( receiveNumber );
        if(message.equals( "成功" )){
            outbox.setMessage( "成功" );
        }else{
            outbox.setMessage( "失败("+message+")" );
        }
        outbox.setSendline( filename );
        outbox.setSendernumber( sendNumber );
        Date date=new Date();
        outbox.setCreate_time(date);
        outbox.setReceivingunit( courtName );
        outboxMapper.insertNewMessage(outbox);
    }
    public void insertDataReceipt(String message,String receiveNumber,String filename,String sendNumber,String courtName){
        Send_Receipt sendReceipt=new Send_Receipt();
        sendReceipt.setReceivenumber( receiveNumber );
        if(message.equals( "成功" )){
            sendReceipt.setMessage( "成功" );
        }else{
            sendReceipt.setMessage( "失败("+message+")" );
        }
        sendReceipt.setSendline( filename );
        sendReceipt.setSendnumber( sendNumber );
        Date date=new Date();
        sendReceipt.setCreate_time(date);
        sendReceipt.setReceivingunit( courtName );
        SendReceiptMapper.insertNewMessage(sendReceipt);
    }
    public static void deleteFiles(String tifPath,String base64){
        File file=new File(tifPath);
        if(file.isFile()){
            file.delete();
        }
        File file2=new File(base64);
        if(file2.isFile()){
            file2.delete();
        }
    }
    public static String getfileList(String tifPath,String base64){
        String [] main=tifPath.split("/");
        String mainText=main[main.length-1];
        String [] receipt=base64.split("/");
        String receiptText=receipt[receipt.length-1];
        String fileList=mainText+";"+receiptText;
        return fileList;
    }
}
