package com.hx.controller;

import com.alibaba.fastjson.JSONObject;
import com.hx.modle.Inbox;
import com.hx.service.InBoxService;
import com.hx.util.ExcelHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 收件箱管理
 * @author 范聪敏
 * @date 2019/9/11 22:17
 * @desc
 */

@Controller
@RequestMapping("/inbox")
public class InBoxController {
    @Resource
    private InBoxService inBoxService;
    private static Logger log = Logger.getLogger(InBoxController.class);// 日志文件

    /**
     * 收件箱导出
     */
    @RequestMapping(value = "/export.do", produces = "application/form-data; charset=utf-8")
    public void OutputExcel2(String ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html,charset=utf-8");
        //查询需要导出的数据
        List<Inbox> list = inBoxService.getAll(ids.split(","));
        List<Object[]> data = new ArrayList<>();    //转换数据
        Iterator<Inbox> it = list.iterator();
        while (it.hasNext()) {
            Inbox m = it.next();
            data.add(new Object[]{m.getSendnumber(), m.getReceivenumber(), m.getReceivenumber(), m.getIsSign(), m.getPageNum()});
            // data.add(new Object[]{  m.getSendname(), m.getAccepttime(), m.getFileaddress()});
        }
        //构建Excel表头,此处需与data中数据一一对应
        List<String> headers = new ArrayList<String>();
        headers.add("发送方号码");
        headers.add("发送方单位");
        headers.add("接收号码");
        headers.add("是否已签收");
        headers.add("页数");

        ExcelHelper.exportExcel(headers, data, "收件箱", "xlsx", response);//downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码
    }

    //收件箱查询
    @RequestMapping(value = "/readinbox", method = RequestMethod.GET)
    @ResponseBody
    public String readinbox(Integer pageNo, Integer pageSize, String sendnumber, String senderunit, String receivenumber, String Isreceipt, String beginDate, String endDate) {
        //mailListId="m";
        Map<String, Object> result = new HashMap<>();
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate = beginDate.trim();
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate = endDate.trim();
        }
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("sendnumber", sendnumber);
        searchMap.put("senderunit", senderunit);
        searchMap.put("receivenumber", receivenumber);
        searchMap.put("Isreceipt", Isreceipt);
        searchMap.put("beginDate", beginDate);
        searchMap.put("endDate", endDate);
        int count = inBoxService.queryTotalCount(searchMap);
        List<Inbox> mails = null;
        if ( count > 0 ) {
            mails = inBoxService.readinboxALLMail(searchMap, pageNo, pageSize);
            result.put("totalCount", count);
            result.put("state", 1);
            result.put("mails", mails);
        } else {
            result.put("mails", new ArrayList<Inbox>());
            result.put("state", 0);
        }
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss" );
    }

    @RequestMapping(value = "/inboxList", method = RequestMethod.GET)
    @ResponseBody
    public String mailLists() {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 查询全部
        List<Inbox> mailLists = inBoxService.queryALLMailList();
        result.put("mailLists", mailLists); //
        result.put("state", 1); //0代表失败，1代表成功
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss" );
    }


    //TODO 更改签收
    @RequestMapping(value = "/Signforinbox", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> Signfornbox(Integer id) {
        Map<String, Object> result = new HashMap<>();
        if ( null == id ) {
            result.put("msg", "参数错误");
            return result;
        }
        try {
            System.out.println(id);
            int count=inBoxService.Signfornbox(id);
            if(count>0){
                result.put("state", 1);
            }else{
                result.put("state", 0);
            }
        } catch (Exception e) {
            log.error(e.toString());
            result.put("msg", "操作失败");
        }
        return result;
    }

    //TODO 回收站显示
    @RequestMapping(value = "/recoveryinbox", method = RequestMethod.GET)
    @ResponseBody
    public String recoveryinbox(Integer pageNo, Integer pageSize, String sendnumber, String senderunit, String receivenumber, String Isreceipt, String beginDate, String endDate) {
        //mailListId="m";
        Map<String, Object> result = new HashMap<>();
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate = beginDate.trim();
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate = endDate.trim();
        }
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("sendnumber", sendnumber);
        searchMap.put("senderunit", senderunit);
        searchMap.put("receivenumber", receivenumber);
        searchMap.put("Isreceipt", Isreceipt);
        searchMap.put("beginDate", beginDate);
        searchMap.put("endDate", endDate);
        int count = inBoxService.queryTotalCount(searchMap);
        List<Inbox> mails = null;
        if ( count > 0 ) {
            mails = inBoxService.RecoveryInbox(searchMap, pageNo, pageSize);
            result.put("totalCount", count);
            result.put("state", 1);
            result.put("bs", 1);
            result.put("mails", mails);
        } else {
            result.put("mails", new ArrayList<Inbox>());
            result.put("state", 0);
        }
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss" );
    }

    //TODO 数据还原
    @RequestMapping(value = "/reductioninbox", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> reductioninbox(String ids, String bs) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 验证标题不能为空
        if ( null == ids || "".equals(ids) ) {
            result.put("msg", "参数错误");
            return result;
        }
        //TODO 更改状态
        boolean b = inBoxService.reductioninbox(ids, bs);
        if ( b ) {
            result.put("state", 1); //0代表失败，1代表成功
        }
        return result;
    }

    //TODO 回收站删除数据
    @RequestMapping(value = "/delinbox", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> deinbox(String ids, String bs) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( StringUtils.isEmpty(ids) ) {
            result.put("msg", "参数错误");
            return result;
        }
        boolean b = inBoxService.delinbox(ids, bs);
        if ( b ) {
            result.put("state", 1); //0代表失败，1代表成功
        }
        return result;
    }
    @RequestMapping(value = "/queryinbox", method = RequestMethod.GET)
    @ResponseBody
    public String inboxs(Integer pageNo, Integer pageSize, String sendnumber, String senderunit, String receivenumber, String Isreceipt, String beginDate, String endDate) {
        //mailListId="m";
        Map<String, Object> result = new HashMap<>();
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate = beginDate.trim();
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate = endDate.trim();
        }
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("sendnumber", sendnumber);
        searchMap.put("senderunit", senderunit);
        searchMap.put("receivenumber", receivenumber);
        searchMap.put("Isreceipt", Isreceipt);
        searchMap.put("beginDate", beginDate);
        searchMap.put("endDate", endDate);
        int count = inBoxService.queryTotalCountw(searchMap);
        List<Inbox> mails = null;
        if ( count > 0 ) {
            mails = inBoxService.queryALLMail(searchMap, pageNo, pageSize);
            result.put("totalCount", count);
            result.put("state", 1);
            result.put("mails", mails);
        } else {
            result.put("mails", new ArrayList<Inbox>());
            result.put("state", 0);
        }
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss" );
    }
    //TODO 更改状态
    @RequestMapping(value = "/modifyinbox", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> modifyinbox(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( null == id || "".equals(id.trim()) ) {
            result.put("msg", "参数错误");
            return result;
        }
        String[] split = id.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                inBoxService.modifinbox(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            log.error(e.toString());
            result.put("msg", "操作失败");
        }
        return result;
    }
}

