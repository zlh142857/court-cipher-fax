package com.hx.modle;

import com.hx.util.TimeHelper;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/9/2 16:53
 * @desc 已发邮件
 */
public class Outbox implements Serializable {
    private static final long serialVersionUID = -293973874820213180L;
    private int id;//id
    private String sendnumber;//发送号码
    private String receivenumber;//接收号码
    private String receivingunit;//接收单位
    private Date create_time;//发送时间
    private String sendline;//文件标题
    private String message;
    private String filsavepath;

    public String getFilsavepath() {
        return filsavepath;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    @Override
    public String toString() {
        return "Outbox{" +
                "id=" + id +
                ", sendnumber='" + sendnumber + '\'' +
                ", receivenumber='" + receivenumber + '\'' +
                ", receivingunit='" + receivingunit + '\'' +
                ", create_time=" + create_time +
                ", sendline='" + sendline + '\'' +
                ", message='" + message + '\'' +
                ", filsavepath='" + filsavepath + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public void setReceivingunit(String receivingunit) {
        this.receivingunit = receivingunit;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setSendline(String sendline) {
        this.sendline = sendline;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getId() {
        return id;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public String getCreateTimeFormat() {
        return TimeHelper.formatDateTime(create_time);
    }

    public String getSendline() {
        return sendline;
    }

    public String getMessage() {
        return message;
    }


}
