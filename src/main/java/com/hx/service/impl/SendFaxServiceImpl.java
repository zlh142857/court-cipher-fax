package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/23 9:52
 *@功能:
 */

import com.hx.common.Fax;
import com.hx.dao.*;
import com.hx.modle.*;
import com.hx.service.SendFaxService;
import com.hx.util.GetTimeToFileName;
import com.spire.barcode.BarCodeGenerator;
import com.spire.barcode.BarCodeType;
import com.spire.barcode.BarcodeScanner;
import com.spire.barcode.BarcodeSettings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.hx.change.ChangeFile.*;
import static com.hx.change.ImgToPdf.imgToPdf;
import static com.hx.common.ChStateCode.getStateMsgBy7;
import static com.hx.common.ChStateCode.getStateMsgByFaxCh;
import static com.hx.common.ChStateCode.getStateMsgByNot7;
import static com.hx.common.Decide.getTiffPages;
import static com.hx.common.Speed.getSpeed;
import static com.hx.controller.AlertController.*;
import static com.hx.util.TempDir.fileTemp;
import static com.hx.util.TempDir.schTask;
import static com.hx.util.TiffToJPEG.readerTiffOne;


@Service("sendFaxService")
public class SendFaxServiceImpl implements SendFaxService {
    private static Logger logger=Logger.getLogger( SendFaxServiceImpl.class);
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private ProgramSettingDao programSettingDao;
    @Autowired
    private MailMapper mailMapper;
    @Autowired
    private OutboxMapper outboxMapper;
    @Autowired
    private SendReceiptMapper SendReceiptMapper;
    @Autowired
    private InboxMapper inboxMapper;
    @Autowired
    private SchMapper schMapper;
    @Autowired
    private SpeedDao speedDao;
    public static String faxResult="";
    public static int faxCh=0;
    //查询座机号下拉列表框
    @Override
    public List<Device_Setting> selectSeatNumber() {
        List<Device_Setting> numbers=deviceDao.selectSeatNumber();
        return numbers;
    }
    //初始化板卡时查询通道类型
    @Override
    public int selectChType(int ch) {
        int chType=deviceDao.selectChType(ch);
        return chType;
    }

    @Override
    public String baseToTif(String base64) {
        String tifPath="";
        ImageOutputStream os=null;
        InputStream is=null;
        try {
            tifPath=schTask();
            String pdfPath=baseToPdf(base64);
            File file=new File( pdfPath );
            is=new FileInputStream( file );
            os=new FileImageOutputStream( new File( tifPath ) );
            boolean flag=PdfToTiff(is,os);
            if(!flag){
                tifPath=null;
            }
            return tifPath;
        } catch (Exception e){
            logger.error( e.toString() );
            return null;
        }finally {
            try {
                if(os!=null){
                    os.close();
                }
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                logger.error( e.toString() );
            }
        }

    }

