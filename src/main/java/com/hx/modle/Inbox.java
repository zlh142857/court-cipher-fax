package com.hx.modle;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/9/4 15:40
 * @desc
 */
public class Inbox extends BaseRowModel implements Serializable  {
    private static final long serialVersionUID = 1670857753595889875L;
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;//id
    @ExcelProperty(value = "发送方号码",index = 1)
    private String sendnumber;//发送方号码
    @ExcelProperty(value = "发送方单位",index = 2)
    private String senderunit;//发送方单位
    @ExcelProperty(value = "接收号码",index = 3)
    private String receivenumber;//接收号码
    @ExcelProperty(value = "传真文件保存路径",index = 4)
    private String filsavepath;//文件保存路径
    @ExcelProperty(value = "接收时间",index = 5)
    private Date create_time;//接收时间
    @ExcelProperty(value = "是否已回执",index = 6)
    private Integer isreceipt;//是否已回执
    @ExcelProperty(value = "条形码",index = 7)
    private String barCode;//条形码的值
    @ExcelProperty(value = "是否已关联",index = 8)
    private Integer isLink;//是否已关联
    @ExcelProperty(value = "是否已删除",index = 9)
    private Integer state;
    @ExcelProperty(value = "是否已签收",index = 10)
    private Integer isSign;
    @ExcelProperty(value = "页码",index = 11)
    private Integer pageNum;//文件页数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public String getSenderunit() {
        return senderunit;
    }

    public void setSenderunit(String senderunit) {
        this.senderunit = senderunit;
    }

    public String getReceivenumber() {
        return receivenumber;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public String getFilsavepath() {
        return filsavepath;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getIsreceipt() {
        return isreceipt;
    }

    public void setIsreceipt(Integer isreceipt) {
        this.isreceipt = isreceipt;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getIsLink() {
        return isLink;
    }

    public void setIsLink(Integer isLink) {
        this.isLink = isLink;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsSign() {
        return isSign;
    }

    public void setIsSign(Integer isSign) {
        this.isSign = isSign;
    }

    @Override
    public String toString() {
        return "Inbox{" +
                "id=" + id +
                ", sendnumber='" + sendnumber + '\'' +
                ", senderunit='" + senderunit + '\'' +
                ", receivenumber='" + receivenumber + '\'' +
                ", filsavepath='" + filsavepath + '\'' +
                ", create_time=" + create_time +
                ", isreceipt=" + isreceipt +
                ", barCode='" + barCode + '\'' +
                ", isLink=" + isLink +
                ", pageNum=" + pageNum +
                ", state=" + state +
                ", isSign=" + isSign +
                '}';
    }
}