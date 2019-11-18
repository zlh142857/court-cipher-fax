package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/12 14:50
 *@功能:
 */

import com.hx.modle.Sch_Task;
import com.hx.service.SendFaxService;

import javax.annotation.PostConstruct;
import java.util.List;

public class SendSchTask {
    private static SendFaxService sendFaxService;
    private static SendSchTask sendSchTask;
    @PostConstruct
    public void init() {
        sendSchTask=this;
        sendSchTask.sendFaxService=this.sendFaxService;
    }
    public  void setSendFaxService(SendFaxService sendFaxService) {
        this.sendFaxService = sendFaxService;
    }
    public static void sendSchTask(List<Sch_Task> schTaskList) throws Exception {
        for(int i=0;i<schTaskList.size();i++){
            String tifPath=schTaskList.get( i ).getTifPath();
            String receiptPath=schTaskList.get( i ).getReceiptPath();
            String courtName=schTaskList.get( i ).getCourtName();
            String receiveNumber=schTaskList.get( i ).getReceiveNumber();
            String sendNumber=schTaskList.get( i ).getSendNumber();
            String isBack=schTaskList.get( i ).getIsBack();
            String ch=schTaskList.get( i ).getCh();
            String filename=schTaskList.get( i ).getFilename();
            String outboxId=schTaskList.get( i ).getOutboxId();
            sendSchTask.sendFaxService.sendFax(tifPath,receiptPath,courtName,receiveNumber,sendNumber,isBack,filename,outboxId,ch);
            sendFaxService.deleteSchTask(schTaskList.get( i ).getId());
            Thread.sleep( 3000 );
        }
    }
}