    @Override
    public String createBarCode() throws IOException {
        String uuid=GetTimeToFileName.GetTimeToFileNameToBar();
        BarcodeSettings settings = new BarcodeSettings();//创建BarcodeSettings实例
        settings.setType(BarCodeType.Code_128);//指定条码类型
        settings.setData(uuid);//设置条码数据
        settings.setData2D(uuid);//设置条码显示数据
        settings.setShowTextOnBottom(true);//设置数据文本显示在条码底部
        settings.setX(1.1f);//设置黑白条宽度
        settings.setImageHeight(80);//设置生成的条码图片高度
        settings.setImageWidth(100);//设置生成的条码图片宽度
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
                          String sendNumber, String isBack,String filename,String id,String ch)throws Exception{
        int newCh=0;
        if(null != ch){
            if(!"".equals( ch )){
                newCh=Integer.parseInt( ch );
            }
        }
        int back=0;
        if(null != isBack){
            if(!"".equals( isBack )){
                back=Integer.parseInt( isBack );
            }
        }
        String message="失败";
        if(null != sendNumber){
            receiveNumber=getReceiveNumber(newCh,sendNumber,receiveNumber);
            //通过指定号码发送,然后查询通道是否空闲
            int isFree=Fax.INSTANCE.SsmGetChState( newCh);
            if(isFree==7){
                Fax.INSTANCE.SsmHangup( newCh);
                isFree=Fax.INSTANCE.SsmGetChState(newCh);
            }
            if(isFree==0){
                message=getMessage( receiveNumber, newCh, tifPath, base64, back, filename, sendNumber, courtName,id );
            }else{
                message="线路繁忙,请稍后发送";
                logger.error( "模拟中继线通道不为空闲状态state:"+isFree );
            }
        }else{
            int count=0;
            int isFree=0;
            //随机选择一个号码发送,查询空闲通道,然后查询该通道是否支持发送
            for(int i=0;i<4;i++){
                isFree=Fax.INSTANCE.SsmGetChState(i);
                if(isFree==7){
                    Fax.INSTANCE.SsmHangup(i);
                    isFree=Fax.INSTANCE.SsmGetChState(i);
                }
                if(isFree==0){
                    int vol=Fax.INSTANCE.SsmGetLineVoltage(i);
                    if(vol>5){
                        receiveNumber=getReceiveNumber(i,sendNumber,receiveNumber);
                        count++;
                        sendNumber=deviceDao.selectSeatNumberByCh(i);
                        message=getMessage( receiveNumber,i, tifPath, base64,back, filename, sendNumber, courtName,id);
                        break;
                    }
                }
            }
            if(count==0){
                message="线路繁忙,请稍后发送";
                logger.error( "模拟中继线通道不为空闲状态state:"+isFree );
            }
        }
        return message;
    }

