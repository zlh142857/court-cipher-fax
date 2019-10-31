package com.hx.modle;


import java.io.Serializable;

/**
 * @author 范聪敏
 * @date 2019/9/2 17:20
 * @desc
 */
public class Mail_List implements Serializable {
    private static final long serialVersionUID = 6531881783788175228L;
    private int id;//id
    private String typename;//类名

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Override
    public String toString() {
        return "Mail_List{" +
                "id=" + id +
                ", typename='" + typename + '\'' +
                '}';
    }
}
