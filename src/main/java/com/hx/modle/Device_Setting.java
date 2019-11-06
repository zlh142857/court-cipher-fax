package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/24 14:58
 *@功能:设备设置表,电话线路设置
 */

import java.io.Serializable;

public class Device_Setting implements Serializable {
    private static final long serialVersionUID = 2412734508087829096L;
    private Integer id; //id
    private Integer ch;  //通道编号
    private String prefix;  //前缀
    private String areaCode;  //区号
    private String seatNumber; //座机号
    private Integer chType;  //通道类型,仅接受1,仅发送2,或者是接收发送都有0,默认为0
    private boolean editFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCh() {
        return ch;
    }

    public void setCh(Integer ch) {
        this.ch = ch;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getChType() {
        return chType;
    }

    public void setChType(Integer chType) {
        this.chType = chType;
    }

    public boolean isEditFlag() {
        return editFlag;
    }

    public void setEditFlag(boolean editFlag) {
        this.editFlag = editFlag;
    }

    @Override
    public String toString() {
        return "Device_Setting{" +
                "id=" + id +
                ", ch=" + ch +
                ", prefix='" + prefix + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", chType=" + chType +
                ", editFlag=" + editFlag +
                '}';
    }
}
