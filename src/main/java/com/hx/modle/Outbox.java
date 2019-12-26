package com.hx.modle;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/9/2 16:53
 * @desc 已发邮件
 */
public class Outbox extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = -293973874820213180L;
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;//id
    @ExcelProperty(value = "发送方号码",index = 1)
    private String sendnumber;//发送号码
    @ExcelProperty(value = "接收号码",index = 2)
    private String receivenumber;//接收号码
    @ExcelProperty(value = "接收单位",index = 3)
    private String receivingunit;//接收单位
    @ExcelProperty(value = "发送时间",index = 4)
    private Date create_time;//发送时间
    @ExcelProperty(value = "文件标题",index = 5)
    private String sendline;//文件标题
    @ExcelProperty(value = "发送结果",index = 6)
    private String message;
    @ExcelProperty(value = "文件保存路径",index = 7)
    private String filsavepath;
    @ExcelProperty(value = "是否已删除",index = 8)
    private Integer state;
    @ExcelProperty(value = "是否发送成功标识",index = 9)
    private boolean isSuccess;
    @ExcelProperty(value = "电话通话结果",index = 10)
    private String telResult;//电话通知结果
    @ExcelProperty(value = "页码",index = 11)
    private Integer pageNum;//文件页数

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getTelResult() {
        return telResult;
    }

    public void setTelResult(String telResult) {
        this.telResult = telResult;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

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

    public String getReceivenumber() {
        return receivenumber;
    }

    public void setReceivenumber(String receivenumber) {
        this.receivenumber = receivenumber;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public void setReceivingunit(String receivingunit) {
        this.receivingunit = receivingunit;
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

    public String getFilsavepath() {
        return filsavepath;
    }

    public void setFilsavepath(String filsavepath) {
        this.filsavepath = filsavepath;
    }

    @Override
    public String toString() {
        return "Outbox{" +
                "id=" + id +
                ", sendnumber='" + sendnumber + '\'' +
                ", receivenumber='" + receivenumber + '\'' +
                ", receivingunit='" + receivingunit + '\'' +
                ", create_time=" + create_time +
                ", sendline='" + sendline + '\'' +
                ", message='" + message + '\'' +
                ", filsavepath='" + filsavepath + '\'' +
                ", isSuccess=" + isSuccess +
                ", telResult='" + telResult + '\'' +
                ", pageNum=" + pageNum +
                ", state=" + state +
                '}';
    }
}
