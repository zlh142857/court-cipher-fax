package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/31 16:56
 *@功能:
 */

import com.hx.dao.DeviceDao;
import com.hx.modle.Device_Setting;
import com.hx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DeviceService")
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceDao deviceDao;
    @Override
    public List<Device_Setting> selectDevice() {
        List<Device_Setting> list=deviceDao.selectDevice();
        for(Device_Setting device_setting:list){
            device_setting.setEditFlag( false );
        }
        return list;
    }

    @Override
    public void updateDevice(Device_Setting deviceSetting) {
        deviceDao.updateDevice(deviceSetting);
    }
}
