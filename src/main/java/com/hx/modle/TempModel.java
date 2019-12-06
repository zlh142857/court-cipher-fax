package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/30 9:56
 *@功能:
 */

import java.io.Serializable;

public class TempModel implements Serializable {
    private static final long serialVersionUID = -922290095790271279L;
    private String typename;
    private String linknumber;
    private String tifPath;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getLinknumber() {
        return linknumber;
    }

    public void setLinknumber(String linknumber) {
        this.linknumber = linknumber;
    }

    public String getTifPath() {
        return tifPath;
    }

    public void setTifPath(String tifPath) {
        this.tifPath = tifPath;
    }

    @Override
    public String toString() {
        return "TempModel{" +
                "typename='" + typename + '\'' +
                ", linknumber='" + linknumber + '\'' +
                ", tifPath='" + tifPath + '\'' +
                '}';
    }
}
