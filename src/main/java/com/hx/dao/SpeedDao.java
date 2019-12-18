package com.hx.dao;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/9 10:31
 *@功能:
 */

import com.hx.modle.Speed_Setting;

import java.util.List;

public interface SpeedDao {
    List<Speed_Setting> selectSpeed();

    int selectSpeedById(int speedId);
}
