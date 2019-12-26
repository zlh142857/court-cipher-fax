package com.hx.BackUp;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/25 15:20
 *@功能:
 */

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hx.dao.SendReceiptMapper;
import com.hx.modle.Send_Receipt;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class ExcelSendReceiptListener extends AnalysisEventListener<Send_Receipt> {
    private static Logger logger=Logger.getLogger( ExcelSendReceiptListener.class );
    private static SendReceiptMapper sendReceiptMapper;
    private static ExcelSendReceiptListener excelSendReceiptListener;
    public  void setSendReceiptMapper(SendReceiptMapper sendReceiptMapper) {
        this.sendReceiptMapper = sendReceiptMapper;
    }
    @PostConstruct
    public void init() {
        excelSendReceiptListener=this;
        excelSendReceiptListener.sendReceiptMapper=this.sendReceiptMapper;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    List<Send_Receipt> list = new ArrayList<Send_Receipt>();
    private static int count = 1;
    @Override
    public void invoke(Send_Receipt data, AnalysisContext context) {
        list.add(data);
        count ++;
        if (list.size() >= BATCH_COUNT) {
            saveData( count );
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData( count );
        logger.info("发回执箱备份恢复完成");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(int count) {
        try{
            //进行数据存入数据库
            int flag=excelSendReceiptListener.sendReceiptMapper.insertMuchSendReceipt(list);
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
