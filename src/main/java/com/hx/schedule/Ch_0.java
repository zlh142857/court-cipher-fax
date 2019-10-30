package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/11 10:44
 *@功能:
 */

import com.hx.common.Decide;
import com.hx.common.Fax;
import org.apache.log4j.Logger;

//监听通道0
public class Ch_0 implements Runnable {
    private Logger logger=Logger.getLogger(this.getClass());
    @Override
    public void run(){
        try{
            //业务逻辑:先查询通道的状态码
            int stateCode=Fax.INSTANCE.SsmGetChState(0);
            if(stateCode!=-1){
                //当前状态为振铃,便开始接收
                Decide.decideCh(stateCode,0);
            }else{
                logger.error( "查看通道编号0状态调用失败" );
            }
        }catch (Throwable t){
            logger.error( t.toString() );
        }
    }
}
