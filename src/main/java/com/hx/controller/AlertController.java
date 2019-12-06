package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/6 17:14
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.modle.WebModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
public class AlertController {
    private static Logger logger=Logger.getLogger(AlertController.class);
    public static WebModel webModels=null;
    public static WebModel sendModels=null;
    public static WebModel inboxModels=null;
    public static WebModel outboxModels=null;
    public static int webModelCount=0;
    public static int inboxCount=0;
    public static int outboxCount=0;
    public static int sendCount=0;
    @RequestMapping("webSocket")
    @ResponseBody
    public String returnTest(){
        List<WebModel> nullList=new ArrayList<>(  );
        String msg="";
        try{
            if(webModelCount==1){
                nullList.add( webModels );
                webModelCount=0;
            }
            if(inboxCount==1){
                nullList.add( inboxModels );
                inboxCount=0;
            }
            if(outboxCount==1){
                nullList.add( outboxModels );
                outboxCount=0;
            }
            if(sendCount==1){
                nullList.add( sendModels );
                sendCount=0;
            }
        }catch (Exception e){
            logger.error( e.toString() );
        }
        return JSONObject.toJSONStringWithDateFormat( nullList,"yyyy-MM-dd HH:mm:ss" );
    }
}
