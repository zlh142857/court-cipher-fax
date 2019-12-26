package com.hx.controller;
import com.alibaba.fastjson.JSONObject;
import com.hx.modle.Return_Receipt;
import com.hx.service.ReturnReceiptService;
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
 * @author 范聪敏
 * @date 2019/10/16 17:20
 * @desc
 */

@Controller
@RequestMapping("/ReturnReceipt")
public class ReturnReceiptController {
    @Resource
    private ReturnReceiptService returnReceiptService;
    private static Logger log = Logger.getLogger(ReturnReceiptController.class);// 日志文件

    //收回执查询
    @RequestMapping(value = "/ReturnReceiptlist", method = RequestMethod.GET)
    @ResponseBody
    public String inboxs(Integer pageNo, Integer pageSize, String senderunit,
                                      String receivenumber, String sendnumber, String beginDate, String endDate) {
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate = beginDate.trim();  //2019-12-01 12:00:00
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate = endDate.trim();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("senderunit", senderunit);
        searchMap.put("receivenumber", receivenumber);
        searchMap.put("sendnumber", sendnumber);
        searchMap.put("beginDate", beginDate);
        searchMap.put("endDate", endDate);
        int count = returnReceiptService.queryTotalCount(searchMap);
        List<Return_Receipt> mails = null;
        if ( count > 0 ) {
            mails = returnReceiptService.queryALLMail(searchMap, pageNo, pageSize);
            result.put("totalCount", count);
            result.put("state", 1);
            result.put("mails", mails);
        } else {
            result.put("mails", new ArrayList<Return_Receipt>());
            result.put("state", 0);
        }
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss" );
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

    @RequestMapping(value = "/ReturnReceiptpoi.do", produces = "application/form-data; charset=utf-8")
    public void ReturnReceiptpoi(String ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html,charset=utf-8");
        //查询需要导出的数据
        List<Return_Receipt> list = returnReceiptService.getAll(ids.split(","));
        List<Object[]> data = new ArrayList<>();    //转换数据
        Iterator<Return_Receipt> it = list.iterator();
        while (it.hasNext()) {
            Return_Receipt m = it.next();
            data.add(new Object[]{m.getSendnumber(), m.getReceivenumber(), m.getSenderunit()});
        }
        //构建Excel表头,此处需与data中数据一一对应
        List<String> headers = new ArrayList<String>();
        headers.add("接收号码");
        headers.add("发送号码");
        headers.add("发送方单位");
        ExcelHelper.exportExcel(headers, data, "收回执", "xlsx", response);       //downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码;
    }

    //TODO 更改状态
    @RequestMapping(value = "/modifyReturnReceipt", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> modifyinbox(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 验证标题不能为空
        if ( null == id || "".equals(id) ) {
            result.put("msg", "参数错误");
            return result;
        }
        //TODO 更改状态
        String[] split = id.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                returnReceiptService.modifReturnReceipt(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            log.error(e.toString());
            result.put("msg", "删除失败");
        }
        return result;
    }

    //TODO 回收站显示
    @RequestMapping(value = "/recoveryReturnReceipt", method = RequestMethod.GET)
    @ResponseBody
    public String recoveryReturnReceipt(Integer pageNo, Integer pageSize, String senderunit,
                                                     String receivenumber, String sendnumber, String beginDate, String endDate) {
        //mailListId="m";
        Map<String, Object> result = new HashMap<>();
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate = beginDate.trim();
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate = endDate.trim();
        }
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("senderunit", senderunit);
        searchMap.put("receivenumber", receivenumber);
        searchMap.put("sendnumber", sendnumber);
        searchMap.put("beginDate", beginDate);
        searchMap.put("endDate", endDate);
        int count = returnReceiptService.queryTotalCount(searchMap);
        List<Return_Receipt> mails = null;
        if ( count > 0 ) {
            mails = returnReceiptService.RecoveryReturnReceipt(searchMap, pageNo, pageSize);
            result.put("totalCount", count);
            result.put("state", 1);
            result.put("bs", 3);
            result.put("mails", mails);
        } else {
            result.put("mails", new ArrayList<Return_Receipt>());
            result.put("state", 0);
        }
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss" );
    }

    //TODO 数据还原
    @RequestMapping(value = "/reductionReturnReceipt", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> reductionReturnReceipt(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 验证标题不能为空
        if ( null == id || "".equals(id) ) {
            result.put("msg", "参数错误");
            return result;
        }
        //TODO 更改状态
        returnReceiptService.reductionReturnReceipt(id);
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }

    //TODO 回收站删除数据
    @RequestMapping(value = "/deleteReturnReceipt", method = RequestMethod.GET)
    @ResponseBody

    public Map<String, Object> deleteReturnReceipt(String ids) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( StringUtils.isEmpty(ids) ) {
            result.put("msg", "参数错误");
            return result;
        }
        boolean b = returnReceiptService.deleteReturnReceipt(ids);
        if ( b ) {
            result.put("state", 1); //0代表失败，1代表成功
        }
        return result;
    }
}


