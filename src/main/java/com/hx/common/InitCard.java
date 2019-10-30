package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/10 14:21
 *@功能:
 */

import com.hx.modle.Device_Setting;
import com.hx.schedule.ScheduleTask;
import com.hx.service.SendFaxService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

//项目启动自动初始化板卡一次
//在初始化板卡成功后,调用定时器任务,开始监听接收传真
public class InitCard implements ServletContextListener {
    private Logger logger=Logger.getLogger(this.getClass());
    @Autowired
    private SendFaxService sendFaxService;
    //初始化方法
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String lpSsmCfgFileName = "C:\\ShCti\\ShConfig.ini";
        String lpIndexCfgFileName = "C:\\ShCti\\ShIndex.ini";
        int isOk = Fax.INSTANCE.SsmStartCti(lpSsmCfgFileName, lpIndexCfgFileName);
        if(isOk==0){
            //在初始化板卡成功后,调用定时器任务,开始监听接收传真
            ScheduleTask.ScheduleTask();
            //从数据库查询,哪些通道编号仅接受,仅发送
            /*List<Device_Setting> list=sendFaxService.selectChType();
            int length=list.size();
            if(length>0){
                for(int i=0;i<length;i++){
                    int ch=list.get( i ).getCh();
                    int chType=list.get( i ).getChType();
                    if(chType==1){

                    }
                }
            }*/
        }else if(isOk==-2){
            logger.error( "初始化失败，因为驱动程序已经装载" );
        }else if(isOk==-1){
            String message=Fax.INSTANCE.SsmGetLastErrMsgA();
            logger.error( "初始化失败:"+message );
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Fax.INSTANCE.SsmCloseCti();
    }
}
