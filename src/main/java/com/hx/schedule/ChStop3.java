package com.hx.schedule;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/18 14:30
 *@功能:
 */

import com.hx.common.Fax;
import org.apache.log4j.Logger;

import static com.hx.service.impl.SendFaxServiceImpl.faxCh;
import static com.hx.service.impl.SendFaxServiceImpl.faxResult;

public class ChStop3 implements Runnable{
    private Logger logger=Logger.getLogger(this.getClass());
    @Override
    public void run(){
        try{
            int ch=3;
            int i=11;
            int state=Fax.INSTANCE.SsmGetChState(ch);
            int stateFax=Fax.INSTANCE.SsmGetChState(i);
            if(state==7){
                if(stateFax!=0){
                    Fax.INSTANCE.SsmFaxStop( i );
                    Fax.INSTANCE.SsmStopTalkWith( ch,i );
                }
                Fax.INSTANCE.SsmHangup(ch);
            }else if(state==3){
                if(faxCh==ch && faxResult.equals( "发送失败" )){
                    Fax.INSTANCE.SsmFaxStop( i );
                    Fax.INSTANCE.SsmStopTalkWith( ch,i );
                    Fax.INSTANCE.SsmHangup(ch);
                    faxResult="";
                }
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
