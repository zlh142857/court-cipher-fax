package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/11 11:01
 *@功能:
 */

import com.hx.dao.DeviceDao;
import com.hx.dao.InboxMapper;
import com.hx.dao.ProgramSettingDao;
import com.hx.dao.ReceiptMapper;
import com.hx.modle.Inbox;
import com.hx.modle.Program_Setting;
import com.hx.modle.Return_Receipt;
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

import static com.hx.util.ColorReverse.writeJpg;
import static com.hx.util.ColorReverse.writeJpgOne;
import static com.hx.util.TempDir.tifDir;
import static com.hx.util.TiffToJPEG.readerTiff;

//根据switch case判断当前状态码
@Component
public class Decide {
    private static Logger logger=Logger.getLogger(Decide.class);
    private static ProgramSettingDao programSettingDao;
    private static DeviceDao deviceDao;
    private static InboxMapper inboxMapper;
    private static ReceiptMapper receiptMapper;
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
    public  void setReceiptMapper(ReceiptMapper receiptMapper) {
        this.receiptMapper = receiptMapper;
    }
    @PostConstruct
    public void init() {
        decide=this;
        decide.programSettingDao=this.programSettingDao;
        decide.inboxMapper=this.inboxMapper;
        decide.deviceDao=this.deviceDao;
        decide.receiptMapper=this.receiptMapper;
    }
    public static void decideCh(int flag, int ch) throws Exception {
        if(flag==2){
            Map<String,String> map=chState_2(ch);
            if(map.get( "message" ).equals( "成功" )){
                //先存入数据库再打印,先获取tiff文件页数,==1,并扫描有条形码,说明是回执页
                int tifLength=map.get( "tifPath" ).length();
                String tifPath=map.get( "tifPath" );
                int pages=0;
                if(tifLength>0){
                    pages=getTiffPages(tifPath);
                    List<String> pathList=readerTiff(tifPath);
                    if(pages==1){
                        //扫描
                        boolean scanResult=getFileType(pathList);
                        if(scanResult){
                            //只有回执页
                            insertMsgReceipt(ch,map.get( "callerId" ), tifPath);
                        }else{
                            //只有正文
                            insertMsg(ch,map.get( "callerId" ), tifPath);
                        }
                    }else{
                        insertMsg(ch,map.get( "callerId" ), tifPath);
                    }
                    //查询打印服务名称
                    Program_Setting programSetting=decide.programSettingDao.selectProgramSetting();
                    String printService=programSetting.getPrintService();
                    if(programSetting.getIsPrint()==0){
                        try {
                            //进行颜色反转
                            List<File> newList=writeJpg(pathList);
                            PrintImage.printImageWhenReceive(newList,printService);
                        } catch (Exception e) {
                            logger.error( e.toString() );
                        }

                    }
                }

            }

        }
    }
    public static Map<String,String> chState_2(int ch) throws Exception {
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
                        int state= getCodeFlag(i);
                        if (state==55) {
                            //说明已经在接收中
                            int end=sendEndFor0(i);
                            if(end==0){
                                //目前接收到了一份文件,tifPath不为空,已经接收结束,断开连接
                                map.put( "tifPath",tifPath );
                                map.put( "callerId",callerId );
                                logger.info("接收成功");
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
        stopAndHungUp(ch,i);
        return map;
    }
    public static int getCodeFlag(int i){
        int end=Fax.INSTANCE.SsmGetChState(i);
        if(end==55){
            return end;
        }else if(end ==0){
            return end;
        }else{
            try {
                Thread.sleep( 1000 );
                end=getCodeFlag(i);
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
    public static void insertMsg(int ch, String callerId, String tifPath){
        String receiveNumber=decide.deviceDao.selectNumberByCh( ch );
        Inbox inbox=new Inbox();
        inbox.setSendernumber(callerId);
        inbox.setReceivenumber(receiveNumber);
        Date date=new Date();
        inbox.setCreate_time( date );
        inbox.setFilsavepath(tifPath);
        decide.inboxMapper.insertInbox( inbox );
    }
    public static void insertMsgReceipt(int ch, String callerId, String tifPathBack){
        String receiveNumber=decide.deviceDao.selectNumberByCh( ch );
        Return_Receipt returnReceipt=new Return_Receipt();
        returnReceipt.setSendnumber(callerId);
        returnReceipt.setReceivenumber(receiveNumber);
        Date date=new Date();
        returnReceipt.setCreate_time( date );
        returnReceipt.setFilsavepath(tifPathBack);
        decide.receiptMapper.insertReceipt( returnReceipt );
    }
    public static boolean scanJpg(String tifPath) throws Exception {
        //进行颜色反转,再扫描,有条形码就是回执
        String filePath=writeJpgOne(tifPath);
        String[] datas = BarcodeScanner.scan( filePath, BarCodeType.Code_128);
        String str=datas[0];
        if(str.contains( "N" )){
            return false;
        } else{
            return true;
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
                    e.printStackTrace();
                }
            }
        }
        return numPages;
    }
    public static boolean getFileType(List<String> pathList)throws Exception{
        String jpgPath=pathList.get(pathList.size()-1);
        boolean scanOk=scanJpg(jpgPath);
        if(scanOk){
            //有条形码,回执页加正文
            return true;
        }else{
            //说明没有条形码,就是正文
            return false;
        }
    }
}
