package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/10 14:21
 *@功能:
 */

import com.hx.schedule.ScheduleTask;
import com.hx.service.SendFaxService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

import static com.hx.common.StaticFinal.TEMPDIR;
import static com.hx.schedule.ScheduleTask.executorService;

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
        executorService.shutdownNow();
        File directory=new File( TEMPDIR );
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if(files.length>0){
                for (File f : files) {
                    if(f.isFile()){
                        f.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
