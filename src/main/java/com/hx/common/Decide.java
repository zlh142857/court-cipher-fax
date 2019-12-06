package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/11 11:01
 *@功能:
 */

import com.hx.dao.*;
import com.hx.modle.Inbox;
import com.hx.modle.Program_Setting;
import com.hx.modle.Return_Receipt;
import com.hx.modle.WebModel;
import com.hx.util.PrintImage;
import com.spire.barcode.BarCodeType;
import com.spire.barcode.BarcodeScanner;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.hx.controller.AlertController.*;
import static com.hx.util.TempDir.tifDir;
import static com.hx.util.TiffToJPEG.readerTiff;

//根据switch case判断当前状态码
@Component
public class Decide {
    private static Logger logger=Logger.getLogger(Decide.class);
    private static ProgramSettingDao programSettingDao;
    private static DeviceDao deviceDao;
    private static InboxMapper inboxMapper;
    private static ReturnReceiptMapper returnReceiptMapper;
    private static MailMapper mailMapper;
    private static Decide decide;
    public  void setProgramSettingDao(ProgramSettingDao programSettingDao) {
        this.programSettingDao = programSettingDao;
    }
    public  void setDeviceDao(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }
    public  void setInboxMapper(InboxMapper inboxMapper) {
        this.inboxMapper = inboxMapper;
    }
    public  void setReturnReceiptMapper(ReturnReceiptMapper returnReceiptMapper) {
        this.returnReceiptMapper = returnReceiptMapper;
    }
    public  void setMailMapper(MailMapper mailMapper) {
        this.mailMapper = mailMapper;
    }
    @PostConstruct
    public void init() {
        decide=this;
        decide.programSettingDao=this.programSettingDao;
        decide.inboxMapper=this.inboxMapper;
        decide.deviceDao=this.deviceDao;
        decide.returnReceiptMapper=this.returnReceiptMapper;
        decide.mailMapper=this.mailMapper;
    }
    public static void decideCh(int ch){
        Map<String,String> map=chState_2(ch);
        if(map.get( "message" ).equals( "成功" )){
            //先存入数据库再打印,先获取tiff文件页数,==1,并扫描有条形码,说明是回执页
            String tifPath=map.get( "tifPath" );
            int pages=0;
            if(tifPath.length()>0){
                pages=getTiffPages(tifPath);
                List<String> pathList=readerTiff(tifPath);
                String barCode=getFileType(pathList);//根据最后一页的jpg获取扫描结果
                if(pages==1){
                    if("".equals( barCode )){
                        //只有正文,设置是否回执状态为2:没有回执文件
                        insertMsg(ch,map.get( "callerId" ), tifPath,barCode);
                    }else{
                        //只有回执页
                        insertMsgReceipt(ch,map.get( "callerId" ), tifPath);
                    }
                }else{
                    insertMsg(ch,map.get( "callerId" ), tifPath,barCode);
                }
                //查询打印服务名称
                Program_Setting programSetting=decide.programSettingDao.selectProgramSetting();
                String printService=programSetting.getPrintService();
                if(programSetting.getIsPrint()==0){
                    try {
                        List<File> newList=new ArrayList<>(  );
                        for(int i=0;i<pathList.size();i++){
                            File file=new File( pathList.get( i ) );
                            newList.add( file );
                        }
                        PrintImage.printImageWhenReceive(newList,printService);
                    } catch (Exception e) {
                        logger.error( e.toString() );
                    }

                }
            }

        }
    }
    public static Map<String,String> chState_2(int ch){
        Map<String,String> map=new HashMap<>(  );
        String message="成功";
        String tifPath="";
        //摘机
        int pickup=Fax.INSTANCE.SsmPickup(ch);
        int i = 8;
        if(pickup==0){
            //获取发送方号码
            String callerId = Fax.INSTANCE.SsmGetCallerIdA( ch );
            //查找空闲软通道
            if (ch == 0) {
                i = 8;
            } else if (ch == 1) {
                i = 9;
            } else if (ch == 2) {
                i = 10;
            } else {
                i = 11;
            }
            //根据编号获取当前通道状态
            int code = Fax.INSTANCE.SsmGetChState( i );
            if (code == 0) {
                //空闲状态,建立连接
                int linkOk = Fax.INSTANCE.SsmTalkWith( ch, i );
                if (linkOk == 0) {
                    tifPath=tifDir();
                    int isOk=Fax.INSTANCE.SsmFaxStartReceive( i,tifPath);
                    if(isOk==0){
                        //开始接收,当通道状态为55的时候可以获取文件编码
                        //当通道状态为0的时候可以挂起或者接收第二次
                        int state= getCodeFlag(i,ch);
                        if (state==55) {
                            //说明已经在接收中
                            int end=sendEndFor0(i);
                            if(end==0){
                                //目前接收到了一份文件,tifPath不为空,已经接收结束,断开连接
                                map.put( "tifPath",tifPath );
                                map.put( "callerId",callerId );
                                logger.info("接收成功");
                                stopAndHungUp(ch,i);
                            }
                        }else{
                            message="接收失败";
                            logger.info("接收失败");
                        }
                    }else{
                        message = Fax.INSTANCE.SsmGetLastErrMsgA();
                        logger.error( "建立接收失败:"+message );
                    }
                } else {
                    message = Fax.INSTANCE.SsmGetLastErrMsgA();
                    logger.error( "建立连接失败:"+message );
                }
            }
        }
        map.put( "message",message );
        return map;
    }
    public static int getCodeFlag(int i,int ch){
        int end=Fax.INSTANCE.SsmGetChState(i);
        if(end==55){
            return end;
        }else if(end ==0){
            return end;
        }else if(end ==7){
            stopAndHungUp(i,ch);
        }else{
            try {
                Thread.sleep( 1000 );
                end=getCodeFlag(i,ch);
            } catch (InterruptedException e) {
                logger.error( "Thread.sleep异常:"+e.toString() );
                return 0;
            }
        }
        return end;
    }
    public static int sendEndFor0(int i){
        int end=Fax.INSTANCE.SsmGetChState(i);
        if(end!=0){
            try {
                Thread.sleep( 1000 );
                end=sendEndFor0(i);
            } catch (InterruptedException e) {
                logger.error( "Thread.sleep异常:"+e.toString() );
                end=0;
            }
        }
        return end;
    }
    public static void stopAndHungUp(int ch, int i){
        String errMsg="";
        int isStop=Fax.INSTANCE.SsmStopTalkWith(ch,i);
        if(isStop==0){
            int hangup=Fax.INSTANCE.SsmHangup(ch);
            if(hangup!=0){
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("挂机失败:"+errMsg);
            }
            int stop=Fax.INSTANCE.SsmFaxStop(i);
            if(stop!=0){
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("终止发送失败:"+errMsg);
            }
        }else{
            errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
            logger.error("断开连接失败:"+errMsg);
        }
    }
    public static void insertMsg(int ch, String callerId, String tifPath,String barCode){
        String receiveNumber=decide.deviceDao.selectNumberByCh( ch );
        //根据callerId查询通讯簿有没有相同号码的法院名称
        String courtName=decide.mailMapper.selectCourtName(callerId);
        Inbox inbox=new Inbox();
        if(null != courtName){
            inbox.setSenderunit( courtName );
        }else{
            inbox.setSenderunit( callerId );
        }
        inbox.setSendnumber(callerId);
        inbox.setReceivenumber(receiveNumber);
        Date date=new Date();
        inbox.setCreate_time( date );
        inbox.setFilsavepath(tifPath);
        if("".equals( barCode )){
            inbox.setIsreceipt( 2 );
        }else{
            inbox.setBarCode( barCode );
        }
        decide.inboxMapper.insertInbox( inbox );
        inboxModels=new WebModel();
        inboxModels.setTime( date );
        inboxModels.setMsg( "[收件箱]收到一个新消息" );
        inboxCount=1;
    }
    public static void insertMsgReceipt(int ch, String callerId, String tifPathBack){
        //根据callerId查询通讯簿有没有相同号码的法院名称
        String receiveNumber=decide.deviceDao.selectNumberByCh( ch );
        String courtName=decide.mailMapper.selectCourtName(callerId);
        Return_Receipt returnReceipt=new Return_Receipt();
        if(null != courtName){
            returnReceipt.setSenderunit( courtName );
        }else{
            returnReceipt.setSenderunit( callerId );
        }
        returnReceipt.setSendnumber(callerId);
        returnReceipt.setReceivenumber(receiveNumber);
        Date date=new Date();
        returnReceipt.setCreate_time( date );
        returnReceipt.setFilsavepath(tifPathBack);
        decide.returnReceiptMapper.insertReceipt( returnReceipt );
        webModels=new WebModel();
        webModels.setTime( date );
        webModels.setMsg( "[收回执箱]收到一个新消息" );
        webModelCount=1;
    }
    public static String scanJpg(String tifPath){
        //进行颜色反转,再扫描,有条形码就是回执
        String[] datas = new String[0];
        try {
            datas = BarcodeScanner.scan( tifPath, BarCodeType.Code_128);
        } catch (Exception e) {
            logger.error( "条形码异常:"+e.toString() );
        }
        String str=datas[0];
        if(null==str){
            return "";
        } else{
            return str;
        }

    }
    public  static int getTiffPages(String tiffFilePath){
        SeekableStream seekableStream =null;
        int numPages=0;
        try {
            seekableStream = new FileSeekableStream(new File(tiffFilePath));
            ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", seekableStream, null);
            numPages = decoder.getNumPages();
        }catch(Exception ex){
            logger.error( ex.toString() );
        }finally{
            if(seekableStream!=null){
                try {
                    seekableStream.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
        }
        return numPages;
    }
    public static String getFileType(List<String> pathList){
        String jpgPath=pathList.get(pathList.size()-1);//最后一页jpg
        String barCode=scanJpg(jpgPath);
        if("".equals( barCode )){
            //说明没有条形码,就是正文
            return "";
        }else{
            //有条形码,回执页加正文
            return barCode;
        }
    }
}
