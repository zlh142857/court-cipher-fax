package com.hx.modle;

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
    private int sendernumber;//发送方号码
    private String senderunit;//发送方单位
    private int receivenumber;//接收号码
    private String filsavepath;//文件保存路径
    private Date create_time;//接收时间
    private String receiptpath;//回执文件保存路径
    private int isreceipt;//是否已回执

    @Override
    public String toString() {
        return "Inbox{" +
                "id=" + id +
                ", sendernumber=" + sendernumber +
                ", senderunit='" + senderunit + '\'' +
                ", receivenumber=" + receivenumber +
                ", filsavepath='" + filsavepath + '\'' +
                ", create_time=" + create_time +
                ", receiptpath='" + receiptpath + '\'' +
                ", isreceipt=" + isreceipt +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSendernumber(int sendernumber) {
        this.sendernumber = sendernumber;
    }

    public void setSenderunit(String senderunit) {
        this.senderunit = senderunit;
    }

    public void setReceivenumber(int receivenumber) {
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public int getSendernumber() {
        return sendernumber;
    }

    public String getSenderunit() {
        return senderunit;
    }

    public int getReceivenumber() {
        return receivenumber;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public String getReceiptpath() {
        return receiptpath;
    }

    public int getIsreceipt() {
        return isreceipt;
    }
}
