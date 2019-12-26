package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/6 17:20
 *@功能:
 */

import com.hx.dao.*;
import com.hx.modle.Program_Setting;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.hx.BackUp.BackUpExcel.*;
import static com.hx.BackUp.TextCopyFileAndMove.fileMove;
import static com.hx.BackUp.UpdateData.*;
import static com.hx.common.StaticFinal.*;


@Component
public class DayTask {
    private static Logger logger=Logger.getLogger( DayTask.class );
    @Autowired
    private ProgramSettingDao programSettingDao;
    @Autowired
    private InboxMapper inboxMapper;
    @Autowired
    private OutboxMapper outboxMapper;
    @Autowired
    private ReturnReceiptMapper returnReceiptMapper;
    @Autowired
    private SendReceiptMapper sendReceiptMapper;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private BackUpNoteDao backUpNoteDao;
    @Scheduled(cron="0 0 12 * * ?")
    public void myTest(){
        //先执行日志记录的删除操作
        //查询程序设置的相关设置
        Program_Setting program_setting=programSettingDao.selectProgramSetting();
        int fileDays=program_setting.getDelFileDays();
        int logDays=program_setting.getDelLogDays();
        //日志删除
        if(logDays>0){
            logMapper.deleteExpire(logDays);
        }
        //先大箱后小箱,发件箱->收件箱->发回执箱->收回执箱
        //统一更改文件保存记录->统一转移文件->统一删除表数据
        //先更改收据库文件保存路径
        if(fileDays>0){
            //查询距离上次备份是否已经到了这个间隔
            int days=backUpNoteDao.selectDaysByTime();
            if(fileDays==days){
                //开始更改文件保存记录
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
                        logger.error( e.toString() );
                    }
                }
                boolean flag5=fileMove(TiffDir,aim2);
                if(!flag5){
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
                        logger.error( e.toString() );
                    }
                }
                backUpNoteDao.insertDate( ymd );
            }
        }

    }

}
