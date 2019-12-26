package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/26 11:33
 *@功能:
 */

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.hx.BackUp.ExcelInboxListener;
import com.hx.BackUp.ExcelOutboxListener;
import com.hx.BackUp.ExcelReturnReceiptListener;
import com.hx.BackUp.ExcelSendReceiptListener;
import com.hx.dao.*;
import com.hx.modle.*;
import com.hx.service.BackUpService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hx.BackUp.BackUpExcel.*;
import static com.hx.BackUp.TextCopyFileAndMove.fileMove;
import static com.hx.BackUp.UpdateData.*;
import static com.hx.common.StaticFinal.*;

@Service("backUpService")
public class BackUpServiceImpl implements BackUpService {
    private static Logger logger=Logger.getLogger( BackUpServiceImpl.class );
    @Autowired
    private InboxMapper inboxMapper;
    @Autowired
    private OutboxMapper outboxMapper;
    @Autowired
    private ReturnReceiptMapper returnReceiptMapper;
    @Autowired
    private SendReceiptMapper sendReceiptMapper;
    @Autowired
    private BackUpNoteDao backUpNoteDao;
    @Override
    public Map<String, Object> selectBackUpFilePath(Integer flag) throws Exception{
        Map<String,Object> map=new HashMap<>(  );
        String path =null;
        if(flag==1){
            //收件箱
            path = BACKUPGETFAX;
        }else if(flag==2){
            //发件箱
            path = BACKUPSENDFAX;
        }else if(flag==3){
            //收回执箱
            path = BACKIPGETRECEIPT;
        }else{
            //发回执箱
            path = BACKIPSENDRECEIPT;
        }
        List<TempModel> list = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if(null == files){
            map.put( "message","备份文件夹不存在或无备份文件" );
            map.put( "flag",false );
        }else{
            for (File img:files) {
                TempModel tempModel=new TempModel();
                tempModel.setFileName( img.getName() );
                tempModel.setFilePath( path+"/"+img.getName() );
                list.add( tempModel );
            }
            map.put( "list",list );
            map.put( "message","成功" );
            map.put( "flag",true );
        }
        return map;
    }

    @Override
    public Map<String, Object> recoverBackUp(String filePath,Integer flag) throws Exception{
        Map<String,Object> map=new HashMap<>(  );
        if(flag==1){
            //收件箱
            Sheet sheet = new Sheet(1,1,Inbox.class);
            EasyExcelFactory.readBySax(new FileInputStream(filePath),sheet,new ExcelInboxListener());
        }else if(flag==2){
            //发件箱
            Sheet sheet = new Sheet(1,1,Outbox.class);
            EasyExcelFactory.readBySax(new FileInputStream(filePath),sheet,new ExcelOutboxListener());
        }else if(flag==3){
            //收回执箱
            Sheet sheet = new Sheet(1,1,Return_Receipt.class);
            EasyExcelFactory.readBySax(new FileInputStream(filePath),sheet,new ExcelReturnReceiptListener());
        }else{
            //发回执箱
            Sheet sheet = new Sheet(1,1,Send_Receipt.class);
            EasyExcelFactory.readBySax(new FileInputStream(filePath),sheet,new ExcelSendReceiptListener());
        }
        map.put( "message","成功" );
        map.put( "flag",true );
        return map;
    }

    @Override
    public Map<String, Object> startBackUp() throws Exception{
        Map<String,Object> map=new HashMap<>(  );
        Date ymd=new Date(  );
        SimpleDateFormat sdf=new SimpleDateFormat( "yyyy-MM-dd" );
        String dateName=sdf.format( ymd );
        boolean flag0=updateDataFileSavePathOutbox(dateName);
        boolean flag1=updateDataFileSavePathInbox(dateName);
        boolean flag2=updateDataFileSavePathSendReceipt(dateName);
        boolean flag3=updateDataFileSavePathReturnReceipt(dateName);
        //开始转移文件夹
        String aim=BACKUPSENDFAXFILE+"/"+dateName;//发送前文件夹
        String aim2=BACKUPGETFAXFILE+"/"+dateName;//接收文件夹
        boolean flag4=fileMove(SCHTASK,aim);
        if(!flag4){
            map.put( "message5","发送前文件夹备份出错/源文件夹不存在/源文件夹无文件" );
            logger.error( "发送前文件夹备份出错/源文件夹不存在/源文件夹无文件" );
        }else{
            //开始导出为Excel
            File file2=new File( BACKUPSENDFAX );
            if(!file2.exists()){
                file2.mkdir();
            }
            String outPath0=BACKUPSENDFAX+"/"+dateName+".xlsx";
            try {
                writeExcelOutbox(outPath0);
                //开始清除原数据
                outboxMapper.deleteAll();
            } catch (IOException e) {
                map.put( "message2","发件箱备份失败" );
                logger.error( e.toString() );
            }
            File file3=new File( BACKIPSENDRECEIPT );
            if(!file3.exists()){
                file3.mkdir();
            }
            //3
            String outPath2=BACKIPSENDRECEIPT+"/"+dateName+".xlsx";
            try {
                writeExcelSendReceipt(outPath2);
                sendReceiptMapper.deleteAll();
            } catch (IOException e) {
                map.put( "message4","发回执箱备份失败" );
                logger.error( e.toString() );
            }
        }
        boolean flag5=fileMove(TiffDir,aim2);
        if(!flag5){
            map.put( "message6","接收文件夹备份出错/源文件夹不存在/源文件夹无文件" );
            logger.error( "接收文件夹备份出错/源文件夹不存在/源文件夹无文件" );
        }else{
            //2
            File file=new File( BACKUPGETFAX );
            if(!file.exists()){
                file.mkdir();
            }
            String outPath1=BACKUPGETFAX+"/"+dateName+".xlsx";
            try {
                writeExcelInbox(outPath1);
                inboxMapper.deleteAll();
            } catch (IOException e) {
                map.put( "message1","收件箱备份失败" );
                logger.error( e.toString() );
            }
            File file2=new File( BACKIPGETRECEIPT );
            if(!file2.exists()){
                file2.mkdir();
            }
            //4
            String outPath3=BACKIPGETRECEIPT+"/"+dateName+".xlsx";
            try {
                writeExcelReturnReceipt(outPath3);
                returnReceiptMapper.deleteAll();
            } catch (IOException e) {
                map.put( "message3","收回执箱备份失败" );
                logger.error( e.toString() );
            }
        }
        if(flag4){
            if(flag5){
                backUpNoteDao.insertDate( ymd );
                map.put( "message","备份成功" );
                map.put( "flag",true );
            }else{
                map.put( "flag",false );
            }
        }else{
            map.put( "flag",false );
        }
        return map;
    }
}
