package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/12 14:04
 *@功能:
 */

import com.hx.dao.SchMapper;
import com.hx.modle.Sch_Task;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
//每分钟查询一次公共list集合,有值就获取list值进行发送,然后把公共的list置为空,公共list每分钟都从数据库查一次马上要发送的值,有几条就count==几,
//然后获取把公共的list集合赋值给新的私有list集合,然后新list集合进行发送
public class SchTask implements Runnable{
    private static SchMapper schMapper;
    private static SchTask schTask;
    public static List<Sch_Task> schTasks=new ArrayList<>(  );
    public static int schCount=0;
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
            List<Sch_Task> schTaskList=schTask.schMapper.selectTask();
            //一分钟之内的任务添加到list集合,然后依次发送
            for(int i=0;i<schTaskList.size();i++){
                long time=schTaskList.get(i).getSendTime().getTime();
                long  nowTime = System.currentTimeMillis();
                long interval=time-nowTime;
                //如果时间差距在1分钟之内,就进行发送
                if(interval<=60000 && interval>=0){
                    schTasks.add( schTaskList.get( i ) );
                }
            }
            if(schTasks.size()>0){
                schCount=1;
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
