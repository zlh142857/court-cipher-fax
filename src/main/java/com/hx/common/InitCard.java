package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/10 14:21
 *@功能:
 */

import com.hx.dao.ProgramSettingDao;
import com.hx.schedule.Ch_0;
import com.hx.schedule.ScheduleTask;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.net.*;
import java.util.Optional;

import static com.hx.common.StaticFinal.TEMPDIR;
import static com.hx.schedule.ScheduleTask.chBadMsgStopService;
import static com.hx.schedule.ScheduleTask.executorService;

//项目启动自动初始化板卡一次
//在初始化板卡成功后,调用定时器任务,开始监听接收传真
public class InitCard implements ServletContextListener {
    private Logger logger=Logger.getLogger(this.getClass());
    private static ProgramSettingDao programSettingDao;
    private static InitCard initCard;
    @PostConstruct
    public void init() {
        initCard=this;
        initCard.programSettingDao=this.programSettingDao;
    }
    public  void setProgramSettingDao(ProgramSettingDao programSettingDao) {
        this.programSettingDao = programSettingDao;
    }
    //初始化方法
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String address="";
            String ip="";
            InetAddress addr = InetAddress.getLocalHost();
            address=addr.getHostName();
            String OIp=getIpBySocket().toString();
            int begin=OIp.indexOf( "/" );
            int end=OIp.indexOf( "]" );
            ip=OIp.substring( begin+1,end );
            programSettingDao.updateServerWindowsName(address,ip);
        } catch (UnknownHostException e) {
            logger.error( e.toString());
        } catch (SocketException e) {
            logger.error( e.toString() );
        }
        try{
            String lpSsmCfgFileName = "C:\\ShCti\\ShConfig.ini";
            String lpIndexCfgFileName = "C:\\ShCti\\ShIndex.ini";
            int isOk = Fax.INSTANCE.SsmStartCti(lpSsmCfgFileName, lpIndexCfgFileName);
            if(isOk==0){
                //在初始化板卡成功后,调用定时器任务,开始监听接收传真
                ScheduleTask.ScheduleTask();
                ScheduleTask.chBadMsgStop();
            }else if(isOk==-2){
                logger.error( "初始化失败，因为驱动程序已经装载" );
            }else if(isOk==-1){
                String message=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error( "初始化失败:"+message );
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Fax.INSTANCE.SsmCloseCti();
        executorService.shutdownNow();
        chBadMsgStopService.shutdownNow();
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
    private static Optional<Inet4Address> getIpBySocket() throws SocketException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            if (socket.getLocalAddress() instanceof Inet4Address) {
                return Optional.of((Inet4Address) socket.getLocalAddress());
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
