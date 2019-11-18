package com.hx.controller;


import com.hx.modle.Return_Receipt;
import com.hx.service.ReturnReceiptService;
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
 * @date 2019/10/16 17:20
 * @desc
 */
@Controller
@RequestMapping("/ReturnReceipt")
public class ReturnReceiptController {
    @Resource
    private ReturnReceiptService returnReceiptService;
    private static Logger log = Logger.getLogger(Controller.class);// 日志文件

    //收回执查询
    @RequestMapping(value = "/ReturnReceiptlist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> inboxs(Integer pageNo, Integer pageSize, String senderunit,
                                      String receivenumber, String sendnumber, String beginDate , String endDate) {
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate=beginDate.trim();  //2019-12-01 12:00:00
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate=endDate.trim();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        Map<String,Object> searchMap=new HashMap();
        searchMap.put("senderunit",senderunit);
        searchMap.put("receivenumber",receivenumber);
        searchMap.put("sendnumber",sendnumber);
        searchMap.put("beginDate",beginDate);
        searchMap.put("endDate",endDate);
        int count = returnReceiptService.queryTotalCount(searchMap);

        if ( count > 0 ) {
            List<Return_Receipt> mails = returnReceiptService.queryALLMail(searchMap, pageNo, pageSize);
            result.put("mails", mails);
            result.put("totalCount", count);
        }
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }
    @RequestMapping(value = "/ReturnReceiptLists", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> mailLists() {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 查询全部
        List<Return_Receipt> mailLists = returnReceiptService.queryALLMailList();
        result.put("mailLists", mailLists); //
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }

    //TODO 删除记录
    @RequestMapping(value = "/delReturnReceipt", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delinbox(String str) {

        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( null == str || "".equals(str.trim()) ) {
            result.put("msg", "参数错误");
            log.error("参数错误"+str);
            return result;
        }
        String[] split = str.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                returnReceiptService.deinbox(Integer.parseInt(split[i]));
            }
            log.info("删除成功");
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
        }
        return result;

    }
}


