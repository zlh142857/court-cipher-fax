package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/23 9:20
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.service.SendFaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;



@Controller
public class SendFaxController {
    @Autowired
    private SendFaxService sendFaxService;
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
        List<String> numbers=sendFaxService.selectSeatNumber();
        System.out.println(numbers);
        return JSONObject.toJSONString( numbers );
    }
    /**
     *
     * 功能描述: 发送传真
     *
     * 业务逻辑:先呼叫对方号码,然后才能调用传真函数
     * 
     * @param: 正文文件路径,回执页base64,收文单位,收文号码,指定发送号码,是否包含回执页 isBack 0包含回执页,1不包含回执页,2只有回执页
     * @return: 
     * @auther: 张立恒
     * @date: 2019/10/8 17:13
     */
    @RequestMapping("sendFax")
    @ResponseBody
    public String sendFax(@RequestParam("tifPath") String tifPath,@RequestParam("base64") String base64,
                          @RequestParam("courtName") String courtName,@RequestParam("receiveNumber") String receiveNumber,
                          @RequestParam("sendNumber") String sendNumber,@RequestParam("isBack") int isBack,@RequestParam("ch") int ch){
        String mes=sendFaxService.sendFax(tifPath,base64,courtName,receiveNumber,sendNumber,isBack,ch);
        return mes;
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
    /**
     *
     * 功能描述: 删除发送方的两个文件
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/10/30 15:00
     */
    @RequestMapping("deleteFileByPath")
    @ResponseBody
    public void deleteFileByPath(@RequestParam("receiptPath") String receiptPath,@RequestParam("tifPath") String tifPath){
        File file=new File(receiptPath);
        if(file.isFile()){
            file.delete();
        }
        File file2=new File(tifPath);
        if(file2.isFile()){
            file2.delete();
        }
    }



}
