package com.hx.modle;

import org.omg.CORBA.DATA_CONVERSION;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/10/20 20:01
 * @desc 回收站
 */
public class RecycleBin implements Serializable {

    private int id;

    private Date recoverytime;

    private String senderunit;

    private String receivingunit;

    private int receivenumber;

    private int sendernumber;

    private String sendline;

    private String filsavepath;

    private String receiptpath;

    private int Isreceipt;

    private Date create_time;

    private String relate_type;

    private String message;

    @Override
    public String toString() {
        return "RecycleBin{" +
                "id=" + id +
                ", recoverytime=" + recoverytime +
                ", senderunit='" + senderunit + '\'' +
                ", receivingunit='" + receivingunit + '\'' +
                ", receivenumber=" + receivenumber +
                ", sendernumber=" + sendernumber +
                ", sendline='" + sendline + '\'' +
                ", filsavepath='" + filsavepath + '\'' +
                ", receiptpath='" + receiptpath + '\'' +
                ", Isreceipt=" + Isreceipt +
                ", create_time=" + create_time +
                ", relate_type='" + relate_type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecoverytime(Date recoverytime) {
        this.recoverytime = recoverytime;
    }

    public void setSenderunit(String senderunit) {
        this.senderunit = senderunit;
    }

    public void setReceivingunit(String receivingunit) {
        this.receivingunit = receivingunit;
    }

    public void setReceivenumber(int receivenumber) {
        this.receivenumber = receivenumber;
    }

    public void setSendernumber(int sendernumber) {
        this.sendernumber = sendernumber;
    }

    public void setSendline(String sendline) {
        this.sendline = sendline;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    public void setReceiptpath(String receiptpath) {
        this.receiptpath = receiptpath;
    }

    public void setIsreceipt(int isreceipt) {
        Isreceipt = isreceipt;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setRelate_type(String relate_type) {
        this.relate_type = relate_type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {

        return id;
    }

    public Date getRecoverytime() {
        return recoverytime;
    }

    public String getSenderunit() {
        return senderunit;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public int getReceivenumber() {
        return receivenumber;
    }

    public int getSendernumber() {
        return sendernumber;
    }

    public String getSendline() {
        return sendline;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    public String getReceiptpath() {
        return receiptpath;
    }

    public int getIsreceipt() {
        return Isreceipt;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public String getRelate_type() {
        return relate_type;
    }

    public String getMessage() {
        return message;
    }
}