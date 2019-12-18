package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/2 18:10
 *@功能:
 */

import com.hx.dao.ProgramSettingDao;
import com.hx.modle.Program_Setting;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

public class CloseService implements Runnable {
    private Logger logger=Logger.getLogger(this.getClass());
    private static ProgramSettingDao programSettingDao;
    private static CloseService closeService;
    @PostConstruct
    public void init() {
        closeService=this;
        closeService.programSettingDao=this.programSettingDao;
    }
    public  void setProgramSettingDao(ProgramSettingDao programSettingDao) {
        this.programSettingDao = programSettingDao;
    }
    @Override
    public void run(){
        try{
            Program_Setting program_setting=closeService.programSettingDao.selectProgramSetting();
            boolean flag=program_setting.getIsAutoClose();
            if(flag){
                long time=program_setting.getAutoCloseDate().getTime();
                long  nowTime = System.currentTimeMillis();
                long interval=time-nowTime;
                //如果时间差距在1分钟之内,就进行发送
                if(interval<=60000 && interval>=0){
                    Runtime.getRuntime().exec("shutdown /s /t " + 60);  //关机时间可以自动设置
                }
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
