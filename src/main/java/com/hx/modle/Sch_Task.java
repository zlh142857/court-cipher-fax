package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/12 14:24
 *@功能:
 */

import java.io.Serializable;
import java.util.Date;

public class Sch_Task implements Serializable {
    private static final long serialVersionUID = -3074414984571399362L;
    private int id;
    private String tifPath;//正文路径
    private String receiptPath;//回执文件路径
    private String courtName;//接收方法院名称
    private String receiveNumber;//接收的号码
    private String sendNumber;//指定发送的号码
    private String isBack;//是否回执标识
    private String ch;//指定的通道编号
    private String filename;//文件标题
    private String outboxId;//指定的要修改是否回执状态的收件箱id
    private Date sendTime;//指定发送时间
    private Date createTime;//创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTifPath() {
        return tifPath;
    }

    public void setTifPath(String tifPath) {
        this.tifPath = tifPath;
    }

    public String getReceiptPath() {
        return receiptPath;
    }

    public void setReceiptPath(String receiptPath) {
        this.receiptPath = receiptPath;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(String receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getIsBack() {
        return isBack;
    }

    public void setIsBack(String isBack) {
        this.isBack = isBack;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOutboxId() {
        return outboxId;
    }

    public void setOutboxId(String outboxId) {
        this.outboxId = outboxId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Sch_Task{" +
                "id=" + id +
                ", tifPath='" + tifPath + '\'' +
                ", receiptPath='" + receiptPath + '\'' +
                ", courtName='" + courtName + '\'' +
                ", receiveNumber='" + receiveNumber + '\'' +
                ", sendNumber='" + sendNumber + '\'' +
                ", isBack='" + isBack + '\'' +
                ", ch='" + ch + '\'' +
                ", filename='" + filename + '\'' +
                ", outboxId='" + outboxId + '\'' +
                ", sendTime=" + sendTime +
                ", createTime=" + createTime +
                '}';
    }
}
