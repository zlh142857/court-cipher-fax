package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/15 9:38
 *@功能:
 */

import com.hx.common.SendSchTask;
import com.hx.modle.Sch_Task;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.List;

import static com.hx.schedule.SchTask.schCount;
import static com.hx.schedule.SchTask.schTasks;
//进行发送
public class SchSend implements Runnable{
    private Logger logger=Logger.getLogger(this.getClass());
    @Override
    public void run(){
        try{
            if(schCount==1){
                List<Sch_Task> list=new ArrayList<>(  );
                for(int i=0;i<schTasks.size();i++){
                    list.add( schTasks.get( i ) );
                }
                schTasks=new ArrayList<>(  );
                for(int i=0;i<list.size();i++){
                    SendSchTask.sendSchTask(list);
                }
                schCount=0;
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
