package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/12 14:04
 *@功能:
 */

import com.hx.common.SendSchTask;
import com.hx.dao.SchMapper;
import com.hx.modle.Sch_Task;
import com.hx.service.SendFaxService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

//每分钟查询一次定时任务,如果定时任务指定时间和当前系统时间相隔不到一分钟,就进行发送
public class SchTask implements Runnable{
    private static SchMapper schMapper;
    private static SchTask schTask;
    @PostConstruct
    public void init() {
        schTask=this;
        schTask.schMapper=this.schMapper;
    }
    public  void setSchMapper(SchMapper schMapper) {
        this.schMapper = schMapper;
    }
    private Logger logger=Logger.getLogger(this.getClass());
    @Override
    public void run(){
        try{
            //业务逻辑:先查询定时任务表
            List<Sch_Task> schTasks=schTask.schMapper.selectTask();
            //一分钟之内的任务添加到list集合,然后依次发送
            List<Sch_Task> list=new ArrayList<>(  );
            for(int i=0;i<schTasks.size();i++){
                long time=schTasks.get(i).getSendTime().getTime();
                long  nowTime = System.currentTimeMillis();
                long interval=time-nowTime;
                //如果时间差距在1分钟之内,就进行发送
                if(interval<=60000 && interval>=0){
                    list.add( schTasks.get( i ) );
                }
            }
            if(list.size()>0){
                System.out.println(list.toString());
                SendSchTask.sendSchTask(list);
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
