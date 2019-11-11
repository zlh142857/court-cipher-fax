package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/30 9:56
 *@功能:
 */

import java.io.Serializable;

public class TempModel implements Serializable {
    private static final long serialVersionUID = -922290095790271279L;
    private String courtName;
    private String receiveNumber;
    private String tifPath;

    public String getTifPath() {
        return tifPath;
    }

    public void setTifPath(String tifPath) {
        this.tifPath = tifPath;
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
                "courtName='" + courtName + '\'' +
                ", receiveNumber='" + receiveNumber + '\'' +
                ", tifPath='" + tifPath + '\'' +
                '}';
    }
}
