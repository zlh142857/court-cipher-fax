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

public class Ch_1 implements Runnable {
    private Logger logger=Logger.getLogger(this.getClass());
    private static SendFaxService sendFaxService;
    private static Ch_1 ch_1;
    @PostConstruct
    public void init() {
        ch_1=this;
        ch_1.sendFaxService=this.sendFaxService;
    }
    public  void setSendFaxService(SendFaxService sendFaxService) {
        this.sendFaxService = sendFaxService;
    }
    @Override
    public void run(){
        try{
            //业务逻辑:先查询通道的状态码
            int stateCode=Fax.INSTANCE.SsmGetChState(1);
            if(stateCode!=-1){
                int chType=ch_1.sendFaxService.selectChType(1);
                if(chType!=2){
                    //当前状态为振铃,便开始接收
                    Decide.decideCh(stateCode,1);
                }
            }else{
                logger.error( "查看通道编号1状态调用失败" );
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
