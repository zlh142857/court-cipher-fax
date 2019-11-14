package com.hx.modle;


import java.io.Serializable;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/10/20 20:01
 * @desc 回收站
 */
public class RecycleBin implements Serializable {
    private static final long serialVersionUID = 2584724251504684816L;
    private int id;
    private Date recoverytime;
    private String senderunit;
    private String receivingunit;
    private String receivenumber;
    private String sendernumber;
    private String sendline;
    private String filsavepath;
    private String receiptpath;
    private int Isreceipt;
    private Date create_time;
    private String relate_type;
    private String message;
    private int isLink;
    private String barCode;
    private String tifPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRecoverytime() {
        return recoverytime;
    }

    public void setRecoverytime(Date recoverytime) {
        this.recoverytime = recoverytime;
    }

    public String getSenderunit() {
        return senderunit;
    }

    public void setSenderunit(String senderunit) {
        this.senderunit = senderunit;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public void setReceivingunit(String receivingunit) {
        this.receivingunit = receivingunit;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public String getSendernumber() {
        return sendernumber;
    }

    public void setSendernumber(String sendernumber) {
        this.sendernumber = sendernumber;
    }

    public String getSendline() {
        return sendline;
    }

    public void setSendline(String sendline) {
        this.sendline = sendline;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    public String getReceiptpath() {
        return receiptpath;
    }

    public void setReceiptpath(String receiptpath) {
        this.receiptpath = receiptpath;
    }

    public int getIsreceipt() {
        return Isreceipt;
    }

    public void setIsreceipt(int isreceipt) {
        Isreceipt = isreceipt;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getRelate_type() {
        return relate_type;
    }

    public void setRelate_type(String relate_type) {
        this.relate_type = relate_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getTifPath() {
        return tifPath;
    }

    public void setTifPath(String tifPath) {
        this.tifPath = tifPath;
    }

    @Override
    public String toString() {
        return "RecycleBin{" +
                "id=" + id +
                ", recoverytime=" + recoverytime +
                ", senderunit='" + senderunit + '\'' +
                ", receivingunit='" + receivingunit + '\'' +
                ", receivenumber='" + receivenumber + '\'' +
                ", sendernumber='" + sendernumber + '\'' +
                ", sendline='" + sendline + '\'' +
                ", filsavepath='" + filsavepath + '\'' +
                ", receiptpath='" + receiptpath + '\'' +
                ", Isreceipt=" + Isreceipt +
                ", create_time=" + create_time +
                ", relate_type='" + relate_type + '\'' +
                ", message='" + message + '\'' +
                ", isLink=" + isLink +
                ", barCode='" + barCode + '\'' +
                ", tifPath='" + tifPath + '\'' +
                '}';
    }
}