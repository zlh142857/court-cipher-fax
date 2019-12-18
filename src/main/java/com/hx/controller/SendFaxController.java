package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/23 9:20
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hx.modle.ChMsg;
import com.hx.modle.Device_Setting;
import com.hx.modle.TempModel;
import com.hx.service.SendFaxService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.*;


@Controller
public class SendFaxController {
    private static Logger logger=Logger.getLogger(SendFaxController.class);
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
        List<TempModel> tempList = null;
        try {
            tempList = (List<TempModel>)mapper.readValue(tempModels, jt);
            int size=tempList.size();
            for(int i=0;i<size;i++){
                mes=sendFaxService.sendFax(tifPath,receiptPath,tempList.get( i ).getTypename(),tempList.get(i).getLinknumber(),sendNumber,isBack,filename,id,ch);
                try {
                    Thread.sleep( 3000 );
                } catch (InterruptedException e) {
                    logger.error( e.toString() );
                }
            }
        } catch (IOException e) {
            logger.error( e.toString() );
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
    public String baseToTif(String base64){
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
    /**
     *
     * 功能描述: 新增定时任务
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/13 18:49
     */
    @RequestMapping("insertSchTask")
    @ResponseBody
    public boolean insertSchTask(String tifPath,String receiptPath,String tempModels,String sendNumber,
                                 String isBack,String ch,String filename,String id,String sendTime){
        boolean flag=false;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, TempModel.class);
            List<TempModel> tempList =  (List<TempModel>)mapper.readValue(tempModels, jt);
            flag=sendFaxService.insertSchTask(tifPath,receiptPath,tempList,sendNumber,isBack,filename,id,ch,sendTime);
        } catch (Exception e) {
            logger.error( e );
        }
        return flag;
    }
    @RequestMapping("deleteSchTask")
    @ResponseBody
    public boolean deleteSchTask(String id){
        boolean flag=false;
        try {
            if(null !=id){
                flag=sendFaxService.deleteSchTask(Integer.valueOf( id ));
            }else{
                logger.error( "id为空" );
            }
        } catch (Exception e) {
            logger.error( e );
        }
        return flag;
    }
    @RequestMapping("updateTaskDate")
    @ResponseBody
    public boolean updateTaskDate(String id,String date){
        boolean flag=false;
        try {
            flag=sendFaxService.updateTaskDate(id,date);
        } catch (Exception e) {
            logger.error( e );
        }
        return flag;
    }
    @RequestMapping("selectTaskLimit")
    @ResponseBody
    public String selectTaskLimit(String pageNow,String pageSize){
        Map<String,Object> map=null;
        try {
            if(null!=pageNow&&null!=pageSize){
                map=sendFaxService.selectTaskLimit(Integer.valueOf( pageNow ),Integer.valueOf( pageSize ));
            }else{
                logger.error( "分页参数为空" );
            }
        } catch (Exception e) {
            logger.error( e );
        }
        return JSONObject.toJSONStringWithDateFormat( map,"yyyy-MM-dd HH:mm:ss" );
    }
    /**
     *
     * 功能描述:取消发送
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/11/18 9:50
     */
    @RequestMapping("undoSend")
    @ResponseBody
    public boolean undoSend(String ch){
        boolean flag=false;
        try {
            if(null!=ch){
                flag=sendFaxService.undoSend(ch);
            }else{
                logger.error( "参数为空" );
            }
        } catch (Exception e) {
            logger.error( e );
        }
        return flag;
    }
    /**
     *
     * 功能描述: 右键发送回执,然后拆分文件,返回文件路径
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/22 10:54
     */
    @RequestMapping("returnFaxGetPath")
    @ResponseBody
    public String returnFaxGetPath(String tifPath){
        String path="";
        try {
            if(null!=tifPath){
                path=sendFaxService.returnFaxGetPath(tifPath);
            }else{
                logger.error( "参数为空" );
            }
        } catch (Exception e) {
            logger.error( e );
        }
        return JSONObject.toJSONString( path );
    }
    /**
     *
     * 功能描述: 转发,拆分其中几页,再合并成新的文件进行发送
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/12/10 14:36
     */
    @RequestMapping("bigToSmallTiff")
    @ResponseBody
    public String bigToSmallTiff(String tifPath,int[] pages){
        Map<String,Object> map=new HashMap<>(  );
        if(null==tifPath || "".equals( tifPath )){
            map.put( "message","路径参数为空" );
        }else if(null==pages || (pages!=null&&pages.length==0)){
            map.put( "message","参数为空" );
        }else{
            map=sendFaxService.bigToSmallTiff(tifPath,pages);
        }
        return JSONObject.toJSONString( map );
    }
    /**
     *
     * 功能描述: 手动拨打电话监控刚刚摘机拨打电话正处于通话中的通道编号
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/12/13 13:23
     */
    @RequestMapping("checkChByHand")
    @ResponseBody
    public String checkChByHand(){
        Map<String,Object> map=new HashMap<>(  );
        map=sendFaxService.checkChByHand();
        return JSONObject.toJSONString( map );
    }
    /**
     *
     * 功能描述:
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/12/13 13:55
     */
    @RequestMapping("sendFaxByHand")
    @ResponseBody
    public String sendFaxByHand(String tifPath,String receiptPath,String courtName,String receiveNumber,
                                Integer isBack,Integer ch,String filename,Integer id){
        String msg=sendFaxService.sendFaxByHand(tifPath,receiptPath,courtName,receiveNumber,isBack,ch,filename,id);
        return JSONObject.toJSONString( msg );
    }
    /**
     *
     * 功能描述:电话通知
     *
     * 业务逻辑:
     * 
     * @param:
     * @return: 
     * @auther: 张立恒
     * @date: 2019/12/17 9:47
     */
    @RequestMapping("telNotify")
    @ResponseBody
    public String telNotify(Integer id,String telNotify){
        String msg= null;
        if(null == telNotify || "".equals( telNotify )){
            msg="电话通知号码为空";
            return JSONObject.toJSONString( msg );
        }
        if(null == id){
            msg="id为空";
            return JSONObject.toJSONString( msg );
        }
        try {
            msg = sendFaxService.telNotify(id,telNotify);
        } catch (InterruptedException e) {
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( msg );
    }
    /**
     *
     * 功能描述: 电话通知前查询通讯簿的电话通知号码
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/12/17 9:53
     */
    @RequestMapping("selectNotifyPhone")
    @ResponseBody
    public String selectNotifyPhone(String receiveNumber){
        String msg= null;
        if(null == receiveNumber || "".equals( receiveNumber )){
            msg="发送方号码为空";
            return JSONObject.toJSONString( msg );
        }
        msg=sendFaxService.selectNotifyPhone(receiveNumber);
        if(null == msg){
            return null;
        }else{
            return JSONObject.toJSONString( msg );
        }
    }

}
