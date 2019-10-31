package com.hx.modle;

import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/10/17 9:23
 * @desc 收回执
 */
public class Send_Receipt {
    private static final long serialVersionUID = 1670857753595889875L;
    private int id;//id
    private String receivingunit;//接收方单位名称
    private String receivenumber;//接收方号码
    private Date create_time;//发送时间
    private String sendline;//标题
    private String message;//是否已经成功
    private String sendnumber;//发送方号码

    @Override
    public String toString() {
        return "Send_Receipt{" +
                "id=" + id +
                ", receivingunit='" + receivingunit + '\'' +
                ", receivenumber='" + receivenumber + '\'' +
                ", create_time=" + create_time +
                ", sendline='" + sendline + '\'' +
                ", message='" + message + '\'' +
                ", sendnumber='" + sendnumber + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReceivingunit(String receivingunit) {
        this.receivingunit = receivingunit;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setSendline(String sendline) {
        this.sendline = sendline;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public static long getSerialVersionUID() {

        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public String getSendline() {
        return sendline;
    }

    public String getMessage() {
        return message;
    }

    public String getSendnumber() {
        return sendnumber;
    }
}
