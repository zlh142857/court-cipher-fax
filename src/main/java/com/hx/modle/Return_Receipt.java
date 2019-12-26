package com.hx.modle;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/10/17 9:42
 * @desc 收回执
 */
public class Return_Receipt extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = 1670857753595889875L;
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;//id
    @ExcelProperty(value = "发送方单位", index = 1)
    private String senderunit;//发送方单位名称
    @ExcelProperty(value = "接收号码", index = 2)
    private String receivenumber;
    @ExcelProperty(value = "发送号码", index = 3)
    private String sendnumber;
    @ExcelProperty(value = "接收时间", index = 4)
    private Date create_time;;//接收时间
    @ExcelProperty(value = "文件保存路径", index = 5)
    private String filsavepath;//文件保存路径
    @ExcelProperty(value = "是否已删除", index = 6)
    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
                ", state=" + state +
                '}';
    }
}
