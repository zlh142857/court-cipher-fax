package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/11 10:44
 *@功能:
 */

import com.hx.common.Decide;
import com.hx.common.Fax;
import com.hx.dao.ProgramSettingDao;
import com.hx.modle.Program_Setting;
import com.hx.service.SendFaxService;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ch_1 implements Runnable {
    private Logger logger=Logger.getLogger(this.getClass());
    private static SendFaxService sendFaxService;
    private static ProgramSettingDao programSettingDao;
    private static Ch_1 ch_1;
    @PostConstruct
    public void init() {
        ch_1=this;
        ch_1.sendFaxService=this.sendFaxService;
        ch_1.programSettingDao=this.programSettingDao;
    }
    public  void setSendFaxService(SendFaxService sendFaxService) {
        this.sendFaxService = sendFaxService;
    }
    public  void setProgramSettingDao(ProgramSettingDao programSettingDao) {
        this.programSettingDao = programSettingDao;
    }
    @Override
    public void run(){
        try{
            //业务逻辑:先查询通道的状态码
            int stateCode=Fax.INSTANCE.SsmGetChState(1);
            if(stateCode!=-1){
                if(stateCode==2){
                    int chType=ch_1.sendFaxService.selectChType(1);
                    //允许接收
                    if(chType!=2){
                        Program_Setting program_setting=ch_1.programSettingDao.selectProgramSetting();
                        int start=program_setting.getStartTime();
                        int end=program_setting.getEndTime();
                        Date date=new Date(  );
                        SimpleDateFormat sdf=new SimpleDateFormat( "HH" );
                        int now=Integer.valueOf(sdf.format( date ));
                        if(now>=start && now< end){
                            //当前状态为振铃,便开始接收
                            Decide.decideCh(1);
                        }else{
                            Fax.INSTANCE.SsmHangup( 1 );
                        }
                    }else{
                        Fax.INSTANCE.SsmHangup( 1 );
                    }
                }
            }else{
                logger.error( "查看通道编号1状态调用失败" );
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
