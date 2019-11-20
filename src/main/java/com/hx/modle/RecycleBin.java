package com.hx.modle;

import com.hx.util.TimeHelper;
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

    private String receivenumber;

    private String sendnumber;

    private String sendline;

    private int Isreceipt;

    private Date create_time;

    private String relate_type;

    private String message;

    private String barCode;//条形码的值

    private int isLink;//是否已关联

    private String tifPath;
    private String Receiptpath;
    private String filsavepath;

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

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public void setSendline(String sendline) {
        this.sendline = sendline;
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

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setIsLink(int isLink) {
        this.isLink = isLink;
    }

    public void setTifPath(String tifPath) {
        this.tifPath = tifPath;
    }

    public void setReceiptpath(String receiptpath) {
        Receiptpath = receiptpath;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    public int getId() {

        return id;
    }

    public Date getRecoverytime() {
        return recoverytime;
    }

    public String getRecoverytimeFormat() {
        return TimeHelper.formatDateTime(recoverytime);
    }

    public String getSenderunit() {
        return senderunit;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public String getSendline() {
        return sendline;
    }

    public int getIsreceipt() {
        return Isreceipt;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public String getCreateTimeFormat() {
        return TimeHelper.formatDateTime(create_time);
    }

    public String getRelate_type() {
        return relate_type;
    }

    public String getMessage() {
        return message;
    }

    public String getBarCode() {
        return barCode;
    }

    public int getIsLink() {
        return isLink;
    }

    public String getTifPath() {
        return tifPath;
    }

    public String getReceiptpath() {
        return Receiptpath;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    @Override
    public String toString() {
        return "RecycleBin{" +
                "id=" + id +
                ", recoverytime=" + recoverytime +
                ", senderunit='" + senderunit + '\'' +
                ", receivingunit='" + receivingunit + '\'' +
                ", receivenumber='" + receivenumber + '\'' +
                ", sendnumber='" + sendnumber + '\'' +
                ", sendline='" + sendline + '\'' +
                ", Isreceipt=" + Isreceipt +
                ", create_time=" + create_time +
                ", relate_type='" + relate_type + '\'' +
                ", message='" + message + '\'' +
                ", barCode='" + barCode + '\'' +
                ", isLink=" + isLink +
                ", tifPath='" + tifPath + '\'' +
                ", Receiptpath='" + Receiptpath + '\'' +
                ", filsavepath='" + filsavepath + '\'' +
                '}';
    }
}