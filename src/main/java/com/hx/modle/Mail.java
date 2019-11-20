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
                '}';
    }
}
