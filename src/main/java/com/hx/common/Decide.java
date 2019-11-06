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
import com.hx.util.TiffToJPEG;
import com.spire.barcode.BarCodeType;
import com.spire.barcode.BarcodeScanner;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.hx.util.ColorReverse.writeJpg;
import static com.hx.util.ColorReverse.writeJpgOne;
import static com.hx.util.TempDir.tifDir;

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
                Program_Setting programSetting=decide.programSettingDao.selectProgramSetting();
                String printService=programSetting.getPrintService();
                if(programSetting.getIsPrint()==0){
                    int tifLength=map.get( "tifPath" ).length();
                    int tifLengthBack=map.get( "tifPathBack" ).length();
                    if(tifLength>0){
                        Map<String,Object> fileMap=TiffToJPEG.readerTiff(map.get( "tifPath" ));
                        try {
                            List<String> pathList=(List<String>)fileMap.get("pathList");
                            List<File> newList=writeJpg(pathList);
                            PrintImage.printImageWhenReceive(newList,printService);
                            deleteFileList((List<File>)fileMap.get("fileList"),newList);
                        } catch (Exception e) {
                            logger.error( e.toString() );
                        }
                    }
                    if(tifLengthBack>0){
                        Map<String,Object> fileMap=TiffToJPEG.readerTiff(map.get( "tifPathBack" ));
                        try {
                            //将文件先转换颜色放进list集合再打印
                            List<String> pathList=(List<String>)fileMap.get("pathList");
                            List<File> newList=writeJpg(pathList);
                            PrintImage.printImageWhenReceive(newList,printService);
                            deleteFileList((List<File>)fileMap.get("fileList"),newList);
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
        String tifPathBack="";
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
                                //目前接收到了一份文件,tifPath不为空
                                map.put( "tifPath",tifPath );
                                //说明当前通道已经为空闲状态,查看是否有F1信号
                                int findVoc=0;
                                Thread.sleep( 3000 );
                                System.out.println("开始查找findVOC");
                                for(int k=0;k<3;k++){
                                    Thread.sleep( 1000 );
                                    findVoc=Fax.INSTANCE.SsmGetVocFxFlag( ch,1,true);
                                    if(findVoc==1){
                                        break;
                                    }else if(findVoc==-1){
                                        message=Fax.INSTANCE.SsmGetLastErrMsgA();
                                        logger.error( message );
                                    }
                                }
                                System.out.println("findVOC:"+findVoc);
                                if(findVoc==1){
                                    //说明有F1信号
                                    tifPathBack=tifDir();
                                    isOk=Fax.INSTANCE.SsmFaxStartReceive( i,tifPathBack);
                                    if(isOk==0){
                                        //等待空闲状态
                                        int flag=getCodeFlag(i);
                                        if(flag==55){
                                            //说明已经在接收中了
                                            end=sendEndFor0(i);
                                            if(end==0){
                                                map.put( "tifPathBack",tifPathBack );
                                                logger.info("接收成功");
                                                insertMsg(ch,callerId,tifPath,tifPathBack);
                                            }
                                        }else{
                                            logger.error( "接收失败" );
                                        }
                                    }else{
                                        message=Fax.INSTANCE.SsmGetLastErrMsgA();
                                        logger.error( message );
                                    }
                                }else{
                                    //如果接收到两份文件,tifPath和tifPathBack都不为空
                                    //只接收到一份文件,可能是正文,可能是回执,但是tifPath不为空
                                    //判断文件是否只有一页,然后判断文件是否有条形码
                                    System.out.println("获取pages");
                                    System.out.println("tifPath:"+tifPath);
                                    int pages=getTiffPages( tifPath );
                                    System.out.println("pages:"+pages);
                                    if(pages==-1){
                                        message=Fax.INSTANCE.SsmGetLastErrMsgA();
                                        logger.error( message );
                                    }else if(pages==1){
                                        boolean scan=scanJpg(tifPath);
                                        System.out.println("scan:"+scan);
                                        if(scan){
                                            //回执
                                            insertMsgReceipt(ch,callerId,tifPathBack);
                                        }else{
                                            insertMsg(ch,callerId,tifPath,tifPathBack);
                                        }
                                    }else{
                                        //页数大于1说明是正文
                                        insertMsg(ch,callerId,tifPath,tifPathBack);
                                    }
                                    logger.info("接收成功");
                                }
                            }
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
        }else{
            errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
            logger.error("断开连接失败:"+errMsg);
        }
    }
    public static void deleteFileList(List<File> list,List<File> newList){
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                list.get(i).delete();
            }
        }
        if(newList.size()>0){
            for(int i=0;i<newList.size();i++){
                newList.get(i).delete();
            }
        }
    }
    public static void insertMsg(int ch, String callerId, String tifPath, String tifPathBack){
        System.out.println("开始新增inbox");
        String receiveNumber=decide.deviceDao.selectNumberByCh( ch );
        Inbox inbox=new Inbox();
        inbox.setSendernumber(callerId);
        inbox.setReceivenumber(receiveNumber);
        Date date=new Date();
        inbox.setCreate_time( date );
        inbox.setFilsavepath(tifPath);
        System.out.println("inbox是啊比1");
        if(tifPathBack.length()>0){
            inbox.setReceiptpath( tifPathBack );
        }
        System.out.println("inbox是啊比2");
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
        //先转换成jpg,然后扫描jpg,返回值不为null,就是回执文件
        String filePath=writeJpgOne(tifPath);
        String[] datas = BarcodeScanner.scan( filePath, BarCodeType.Code_128);
        if(datas.equals( null )){
            return false;
        }else{
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
}
