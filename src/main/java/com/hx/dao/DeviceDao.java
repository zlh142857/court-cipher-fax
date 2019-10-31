package com.hx.dao;/*
 */

import com.hx.modle.Device_Setting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceDao {
    List<String> selectSeatNumber();

    List<Device_Setting> selectChType();

    String selectSeatNumberByCh(@Param(value="i")int i);

    Device_Setting selectPrefix(@Param(value="sendNumber")String sendNumber);
}