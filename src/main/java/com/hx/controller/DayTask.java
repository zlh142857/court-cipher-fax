package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/6 17:20
 *@功能:
 */

import com.hx.dao.*;
import com.hx.modle.Outbox;
import com.hx.modle.Program_Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.hx.common.StaticFinal.SCHTASK;

@Component
public class DayTask {
    @Autowired
    private ProgramSettingDao programSettingDao;
    @Autowired
    private InboxMapper inboxMapper;
    @Autowired
    private OutboxMapper outboxMapper;
    @Autowired
    private ReturnReceiptMapper returnReceiptMapper;
    @Autowired
    private LogMapper logMapper;
    @Scheduled(cron="0 0 12 * * ?")
    public void myTest(){
        Program_Setting program_setting=programSettingDao.selectProgramSetting();
        int fileDays=program_setting.getDelFileDays();
        int logDays=program_setting.getDelLogDays();
        if(fileDays>0){
            List<String> inboxList=inboxMapper.selectFilePath(fileDays);
            for(int i=0;i<inboxList.size();i++){
                String path=inboxList.get( i );
                File file=new File( path );
                if(file.isFile()){
                    file.delete();
                }
            }
            List<String> outboxList=outboxMapper.selectFilePath(fileDays);
            for(int i=0;i<outboxList.size();i++){
                String path=outboxList.get( i );
                File file=new File( path );
                if(file.isFile()){
                    file.delete();
                }
            }
            List<String> returnList=returnReceiptMapper.selectFilePath(fileDays);
            for(int i=0;i<returnList.size();i++){
                String path=returnList.get( i );
                File file=new File( path );
                if(file.isFile()){
                    file.delete();
                }
            }
        }
        if(logDays>0){
            logMapper.deleteExpire(logDays);
        }
    }

}
