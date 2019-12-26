package com.hx.BackUp;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/25 13:51
 *@功能:
 */

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hx.dao.OutboxMapper;
import com.hx.modle.Outbox;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class ExcelOutboxListener extends AnalysisEventListener<Outbox> {
    private static Logger logger=Logger.getLogger( ExcelOutboxListener.class );
    private static OutboxMapper outboxMapper;
    private static ExcelOutboxListener excelOutboxListener;
    public  void setOutboxMapper(OutboxMapper outboxMapper) {
        this.outboxMapper = outboxMapper;
    }
    @PostConstruct
    public void init() {
        excelOutboxListener=this;
        excelOutboxListener.outboxMapper=this.outboxMapper;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    List<Outbox> list = new ArrayList<Outbox>();
    private static int count = 1;
    @Override
    public void invoke(Outbox data, AnalysisContext context) {
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
        logger.info("发件箱备份恢复完成");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(int count) {
        try{
            //进行数据存入数据库
            int flag=excelOutboxListener.outboxMapper.insertMuchOutbox(list);
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }
}
