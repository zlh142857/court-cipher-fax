package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/31 16:56
 *@功能:
 */

import com.hx.common.ChStateCode;
import com.hx.common.Fax;
import com.hx.dao.DeviceDao;
import com.hx.dao.SpeedDao;
import com.hx.modle.Device_Setting;
import com.hx.modle.Speed_Setting;
import com.hx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("DeviceService")
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private SpeedDao speedDao;
    @Override
    public List<Device_Setting> selectDevice() {
        List<Device_Setting> list=deviceDao.selectDevice();
        for(Device_Setting device_setting:list){
            device_setting.setEditFlag( false );
        }
        return list;
    }

    @Override
    public boolean updateDevice(Device_Setting deviceSetting) {
        int count=deviceDao.updateDevice(deviceSetting);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Map<String, Object> selectDeviceById(Integer id) throws Exception {
        Map<String,Object> map=new HashMap<>(  );
        Device_Setting device_setting=deviceDao.selectDeviceById(id);
        //查询所有的速率信息,下拉列表框
        List<Speed_Setting> speedSettings=speedDao.selectSpeed();
        for(Speed_Setting speed_setting:speedSettings){
            if(speed_setting.getId()==device_setting.getSendSpeedId()){
                device_setting.setSendSpeed( speed_setting.getSpeed() );
            }
            if(speed_setting.getId()==device_setting.getReceiveSpeedId()){
                device_setting.setReceiveSpeed( speed_setting.getSpeed() );
            }
        }
        device_setting.setEditFlag( false );
        map.put( "speedList",speedSettings );
        map.put( "device",device_setting );
        return map;
    }

    @Override
    public String checkChState(Integer ch) throws Exception{
        String msg="";
        int voltage=Fax.INSTANCE.SsmGetLineVoltage(ch);
        if(voltage<=10 && voltage>=0){
            msg="未插电话线";
        }else if(voltage==-1){
            msg="检测失败";
        }else{
            int state=Fax.INSTANCE.SsmGetChState(ch);
            msg=ChStateCode.checkChCode(state);
        }
        return msg;
    }
}