    public String getMessage(String receiveNumber,int ch,String tifPath,String base64,int isBack,String filename,String sendNumber,String courtName,String id)throws Exception{
        String message="";
        //进行发送
        String Msg=sendFreeCh(receiveNumber,ch);
        if(Msg.equals( "通话中" )){
            message=faxSendStart(ch,tifPath,base64,isBack);
            if(isBack==2){
                //只有回执页,新增进发回执箱,修改已回执状态
                if(null != id){
                    if(!"".equals( id )){
                        int newid=Integer.valueOf( id );
                        inboxMapper.updateIsReceiptById(newid);
                    }
                }
                try{
                    String[] datas = BarcodeScanner.scan( tifPath, BarCodeType.Code_128);
                    String str=datas[0];
                    //扫描一次条形码,有条形码的话,就找到相应条形码的收件箱数据,更改已回执
                    if(null!=str){
                        inboxMapper.updateIsReceiptByBarCode( str );
                    }
                    insertDataReceipt( message,receiveNumber,filename,sendNumber,courtName,str );
                }catch (Exception e){
                    logger.error( e );
                }
            }else{
                int pages=getTiffPages(tifPath);
                insertDataOutBox( message,receiveNumber,filename,sendNumber,courtName,tifPath,pages );
            }
        }else{
            message=Msg;
            if(isBack==2){
                insertDataReceipt( message,receiveNumber,filename,sendNumber,courtName,null);
            }else{
                int pages=getTiffPages(tifPath);
                insertDataOutBox( message,receiveNumber,filename,sendNumber,courtName,tifPath,pages );
            }
        }
        return message;
    }
    //对接收方的号码进行更改
    public String getReceiveNumber(int ch,String sendNumber,String receiveNumber){
        Device_Setting deviceSetting=null;
        //查看有没有前缀
        if(null == sendNumber || "".equals( sendNumber )){
            deviceSetting=deviceDao.selectPrefixByCh(ch);
        }else{
            deviceSetting=deviceDao.selectPrefix(sendNumber);
        }
        int prefixLength=deviceSetting.getPrefix().length();//前缀长度
        if(prefixLength!=0){
            String code=deviceSetting.getAreaCode();//区号
            int codeLength=code.length();
            String rec=receiveNumber.substring( 0,codeLength );
            if(code.equals( rec )){
                //区号相同
                receiveNumber=deviceSetting.getPrefix()+receiveNumber.substring( codeLength, receiveNumber.length());
            }else{
                receiveNumber=deviceSetting.getPrefix()+receiveNumber;
            }
        }else{
            String code = deviceSetting.getAreaCode();
            int codeLength = code.length();
            String codeR = receiveNumber.substring( 0, codeLength );
            if (codeR.equals( code )) {
                receiveNumber = receiveNumber.substring( codeLength, receiveNumber.length() );
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
            Msg=getStateMsgBy7(pend);
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
                Device_Setting device_setting=deviceDao.selectSpeedIdByCh(ch);
                int speedId=device_setting.getSendSpeedId();
                int speed=speedDao.selectSpeedById(speedId);
                Fax.INSTANCE.SsmFaxSetChSpeed(j,speed);
                int sendFlag=-1;
                if(isBack==0){
                    //两份文件
                    String szFile=getfileList(tifPath,base64);
                    sendFlag=Fax.INSTANCE.SsmFaxSendMultiFile(j,"D:\\\\schTask\\\\",szFile);
                }else if(isBack==1){
                    //仅正文
                    sendFlag=Fax.INSTANCE.SsmFaxStartSend(j,tifPath);
                }else{
                    //回执页
                    if(null == tifPath || "".equals( tifPath )){
                        errMsg="获取回执文件失败";
                        faxResult="发送失败";
                        faxCh=ch;
                    }else{
                        sendFlag=Fax.INSTANCE.SsmFaxStartSend(j,tifPath);
                    }
                }
                if(sendFlag==0){
                    //等待通道为空闲状态
                    sendEndFor0(j);
                    //空闲后挂起
                    stopAndHungUp(ch,j);
                    logger.info("发送成功");
                }else{
                    String e=Fax.INSTANCE.SsmGetLastErrMsgA();
                    logger.error( "发送失败:"+e );
                    errMsg="发送失败";
                    faxResult="发送失败";
                    faxCh=ch;
                }
            }else{
                String e=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error( "建立连接失败:"+e );
                errMsg="建立连接失败";
                faxResult="发送失败";
                faxCh=ch;
            }
        }else{
            errMsg="没有空闲通道";
            faxResult="发送失败";
            faxCh=ch;
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
            if(hangup!=0){
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("挂机失败:"+errMsg);
            }
            int stop=Fax.INSTANCE.SsmFaxStop(j);
            if(stop!=0){
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("终止发送失败:"+errMsg);
            }
        }else{
            errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
            logger.error("断开连接失败:"+errMsg);
        }
    }
    public void insertDataOutBox(String message,String receiveNumber,String filename,String sendNumber,String courtName,String tifPath,int pages){
        Outbox outbox=new Outbox();
        outbox.setReceivenumber( receiveNumber );
        if("成功".equals( message )){
            outbox.setIsSuccess( true );
            outbox.setMessage( "成功" );
        }else{
            outbox.setMessage( "失败("+message+")" );
            outbox.setIsSuccess( false );
        }
        outbox.setSendline( filename );
        outbox.setPageNum( pages );
        outbox.setFilsavepath( tifPath );
        outbox.setSendnumber( sendNumber );
        Date date=new Date();
        outbox.setCreate_time(date);
        outbox.setReceivingunit( courtName );
        outboxMapper.insertNewMessage(outbox);
        outboxModels=new WebModel();
        outboxModels.setTime( date );
        outboxModels.setMsg( "[发件箱]收到一个新消息" );
        outboxCount=1;
    }
    public void insertDataReceipt(String message,String receiveNumber,String filename,String sendNumber,String courtName,String barCode){
        Send_Receipt sendReceipt=new Send_Receipt();
        sendReceipt.setReceivenumber( receiveNumber );
        if(message.equals( "成功" )){
            sendReceipt.setMessage( "成功" );
        }else{
            sendReceipt.setMessage( "失败("+message+")" );
        }
        sendReceipt.setBarCode( barCode );
        sendReceipt.setSendline( filename );
        sendReceipt.setSendnumber( sendNumber );
        Date date=new Date();
        sendReceipt.setCreate_time(date);
        sendReceipt.setReceivingunit( courtName );
        SendReceiptMapper.insertNewMessage(sendReceipt);
        sendModels=new WebModel();
        sendModels.setTime( date );
        sendModels.setMsg( "[发回执箱]收到一个新消息" );
        sendCount=1;
    }
    public static String getfileList(String tifPath,String base64){
        String [] main=tifPath.split("/");
        String mainText=main[main.length-1];
        String [] receipt=base64.split("/");
        String receiptText=receipt[receipt.length-1];
        String fileList=mainText+";"+receiptText;
        return fileList;
    }

