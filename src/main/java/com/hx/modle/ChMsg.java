package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/7 10:19
 *@功能:
 */

import java.io.Serializable;

public class ChMsg implements Serializable {
    private static final long serialVersionUID = -6310464266374528107L;
    private int ch;              //通道编号
    private int pages;           //已发送完成页数
    private String speed;        //发送速率
    private String message;      //通道具体信息
    private String selfNumber;   //通道对应的号码
    private String sendNumber;//接收时显示发送方的号码
    private boolean flag;        //判断是否空闲,空闲为true,不空闲为false
    private float percentage;   //百分比
    private String bytes;   //字节数

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSelfNumber() {
        return selfNumber;
    }

    public void setSelfNumber(String selfNumber) {
        this.selfNumber = selfNumber;
    }

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "ChMsg{" +
                "ch=" + ch +
                ", pages=" + pages +
                ", speed=" + speed +
                ", message='" + message + '\'' +
                ", selfNumber='" + selfNumber + '\'' +
                ", sendNumber='" + sendNumber + '\'' +
                ", flag=" + flag +
                ", percentage=" + percentage +
                ", bytes=" + bytes +
                '}';
    }
}
