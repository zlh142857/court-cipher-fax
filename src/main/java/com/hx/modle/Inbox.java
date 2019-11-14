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
    private String sendernumber;//发送方号码
    private String senderunit;//发送方单位
    private String receivenumber;//接收号码
    private String filsavepath;//文件保存路径
    private Date create_time;//接收时间
    private String receiptpath;//回执文件保存路径
    private int isreceipt;//是否已回执
    private int isLink; //是否已关联
    private String barCode;//条形码的值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSendernumber() {
        return sendernumber;
    }

    public void setSendernumber(String sendernumber) {
        this.sendernumber = sendernumber;
    }

    public String getSenderunit() {
        return senderunit;
    }

    public void setSenderunit(String senderunit) {
        this.senderunit = senderunit;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getReceiptpath() {
        return receiptpath;
    }

    public void setReceiptpath(String receiptpath) {
        this.receiptpath = receiptpath;
    }

    public int getIsreceipt() {
        return isreceipt;
    }

    public void setIsreceipt(int isreceipt) {
        this.isreceipt = isreceipt;
    }

    public int getIsLink() {
        return isLink;
    }

    public void setIsLink(int isLink) {
        this.isLink = isLink;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String toString() {
        return "Inbox{" +
                "id=" + id +
                ", sendernumber='" + sendernumber + '\'' +
                ", senderunit='" + senderunit + '\'' +
                ", receivenumber='" + receivenumber + '\'' +
                ", filsavepath='" + filsavepath + '\'' +
                ", create_time=" + create_time +
                ", receiptpath='" + receiptpath + '\'' +
                ", isreceipt=" + isreceipt +
                ", isLink=" + isLink +
                ", barCode='" + barCode + '\'' +
                '}';
    }
}
