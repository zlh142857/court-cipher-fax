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
    private boolean isSuccess;
    private String telResult;//电话通知结果
    private int pageNum;//文件页数

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getTelResult() {
        return telResult;
    }

    public void setTelResult(String telResult) {
        this.telResult = telResult;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public void setReceivingunit(String receivingunit) {
        this.receivingunit = receivingunit;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getSendline() {
        return sendline;
    }

    public void setSendline(String sendline) {
        this.sendline = sendline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
                ", isSuccess=" + isSuccess +
                ", telResult=" + telResult +
                ", pageNum=" + pageNum +
                '}';
    }
}
