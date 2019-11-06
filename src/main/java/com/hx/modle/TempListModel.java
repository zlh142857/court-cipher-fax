package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/1 16:42
 *@功能:
 */

import java.io.Serializable;
import java.util.List;

public class TempListModel implements Serializable {
    private static final long serialVersionUID = -5843750399159133964L;
    private String tifPath;
    private String receiptPath;
    private String sendNumber;
    private String filename;
    private int isBack;
    private int ch;
    private int id;
    private String tempModels;

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

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getIsBack() {
        return isBack;
    }

    public void setIsBack(int isBack) {
        this.isBack = isBack;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTempModels() {
        return tempModels;
    }

    public void setTempModels(String tempModels) {
        this.tempModels = tempModels;
    }

    @Override
    public String toString() {
        return "TempListModel{" +
                "tifPath='" + tifPath + '\'' +
                ", receiptPath='" + receiptPath + '\'' +
                ", sendNumber='" + sendNumber + '\'' +
                ", filename='" + filename + '\'' +
                ", isBack=" + isBack +
                ", ch=" + ch +
                ", id=" + id +
                ", tempModels='" + tempModels + '\'' +
                '}';
    }
}
