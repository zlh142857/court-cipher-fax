package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/11 10:33
 *@功能:
 */

import java.io.Serializable;

public class AgainSend implements Serializable {
    private static final long serialVersionUID = 4226084020236232896L;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "AgainSend{" +
                "imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