    @Override
    public List<ChMsg> rateOfAdvance(String ch) throws Exception {
        List<ChMsg> list=new ArrayList<>(  );
        if(ch != null || ch != ""){
            int chCode= Integer.parseInt( ch );
            int i=8;
            if(chCode==0){
                i=8;
            }else if(chCode==1){
                i=9;
            }else if(chCode==2){
                i=10;
            }else{
                i=11;
            }
            ChMsg chMsg=getchList(chCode,i);
            list.add( chMsg );
        }else{
            logger.error( "ch为空" );
        }
        return list;
    }

    public ChMsg getchList(int ch,int i)throws Exception{
        ChMsg chMsg=new ChMsg();
        //查询每一条通道的状态
        //查询0编号通道,查询通道状态,
        int state=Fax.INSTANCE.SsmGetChState(ch);
        String selfNumber=deviceDao.selectNumberByCh(ch);
        chMsg.setSelfNumber(selfNumber);
        chMsg.setCh( ch );
        if(state==0){
            chMsg.setFlag( true );
        }else{
            chMsg.setFlag( false );
            if(state==7){
                int code=Fax.INSTANCE.SsmGetPendingReason(ch);
                chMsg.setMessage(getStateMsgBy7(code));
            }else if(state==3){
                String callerId=Fax.INSTANCE.SsmGetCallerIdA( ch );
                if(null !=callerId){
                    chMsg.setSendNumber(callerId);
                }
                int faxCh=Fax.INSTANCE.SsmGetChState(i);
                chMsg.setMessage(getStateMsgByFaxCh(faxCh));
                if(faxCh==55){
                    int allByte=Fax.INSTANCE.SsmFaxGetAllBytes(i);
                    if(allByte>0){
                        int done=Fax.INSTANCE.SsmFaxGetSendBytes(i);
                        if(done>=0){
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            numberFormat.setMaximumFractionDigits(0);
                            String result = numberFormat.format((float)done/(float)allByte*100);
                            float num= Float.parseFloat( result );
                            chMsg.setPercentage( num );
                        }
                    }else{
                        int bytes=Fax.INSTANCE.SsmFaxGetRcvBytes(i);
                        chMsg.setBytes(String.valueOf(bytes));
                    }
                    chMsg.setSpeed(getSpeed(Fax.INSTANCE.SsmFaxGetSpeed(i)));
                    chMsg.setPages(Fax.INSTANCE.SsmFaxGetPages(i));;
                }
            }else{
                chMsg.setMessage(getStateMsgByNot7(state));;
            }
        }
        return chMsg;
    }
    @Override
    public List<Device_Setting> selectChAndSeatNumber() {
        List<Device_Setting> deviceSettings=deviceDao.selectDevice();
        return deviceSettings;
    }

