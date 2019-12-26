package com.hx.BackUp;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/24 9:59
 *@功能:
 */

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hx.dao.InboxMapper;
import com.hx.modle.Inbox;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class ExcelInboxListener extends AnalysisEventListener<Inbox> {
    private static Logger logger=Logger.getLogger( ExcelInboxListener.class );
    private static InboxMapper inboxMapper;
    private static ExcelInboxListener excelInboxListener;
    public  void setInboxMapper(InboxMapper inboxMapper) {
        this.inboxMapper = inboxMapper;
    }
    @PostConstruct
    public void init() {
        excelInboxListener=this;
        excelInboxListener.inboxMapper=this.inboxMapper;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    List<Inbox> list = new ArrayList<Inbox>();
    private static int count = 1;
    @Override
    public void invoke(Inbox data, AnalysisContext context) {
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
        logger.info("收件箱备份恢复完成");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(int count) {
        try{
            //进行数据存入数据库
            int flag=excelInboxListener.inboxMapper.insertMuchInbox(list);
        }catch (Exception e){
            logger.error( e.toString() );
        }
    }

}
