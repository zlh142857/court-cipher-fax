package com.hx.modle;


import java.io.Serializable;

/**
 * @author 范聪敏
 * @date 2019/9/26 15:14
 * @desc
 */

public class Mail extends Mail_List implements Serializable  {

    private int id;
    private String linkname;
    private String linknumber;
    private String typeid;

    @Override
    public String toString() {
        return "Mail{" +
                "id=" + id +
                ", linkname='" + linkname + '\'' +
                ", linknumber='" + linknumber + '\'' +
                ", typeid=" + typeid +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLinkname(String linkname) {
        this.linkname = linkname;
    }

    public void setLinknumber(String linknumber) {
        this.linknumber = linknumber;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public int getId() {
        return id;
    }

    public String getLinkname() {
        return linkname;
    }

    public String getLinknumber() {
        return linknumber;
    }

    public String getTypeid() {
        return typeid;
    }
}
