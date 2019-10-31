package com.hx.modle;

import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/10/17 9:42
 * @desc
 */
public class Return_Receipt {
    private static final long serialVersionUID = 1670857753595889875L;
    private int id;//id
    private String senderunit;//发送方单位名称
    private int sendnumber;//接收号码
    private Date create_time;;//接收时间


    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Return_Receipt{" +
                "id=" + id +
                ", senderunit='" + senderunit + '\'' +
                ", sendnumber=" + sendnumber +
                ", create_time=" + create_time +
                '}';
    }

    public void setSenderunit(String senderunit) {
        this.senderunit = senderunit;
    }

    public void setSendnumber(int sendnumber) {
        this.sendnumber = sendnumber;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public static long getSerialVersionUID() {

        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public String getSenderunit() {
        return senderunit;
    }

    public int getSendnumber() {
        return sendnumber;
    }

    public Date getCreate_time() {
        return create_time;
    }
}
