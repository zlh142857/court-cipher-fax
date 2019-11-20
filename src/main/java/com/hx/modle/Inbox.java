package com.hx.modle;

import com.hx.util.TimeHelper;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/9/4 15:40
 * @desc
 */
public class Inbox implements Serializable {
    private static final long serialVersionUID = 1670857753595889875L;

    private int id;//id
    private String sendnumber;//发送方号码
    private String senderunit;//发送方单位
    private String receivenumber;//接收号码
    private String filsavepath;//文件保存路径
    private Date create_time;//接收时间
    private String receiptpath;//回执文件保存路径
    private int isreceipt;//是否已回执
    private String barCode;//条形码的值
    private int isLink;//是否已关联

    @Override
    public String toString() {
        return "Inbox{" +
                "id=" + id +
                ", sendnumber='" + sendnumber + '\'' +
                ", senderunit='" + senderunit + '\'' +
                ", receivenumber='" + receivenumber + '\'' +
                ", filsavepath='" + filsavepath + '\'' +
                ", create_time=" + create_time +
                ", receiptpath='" + receiptpath + '\'' +
                ", isreceipt=" + isreceipt +
                ", barCode='" + barCode + '\'' +
                ", isLink=" + isLink +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public void setSenderunit(String senderunit) {
        this.senderunit = senderunit;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setReceiptpath(String receiptpath) {
        this.receiptpath = receiptpath;
    }

    public void setIsreceipt(int isreceipt) {
        this.isreceipt = isreceipt;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setIsLink(int isLink) {
        this.isLink = isLink;
    }

    public static long getSerialVersionUID() {

        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public String getSenderunit() {
        return senderunit;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public String getCreateTimeFormat() {
        return TimeHelper.formatDateTime(create_time);
    }

    public String getReceiptpath() {
        return receiptpath;
    }

    public int getIsreceipt() {
        return isreceipt;
    }

    public String getBarCode() {
        return barCode;
    }

    public int getIsLink() {
        return isLink;
    }
}