package com.hx.dao;/*
 */

import com.hx.modle.Device_Setting;

import java.util.List;

public interface DeviceDao {
    List<Device_Setting> selectSeatNumber();

    int selectChType(int ch);

    String selectSeatNumberByCh(int i);

    Device_Setting selectPrefix(String seatNumber);

    String selectNumberByCh(int ch);

    List<Device_Setting> selectDevice();

    int updateDevice(Device_Setting deviceSetting);

    Device_Setting selectPrefixByCh(int ch);

    Device_Setting selectDeviceById(Integer id)throws Exception;

    Device_Setting selectSpeedIdByCh(int ch);

    List<Integer> selectCh();
}
