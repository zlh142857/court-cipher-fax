package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/11 10:41
 *@功能:
 */

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//定时器线程池,一个线程监听一个通道
public class ScheduleTask {
    public static void ScheduleTask(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.scheduleAtFixedRate(new Ch_0(), 0, 1, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new Ch_1(), 0, 1, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new Ch_2(), 0, 1, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new Ch_3(), 0, 1, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new SchTask(), 0, 1, TimeUnit.MINUTES);
    }
}
