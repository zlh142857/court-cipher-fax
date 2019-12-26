package com.hx.BackUp;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/25 16:07
 *@功能:
 */

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hx.dao.ReturnReceiptMapper;
import com.hx.modle.Return_Receipt;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class ExcelReturnReceiptListener extends AnalysisEventListener<Return_Receipt> {
    private static Logger logger=Logger.getLogger( ExcelReturnReceiptListener.class );
    private static ReturnReceiptMapper returnReceiptMapper;
    private static ExcelReturnReceiptListener excelReturnReceiptListener;
    public  void setReturnReceiptMapper(ReturnReceiptMapper returnReceiptMapper) {
        this.returnReceiptMapper = returnReceiptMapper;
    }
    @PostConstruct
    public void init() {
        excelReturnReceiptListener=this;
        excelReturnReceiptListener.returnReceiptMapper=this.returnReceiptMapper;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    List<Return_Receipt> list = new ArrayList<Return_Receipt>();
    private static int count = 1;
    @Override
    public void invoke(Return_Receipt data, AnalysisContext context) {
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
        logger.info("收回执箱备份恢复完成");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(int count) {
        try{
            //进行数据存入数据库
            int flag=excelReturnReceiptListener.returnReceiptMapper.insertMuchReturnReceipt(list);
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
