package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/30 13:27
 *@功能:
 */

import java.io.Serializable;

public class Program_Setting implements Serializable {
    private static final long serialVersionUID = -8605147854312074502L;
    private int id;
    private String courtName;
    private int isPrint;
    private String printService;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public int getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(int isPrint) {
        this.isPrint = isPrint;
    }

    public String getPrintService() {
        return printService;
    }

    public void setPrintService(String printService) {
        this.printService = printService;
    }

    @Override
    public String toString() {
        return "Program_Setting{" +
                "id=" + id +
                ", courtName='" + courtName + '\'' +
                ", isPrint=" + isPrint +
                ", printService='" + printService + '\'' +
                '}';
    }
}
