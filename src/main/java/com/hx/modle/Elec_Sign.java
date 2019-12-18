package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/11 16:45
 *@功能:
 */

import java.io.Serializable;
import java.util.Date;

public class Elec_Sign implements Serializable {
    private static final long serialVersionUID = -9095082882141920287L;
    private int id;
    private String signName;
    private String savePath;
    private Date create_time;
    private String signType;
    private String jpgPath;

    public String getJpgPath() {
        return jpgPath;
    }

    public void setJpgPath(String jpgPath) {
        this.jpgPath = jpgPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public String toString() {
        return "Elec_Sign{" +
                "id=" + id +
                ", signName='" + signName + '\'' +
                ", savePath='" + savePath + '\'' +
                ", create_time=" + create_time +
                ", signType='" + signType + '\'' +
                ", jpgPath='" + jpgPath + '\'' +
                '}';
    }
}
