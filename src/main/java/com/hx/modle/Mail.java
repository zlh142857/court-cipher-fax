package com.hx.modle;


import java.io.Serializable;

/**
 * @author 范聪敏
 * @date 2019/9/26 15:14
 * @desc
 */

public class Mail extends Mail_List implements Serializable  {
    private static final long serialVersionUID = -6869552118543251831L;
    private int id;
    private String linknumber;
    private String typeid;
    private String typename;
    private String telNotify;
    private String phone;
    private String e_mail;
    private String wor;
    private String address;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getWor() {
        return wor;
    }

    public void setWor(String wor) {
        this.wor = wor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNotify() {
        return telNotify;
    }

    public void setTelNotify(String telNotify) {
        this.telNotify = telNotify;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getLinknumber() {
        return linknumber;
    }

    public void setLinknumber(String linknumber) {
        this.linknumber = linknumber;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    @Override
    public String getTypename() {
        return typename;
    }

    @Override
    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "id=" + id +
                ", linknumber='" + linknumber + '\'' +
                ", typeid='" + typeid + '\'' +
                ", typename='" + typename + '\'' +
                ", telNotify='" + telNotify + '\'' +
                ", phone='" + phone + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", wor='" + wor + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
