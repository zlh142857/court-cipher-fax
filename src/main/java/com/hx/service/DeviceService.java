package com.hx.service;/*
 */

import com.hx.modle.Device_Setting;

import java.util.List;

public interface DeviceService {
    List<Device_Setting> selectDevice();

    void updateDevice(Device_Setting deviceSetting);
}
