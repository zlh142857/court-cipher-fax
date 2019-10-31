package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/31 16:53
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.modle.Device_Setting;
import com.hx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    /**
     *
     * 功能描述: 查询所有的设备信息
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/10/31 16:57
     */
    @RequestMapping("selectDevice")
    @ResponseBody
    public String selectDevice(){
        Map<String,Object> map=new HashMap<>(  );
        List<Device_Setting> list=deviceService.selectDevice();
        map.put( "boolean",false );
        map.put( "list",list );
        return JSONObject.toJSONString(map);
    }
    /**
     *
     * 功能描述: 修改或新增设备信息
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/10/31 17:31
     */
    @RequestMapping("updateDevice")
    @ResponseBody
    public void updateDevice(@RequestParam("DeviceSetting")Device_Setting deviceSetting){
        deviceService.updateDevice(deviceSetting);
    }
}
