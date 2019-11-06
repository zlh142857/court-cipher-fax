package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/6 18:01
 *@功能:
 */


import java.io.Serializable;
import java.util.Date;

public class Log_Tables implements Serializable {
    private static final long serialVersionUID = -4250652232417713719L;
    private Integer id;
    private String classname;
    private String methodname;
    private String message;
    private Date createtime;
    private String level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Log_Tables{" +
                "id=" + id +
                ", classname='" + classname + '\'' +
                ", methodname='" + methodname + '\'' +
                ", message='" + message + '\'' +
                ", createtime=" + createtime +
                ", level='" + level + '\'' +
                '}';
    }
}