    @Override
    public boolean insertSchTask(String tifPath, String receiptPath, List<TempModel> tempList, String sendNumber, String isBack, String filename, String id, String ch, String sendTime) throws ParseException, InterruptedException, IOException {
        boolean flag=false;
        int count=0;
        for(int i=0;i<tempList.size();i++){
            String tifPath2="";
            String receiptNew="";
            if(tifPath != null || tifPath != ""){
                tifPath2=schTask()+".tif";
                File tifFile=new File(tifPath);
                File tifNewPath = new File(tifPath2);
                tifFile.renameTo(tifNewPath);
            }
            Thread.sleep( 3 );
            if(receiptPath != null || receiptPath != ""){
                receiptNew=schTask()+".tif";
                File tifFile2=new File(receiptPath);
                File tifNewPath = new File(receiptNew);
                tifFile2.renameTo(tifNewPath);
            }
            Sch_Task schTask=new Sch_Task();
            schTask.setCh( ch );
            schTask.setCourtName( tempList.get( i ).getTypename() );
            Date date=new Date(  );
            schTask.setCreateTime( date );
            schTask.setFilename( filename );
            schTask.setIsBack( isBack );
            schTask.setOutboxId( id );
            schTask.setReceiptPath( receiptNew );
            schTask.setReceiveNumber( tempList.get( i ).getLinknumber() );
            schTask.setTifPath( tifPath2 );
            schTask.setSendNumber( sendNumber );
            DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date sendDate=format.parse( sendTime );
            schTask.setSendTime( sendDate );
            schMapper.insertSchTask(schTask);
            count++;
        }
        if(count==tempList.size()){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean deleteSchTask(int id) {
        schMapper.deleteSchTask(id);
        return true;
    }

    @Override
    public boolean updateTaskDate(String id, String date) throws ParseException {
        boolean flag=false;
        if(null != id){
            if(null != date){
                DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date sendDate=format.parse( date );
                schMapper.updateTaskDate(Integer.valueOf( id ),sendDate);
                flag=true;
            }
        }
        return flag;
    }

    @Override
    public Map<String,Object> selectTaskLimit(Integer pageNow, Integer pageSize) {
        Map<String,Object> map=new HashMap<>(  );
        int page=(pageNow-1)*pageSize;
        List<Sch_Task> list=schMapper.selectTaskLimit(page,pageSize);
        Long total=schMapper.selectCount();
        map.put( "list",list);
        map.put( "total",total );
        return map;
    }

    @Override
    public boolean undoSend(String ch) {
        int code=Integer.valueOf( ch );
        int i=8;
        if(code==0){
            i=8;
        }else if(code==1){
            i=9;
        }
        else if(code==2){
            i=10;
        }
        else if(code==3){
            i=11;
        }
        int stateFax=Fax.INSTANCE.SsmGetChState(i);
        if(stateFax!=0){
            Fax.INSTANCE.SsmFaxStop( i );
            Fax.INSTANCE.SsmStopTalkWith( code,i );
        }
        Fax.INSTANCE.SsmHangup( code );
        return true;
    }

    @Override
    public String returnFaxGetPath(String tifPath){
        InputStream is=null;
        ImageOutputStream os=null;
        String path=schTask();
        try{
            List<String> whiteJpg=new ArrayList<>(  );
            String jpgPath=readerTiffOne(tifPath);
            whiteJpg.add( jpgPath );
            String pdfPath=fileTemp()+".pdf";
            imgToPdf(whiteJpg,pdfPath);
            File file=new File(pdfPath);
            if(file.isFile()){
                os= new FileImageOutputStream(new File( path ));
                is=new FileInputStream( file );
                PdfToTiff(is,os);
            }else{
                path="";
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
        }
        return path;
    }

    @Override
    public Map<String,Object> bigToSmallTiff(String tifPath, int[] pages){
        Map<String,Object> map=new HashMap<>(  );
        File file=new File( tifPath );
        ImageOutputStream os=null;
        try {
            if(file.exists()){
                List<String> newPath=new ArrayList<>(  );
                List<String> path=makeSingleTif(file);
                for(int i=0;i<pages.length;i++){
                    int index=pages[i]-1;
                    newPath.add( path.get( index ) );
                }
                String newTifPath=schTask();
                File outFile=new File( newTifPath );
                os=new FileImageOutputStream(outFile);
                boolean flag=tiffMerge(newPath,os);
                if(flag){
                    map.put( "message","转换成功" );
                    map.put( "tifPath",newTifPath );
                }else{
                    map.put( "message","转换失败" );
                }
            }else{
                map.put( "message","文件不存在" );
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
        }
        return map;
    }

    @Override
    public Map<String,Object> checkChByHand() {
        Map<String,Object> map=new HashMap<>(  );
        List<Integer> chs=deviceDao.selectCh();
        int count=0;
        for(;;){
            for(int i=0;i<chs.size();i++){
                int ch=chs.get( i );
                int chFax=0;
                int chState=Fax.INSTANCE.SsmGetChState( chs.get( i ) );
                //当前处于通话中
                if(chState==3){
                    if(ch==0){
                        chFax=8;
                    }else if(ch==1){
                        chFax=9;
                    }else if(ch==2){
                        chFax=10;
                    }else{
                        chFax=11;
                    }
                    int chFaxState=Fax.INSTANCE.SsmGetChState( chFax );
                    if(chFaxState==0){
                        count=count++;
                        map.put( "ch", ch);
                        map.put( "flag", true);
                        break;
                    }
                }
            }
            try {
                Thread.sleep( 500 );
            } catch (InterruptedException e) {
                logger.error( e.toString() );
            }
            if(count>0){
                break;
            }
        }
        return map;
    }

    @Override
    public String sendFaxByHand(String tifPath, String receiptPath, String courtName, String receiveNumber, Integer isBack, Integer ch, String filename, Integer id) {
        String message=faxSendStart(ch,tifPath,receiptPath,isBack);
        //根据通道编号查询发送方的号码
        String sendNumber=deviceDao.selectNumberByCh(ch);
        if(isBack==2){
            //只有回执页,新增进发回执箱,修改已回执状态
            if(null != id){
                int newid=Integer.valueOf( id );
                inboxMapper.updateIsReceiptById(newid);
            }
            try{
                String[] datas = BarcodeScanner.scan( tifPath, BarCodeType.Code_128);
                String str=datas[0];
                //扫描一次条形码,有条形码的话,就找到相应条形码的收件箱数据,更改已回执
                if(null!=str){
                    inboxMapper.updateIsReceiptByBarCode( str );
                }
                insertDataReceipt( message,receiveNumber,filename,sendNumber,courtName,str );
            }catch (Exception e){
                logger.error( e );
            }
        }else{
            int pages=getTiffPages(tifPath);
            insertDataOutBox( message,receiveNumber,filename,sendNumber,courtName,tifPath,pages );
        }
        return message;
    }

    @Override
    public String selectNotifyPhone(String receiveNumber) {
        String telNotify=mailMapper.selectNotifyPhone(receiveNumber);
        return telNotify;
    }

    @Override
    public String telNotify(Integer id,String telNotify) throws InterruptedException {
        int count=0;
        int isFree=0;
        String message="";
        //随机选择一个号码发送,查询空闲通道,然后查询该通道是否支持发送
        for(int i=0;i<4;i++){
            isFree=Fax.INSTANCE.SsmGetChState(i);
            if(isFree==7){
                Fax.INSTANCE.SsmHangup(i);
                isFree=Fax.INSTANCE.SsmGetChState(i);
            }
            if(isFree==0){
                int vol=Fax.INSTANCE.SsmGetLineVoltage(i);
                if(vol>5){
                    count++;
                    telNotify=getReceiveNumber(i,null,telNotify);
                    message=sendVoice(i,telNotify);
                    //修改收件箱的电话通知结果
                    outboxMapper.updateTelNotifyResultById(id,message);
                    break;
                }
            }
        }
        if(count==0){
            message="线路繁忙,请稍后发送";
            logger.error( "模拟中继线通道不为空闲状态state:"+isFree );
        }
        return message;
    }
    public String sendVoice(int ch,String telNotify) throws InterruptedException {
        String message="";
        int flag1=Fax.INSTANCE.SsmPickup(ch);
        if(flag1==0){
            int flag2=Fax.INSTANCE.SsmAutoDial(ch,telNotify);
            if(flag2==0){
                int state=0;
                //等待通话状态
                for(;;){
                    state=Fax.INSTANCE.SsmGetChState(ch);
                    if(state==3){
                        String voicePath=programSettingDao.selectTelVoicePath();
                        int flag3=Fax.INSTANCE.SsmPlayFile(0,voicePath,-2,0,500000);
                        if(flag3==0){
                            //通话成功,等待对端挂机
                            message="电话通知成功";
                            for(;;){
                                state=Fax.INSTANCE.SsmGetChState(ch);
                                if(state==7){
                                    Fax.INSTANCE.SsmStopPlayFile( ch );
                                    Fax.INSTANCE.SsmHangup(ch);
                                    break;
                                }
                            }
                        }else{
                            message="播放电话通知音频失败";
                            String errorMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                            logger.error("播放音频失败原因:"+errorMsg);
                        }
                        break;
                    }else if(state==7){
                        message=getStateMsgBy7(state);
                        break;
                    }
                    Thread.sleep( 500 );
                }
            }else{
                message="去话呼叫失败";
                String errorMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("去话呼叫失败原因:"+errorMsg);
            }
        }else{
            message="摘机失败";
            logger.error( "摘机失败" );
        }
        return message;
    }

}
