package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/23 9:20
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hx.dao.DeviceDao;
import com.hx.dao.SendReceiptMapper;
import com.hx.modle.ChMsg;
import com.hx.modle.Device_Setting;
import com.hx.modle.TempModel;
import com.hx.service.SendFaxService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SendFaxController {
    private static Logger logger=Logger.getLogger(SendFaxController.class);
    @Autowired
    private SendFaxService sendFaxService;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private SendReceiptMapper sendReceiptMapper;
    /**
     *
     * 功能描述: 发送前,自动选择线路发送还是指定号码发送,下拉列表框查询,从device_setting表中查询
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/10/24 14:54
     */
    @RequestMapping("selectSeatNumber")
    @ResponseBody
    public String selectSeatNumber(){
        List<Device_Setting> numbers=sendFaxService.selectSeatNumber();
        return JSONObject.toJSONString( numbers );
    }
    /**
     *
     * 功能描述: 发送传真
     *
     * 业务逻辑:先呼叫对方号码,然后才能调用传真函数
     * 
     * @param: 正文文件路径,回执页base64,收文单位,收文号码,指定发送号码,是否包含回执页 isBack 0包含回执页,1不包含回执页,2只有回执页,文件名称
     * @return: 
     * @auther: 张立恒
     * @date: 2019/10/8 17:13
     */
    @RequestMapping("sendFax")
    @ResponseBody
    public String sendFax(String tifPath,String receiptPath,String tempModels,String sendNumber,
                          String isBack,String ch,String filename,String id){
        String mes="";
        ObjectMapper mapper = new ObjectMapper();
        JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, TempModel.class);
        try {
            List<TempModel> tempList =  (List<TempModel>)mapper.readValue(tempModels, jt);
            for(int i=0;i<tempList.size();i++){
                mes=sendFaxService.sendFax(tifPath,receiptPath,tempList.get( i ).getCourtName(),tempList.get(i).getReceiveNumber(),sendNumber,isBack,filename,id,ch);
            }
        } catch (Exception e) {
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( mes );
    }
    /**
     *
     * 功能描述: base转换为base64
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/10/29 13:50
     */
    @RequestMapping("baseToTif")
    @ResponseBody
    public String baseToTif(@RequestParam("base64") String base64){
        String receiptPath=sendFaxService.baseToTif(base64);
        return JSONObject.toJSONString( receiptPath );
    }
    @RequestMapping("createBarCode")
    @ResponseBody
    public String createBarCode(){
        String file=null;
        try {
            file=sendFaxService.createBarCode();
        } catch (IOException e) {
            logger.error( e );
        }
        return JSONObject.toJSONString( file );
    }
    @RequestMapping("main")
    @ResponseBody
    public void main(String []args){
        int done=22;
        int allByte=100;
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
// 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float)done/(float)allByte*100);
        float num= Float.parseFloat( result );
        System.out.println(num);
    }
    @RequestMapping("rateOfAdvance")
    @ResponseBody
    public synchronized String rateOfAdvance(String ch){
        List<ChMsg> list=new ArrayList<>(  );
        try {
            list=sendFaxService.rateOfAdvance(ch);
        } catch (Exception e) {
            logger.error( e );
        }
        return JSONObject.toJSONString( list );
    }
    @RequestMapping("selectChAndSeatNumber")
    @ResponseBody
    public String selectChAndSeatNumber(){
        List<Device_Setting> list=new ArrayList<>(  );
        try {
            list=sendFaxService.selectChAndSeatNumber();
        } catch (Exception e) {
            logger.error( e );
        }
        return JSONObject.toJSONString( list );
    }
}
