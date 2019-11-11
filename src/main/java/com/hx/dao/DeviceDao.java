package com.hx.dao;/*
 */

import com.hx.modle.Device_Setting;

import java.util.List;

public interface DeviceDao {
    List<Device_Setting> selectSeatNumber();

    List<Device_Setting> selectChType();

    String selectSeatNumberByCh(int i);

    Device_Setting selectPrefix(String seatNumber);

    String selectNumberByCh(int ch);

    List<Device_Setting> selectDevice();

    void updateDevice(Device_Setting deviceSetting);

    Device_Setting selectPrefixByCh(int ch);

    List<Integer> selectCh();
}
