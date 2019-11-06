package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/30 9:56
 *@功能:
 */

import java.io.Serializable;

public class TempModel implements Serializable {
    private static final long serialVersionUID = -922290095790271279L;
    private int codeMode;
    private int endFlag;
    private String courtName;
    private String receiveNumber;

    public int getCodeMode() {
        return codeMode;
    }

    public void setCodeMode(int codeMode) {
        this.codeMode = codeMode;
    }

    public int getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(int endFlag) {
        this.endFlag = endFlag;
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

    @Override
    public String toString() {
        return "TempModel{" +
                "codeMode=" + codeMode +
                ", endFlag=" + endFlag +
                ", courtName='" + courtName + '\'' +
                ", receiveNumber='" + receiveNumber + '\'' +
                '}';
    }
}
