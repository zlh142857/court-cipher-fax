package com.hx.log4j;

import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;


/**
 * @author 范聪敏
 * @date 2019/10/20 22:02
 * @desc LOG4J日志数据库存储拓展
 */
public class Log4JJdbcAppender extends JDBCAppender {

    @Override
    protected String getLogStatement(LoggingEvent event) {
        CustomLoggingEvent bEvent=new CustomLoggingEvent(event);
        return super.getLogStatement(bEvent);
    }
}
