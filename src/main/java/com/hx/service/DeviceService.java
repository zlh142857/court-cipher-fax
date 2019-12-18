package com.hx.service;/*
 */

import com.hx.modle.Device_Setting;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    List<Device_Setting> selectDevice();

    boolean updateDevice(Device_Setting deviceSetting);

    Map<String,Object> selectDeviceById(Integer id) throws Exception;

    String checkChState(Integer ch) throws Exception;
}
