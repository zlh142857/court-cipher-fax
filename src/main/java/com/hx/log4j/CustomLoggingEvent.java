package com.hx.log4j;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/20 22:06
 * @desc 自定义LOG4J事件处理
 */
public class CustomLoggingEvent  extends LoggingEvent {

    public CustomLoggingEvent(LoggingEvent event) {
        super(event.fqnOfCategoryClass, event.getLogger(), event.getLevel(), event.getMessage(), null);
    }

    public CustomLoggingEvent(String fqnOfCategoryClass, Category logger, Priority level, Object message, Throwable throwable) {
        super(fqnOfCategoryClass, logger, level, message, throwable);
    }

    public CustomLoggingEvent(String fqnOfCategoryClass, Category logger, long timeStamp, Priority level, Object message, Throwable throwable) {
        super(fqnOfCategoryClass, logger, timeStamp, level, message, throwable);
    }

    public CustomLoggingEvent(String fqnOfCategoryClass, Category logger, long timeStamp, Level level, Object message, String threadName, ThrowableInformation throwable, String ndc, LocationInfo info, Map properties) {
        super(fqnOfCategoryClass, logger, timeStamp, level, message, threadName, throwable, ndc, info, properties);
    }

    @Override
    public String getRenderedMessage() {
        String msg = super.getRenderedMessage();
        if (msg.indexOf("'") != -1) {
            msg = msg.replaceAll("'", "''");
        }
        return msg;
    }

    @Override
    public String getThreadName() {
        String thrdName = super.getThreadName();
        if (thrdName.indexOf("'") != -1) {
            thrdName = thrdName.replaceAll("'", "''");
        }
        return thrdName;
    }
}
