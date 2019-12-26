package com.hx.modle;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/10/17 9:23
 * @desc 收回执
 */
public class Send_Receipt extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = 1670857753595889875L;
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;//id
    @ExcelProperty(value = "接收单位", index = 1)
    private String receivingunit;//接收方单位名称
    @ExcelProperty(value = "接收号码", index = 2)
    private String receivenumber;//接收方号码
    @ExcelProperty(value = "发送时间", index = 3)
    private Date create_time;//发送时间
    @ExcelProperty(value = "文件标题", index = 4)
    private String sendline;//标题
    @ExcelProperty(value = "发送结果", index = 5)
    private String message;//是否已经成功
    @ExcelProperty(value = "发送号码", index = 6)
    private String sendnumber;//发送方号码
    @ExcelProperty(value = "是否已关联", index = 7)
    private Integer isLink;//是否已关联
    @ExcelProperty(value = "文件保存路径", index = 8)
    private String filsavepath;//关联的正文原件路径
    @ExcelProperty(value = "条形码", index = 9)
    private String barCode;
    @ExcelProperty(value = "是否已删除", index = 10)
    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public void setReceivingunit(String receivingunit) {
        this.receivingunit = receivingunit;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getSendline() {
        return sendline;
    }

    public void setSendline(String sendline) {
        this.sendline = sendline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public Integer getIsLink() {
        return isLink;
    }

    public void setIsLink(Integer isLink) {
        this.isLink = isLink;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

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
                ", isLink=" + isLink +
                ", filsavepath='" + filsavepath + '\'' +
                ", barCode='" + barCode + '\'' +
                ", state=" + state +
                '}';
    }
}



