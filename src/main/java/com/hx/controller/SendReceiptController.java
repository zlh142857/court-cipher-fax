package com.hx.controller;

import com.alibaba.fastjson.JSONObject;
import com.hx.modle.Send_Receipt;
import com.hx.service.SendReceiptService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/31 15:39
 * @desc
 */
@Controller
@RequestMapping("/SendReceipt")
public class SendReceiptController {
    @Resource
    private SendReceiptService sendReceiptService;
    private static Logger log = Logger.getLogger(Controller.class);// 日志文件

    //收件箱查询
    @RequestMapping(value = "/queryReturnReceipt", method = RequestMethod.GET)
    @ResponseBody
    public String inboxs(Integer pageNo, Integer pageSize, String receivingunit, String sendline, String message, String sendernumber,String beginDate ,String endDate) {
        //mailListId="m";
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate=beginDate.trim();
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate=endDate.trim();
        }
        Map<String,Object> searchMap=new HashMap();
        searchMap.put("receivingunit",receivingunit);
        searchMap.put("beginDate",beginDate);
        searchMap.put("endDate",endDate);
        searchMap.put("sendline",sendline);
        searchMap.put("message",message);
        searchMap.put("sendnumber",sendernumber);
        int count = sendReceiptService.queryTotalCount(searchMap);
        if ( count > 0 ) {
            List<Send_Receipt> mails = sendReceiptService.queryALLMail(searchMap, pageNo, pageSize);
            result.put("mails", mails);
            result.put("totalCount", count);
        }

        result.put("state", 1); //0代表失败，1代表成功
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss");
    }
    @RequestMapping(value = "/ReturnReceipts", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> mailLists() {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        //TODO 查询全部
        List<Send_Receipt> mailLists = sendReceiptService.queryALLMailList();
        result.put("mailLists", mailLists); //

        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }

    //TODO 删除记录
    @RequestMapping(value = "/deleSendReceipt", method = RequestMethod.POST)
    @ResponseBody

    public Map<String, Object> delinbox(String str) {

        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        if ( null == str || "".equals(str.trim()) ) {
            result.put("msg", "参数错误");
            return result;
        }
        String[] split = str.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                sendReceiptService.deinbox(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
        }
        return result;

    }
    /**
     *
     * 功能描述: 点击关联回执的按钮,然后弹框,查询未被关联的发回执箱信息
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/13 9:51
     */
    @RequestMapping("/selectReceiptNoLink")
    @ResponseBody
    public String selectReceiptNoLink(String pageNow,String pageSize){
        Map<String,Object> map=null;
        try {
            if(pageNow!=null||pageSize!=null){
                map=sendReceiptService.selectReceiptNoLink(Integer.valueOf( pageNow ),Integer.valueOf( pageSize ));
            }else{
                log.error( "分页参数为空" );
            }
        } catch (Exception e) {
            log.error( e );
        }
        return JSONObject.toJSONStringWithDateFormat( map,"yyyy-MM-dd HH:mm:ss" );
    }
    /**
     *
     * 功能描述: 更改收件箱和
     * 发回执箱的是否已关联状态,改为1
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/11/13 10:01
     */
    @RequestMapping("/updateIsLink")
    @ResponseBody
    public boolean updateIsLink(String intBoxId,String receiptId){
        boolean flag=false;
        try {
            if(intBoxId != null || receiptId != null && intBoxId != "" || receiptId != ""){
                flag=sendReceiptService.updateIsLink(intBoxId,receiptId);
            }
        } catch (Exception e) {
            log.error( e );
        }
        return flag;
    }
    /**
     *
     * 功能描述: 查看关联原件
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/13 10:55
     */
    @RequestMapping("/checkText")
    @ResponseBody
    public String checkText(String tifPath){
        String base64="";
        try {
            if(tifPath != null || tifPath != ""){
                base64=sendReceiptService.checkText(tifPath);
            }
        } catch (Exception e) {
            log.error( e );
        }
        return JSONObject.toJSONString( base64 );
    }

}
