package com.hx.modle;

import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/10/17 9:42
 * @desc 收回执
 */
public class Return_Receipt {
    private static final long serialVersionUID = 1670857753595889875L;
    private int id;//id
    private String receivenumber;//发送方号码
    private String senderunit;//发送方单位名称
    private String sendnumber;//接收号码
    private Date create_time;;//接收时间
    private String filsavepath;//文件保存路径

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public String getSenderunit() {
        return senderunit;
    }

    public void setSenderunit(String senderunit) {
        this.senderunit = senderunit;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    @Override
    public String toString() {
        return "Return_Receipt{" +
                "id=" + id +
                ", receivenumber='" + receivenumber + '\'' +
                ", senderunit='" + senderunit + '\'' +
                ", sendnumber='" + sendnumber + '\'' +
                ", create_time=" + create_time +
                ", filsavepath='" + filsavepath + '\'' +
                '}';
    }
}
