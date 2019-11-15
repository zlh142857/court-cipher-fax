package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/14 16:27
 *@功能:
 */

import java.io.Serializable;
import java.util.Date;

public class WebModel implements Serializable {
    private static final long serialVersionUID = 455716729375226875L;
    private String msg;
    private Date time;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "WebModel{" +
                "msg='" + msg + '\'' +
                ", time=" + time +
                '}';
    }
}
