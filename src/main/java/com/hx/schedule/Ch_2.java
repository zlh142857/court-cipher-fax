package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/11 10:44
 *@功能:
 */

import com.hx.common.Decide;
import com.hx.common.Fax;
import com.hx.service.SendFaxService;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

public class Ch_2 implements Runnable {
    private Logger logger=Logger.getLogger(this.getClass());
    private static SendFaxService sendFaxService;
    private static Ch_2 ch_2;
    @PostConstruct
    public void init() {
        ch_2=this;
        ch_2.sendFaxService=this.sendFaxService;
    }
    public  void setSendFaxService(SendFaxService sendFaxService) {
        this.sendFaxService = sendFaxService;
    }
    @Override
    public void run(){
        try{
            //业务逻辑:先查询通道的状态码
            int stateCode=Fax.INSTANCE.SsmGetChState(2);
            if(stateCode!=-1){
                int chType=ch_2.sendFaxService.selectChType(2);
                if(chType!=2){
                    //当前状态为振铃,便开始接收
                    Decide.decideCh(stateCode,2);
                }
            }else{
                logger.error( "查看通道编号2状态调用失败" );
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
