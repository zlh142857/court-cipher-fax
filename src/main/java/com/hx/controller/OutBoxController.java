package com.hx.controller;



import com.alibaba.fastjson.JSONObject;
import com.hx.modle.Outbox;
import com.hx.service.OutBoxService;
import com.hx.util.ExcelHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 发件箱管理
 * @author 范聪敏
 * @date 2019/9/11 22:17
 * @desc
 */

@Controller
@RequestMapping("/Outbox")
public class OutBoxController {
    @Autowired
    private OutBoxService outBoxService;
    private static Logger log = Logger.getLogger(OutBoxController.class);// 日志文件
    /**
     * 发件箱导出
     */
    @RequestMapping(value = "/export.do", produces = "application/form-data; charset=utf-8")
    public void OutputExcel2(String ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html,charset=utf-8");
        //查询需要导出的数据
        List<Outbox> list = outBoxService.getAll(ids.split(","));
        List<Object[]> data = new ArrayList<>();    //转换数据
        Iterator<Outbox> it = list.iterator();
        while (it.hasNext()) {
            Outbox m = it.next();
            data.add(new Object[]{m.getSendnumber(), m.getReceivingunit(),
                    m.getReceivenumber(), m.getSendline(), m.getPageNum(), m.getMessage(), m.getCreate_time()});
        }
        //构建Excel表头,此处需与data中数据一一对应
        List<String> headers = new ArrayList<String>();
        headers.add("发送方号码");
        headers.add("接收单位");
        headers.add("接收号码");
        headers.add("文件标题");
        headers.add("页数");
        headers.add("是否发送成功");
        headers.add("发送时间");
        ExcelHelper.exportExcel(headers, data, "发件箱", "xlsx", response);       //downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码
    }

    @RequestMapping(value = "/queryoutbox", method = RequestMethod.GET)
    @ResponseBody
    public String outboxLists(Integer pageNo, Integer pageSize, String sendnumber, String receivenumber, String receivingunit,
                                           String sendline, String message, String beginDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        Map<String, Object> searchMap = new HashMap();
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate = beginDate.trim();  //2019-12-01 12:00:00
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate = endDate.trim();
        }
        searchMap.put("sendnumber", sendnumber);
        searchMap.put("receivingunit", receivingunit);
        searchMap.put("receivenumber", receivenumber);
        searchMap.put("sendline", sendline);
        searchMap.put("message", message);
        searchMap.put("beginDate", beginDate);
        searchMap.put("endDate", endDate);
        int count = outBoxService.queryTotalCount(searchMap);
        List<Outbox> mails = null;
        if ( count > 0 ) {
            mails = outBoxService.queryoutBox(searchMap, pageNo, pageSize);
            result.put("totalCount", count);
            result.put("state", 1);
            result.put("mails", mails);
        } else {
            result.put("mails", new ArrayList<Outbox>());
            result.put("state", 0);
        }
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss" );
    }

    //TODO 修改标题
    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> modifyoutbox(String sendline, String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 验证标题不能为空
        if ( null == sendline || "".equals(sendline) ) {
            result.put("msg", "参数错误");
            return result;
        }
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("id", id);
        searchMap.put("sendline", sendline);
        //TODO 修改标题
        outBoxService.modifyoutBox(searchMap);
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }

    //TODO 更改状态
    @RequestMapping(value = "/modifyoutbox", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> modifyoutbox(String id) {
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
                outBoxService.modifoutbox(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            log.error(e.toString());
            result.put("msg", "删除失败");
        }
        return result;
    }

    //TODO 回收站显示
    @RequestMapping(value = "/recoveryoutbox", method = RequestMethod.GET)
    @ResponseBody
    public String recoveryoutbox(Integer pageNo, Integer pageSize, String sendnumber, String receivenumber, String receivingunit,
                                              String sendline, String message, String beginDate, String endDate) {
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
        searchMap.put("receivingunit", receivingunit);
        searchMap.put("receivenumber", receivenumber);
        searchMap.put("sendline", sendline);
        searchMap.put("message", message);
        searchMap.put("beginDate", beginDate);
        searchMap.put("endDate", endDate);
        int count = outBoxService.queryTotalCount(searchMap);
        List<Outbox> mails = null;
        if ( count > 0 ) {
            mails = outBoxService.Recoveryoutbox(searchMap, pageNo, pageSize);
            result.put("totalCount", count);
            result.put("state", 1);
            result.put("bs", 2);
            result.put("mails", mails);
        } else {
            result.put("mails", new ArrayList<Outbox>());
            result.put("state", 0);
        }
        return JSONObject.toJSONStringWithDateFormat( result,"yyyy-MM-dd HH:mm:ss" );
    }

    //TODO 数据还原
    @RequestMapping(value = "/reductionoutbox", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> reductionoutbox(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 验证标题不能为空
        if ( null == id || "".equals(id) ) {
            result.put("msg", "参数错误");
            return result;
        }
        //TODO 更改状态
        outBoxService.reductionoutbox(id);
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }

    //TODO 回收站删除数据
    @RequestMapping(value = "/deleteoutbox", method = RequestMethod.GET)
    @ResponseBody

    public Map<String, Object> deleteoutbox(String ids) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( StringUtils.isEmpty(ids) ) {
            result.put("msg", "参数错误");
            return result;
        }
        boolean b = outBoxService.deleteoutbox(ids);
        if ( b ) {
            result.put("state", 1); //0代表失败，1代表成功
        }
        return result;
    }


}
