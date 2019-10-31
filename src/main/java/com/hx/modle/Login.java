package com.hx.modle;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 范聪敏
 * @date 2019/9/2 15:57
 * @desc
 */
public class Login implements Serializable {
    private static final long serialVersionUID = -6827279844262298675L;
    private int id;//ID
    private String user;//用户名
    private String password;//用户密码
    private Date logintime;//登录时间


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }


    @Override
    public String toString() {
        return "    Login{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", logintime=" + logintime +
                '}';
    }
}
