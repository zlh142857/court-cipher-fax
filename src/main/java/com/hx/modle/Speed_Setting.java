package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/9 10:24
 *@功能:
 */

import java.io.Serializable;

public class Speed_Setting implements Serializable {
    private static final long serialVersionUID = 7951493998595071669L;
    private int id;
    private int speed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Speed_Setting{" +
                "id=" + id +
                ", speed='" + speed + '\'' +
                '}';
    }
}
