package com.hx.controller;



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
import java.io.UnsupportedEncodingException;
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
    private static Logger log = Logger.getLogger(ExcelController.class);// 日志文件
    /**
     * 发件箱导出
     */
    @RequestMapping(value = "/export.do", produces = "application/form-data; charset=utf-8")
    public void OutputExcel2(String ids,HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html,charset=utf-8");
            //查询需要导出的数据
            List<Outbox> list = outBoxService.getAll(ids.split(","));
        //        List<Outbox> list = new ArrayList<>();
        ////        for (String id : ids.split(",")) {
        ////            list = outBoxService.getAll(ids);
        ////            //list.add(mail);
        ////        }
            List<Object[]> data = new ArrayList<>();    //转换数据
            Iterator<Outbox> it = list.iterator();
            while (it.hasNext()) {
                Outbox m = it.next();
                //data.add(new Object[]{m.getId(), m.getLinknumber(), m.getTypeid(), m.getLinkname()});
                data.add(new Object[]{  m.getSendnumber(), m.getReceivingunit(),
                        m.getReceivenumber(), m.getSendline()});
            }
            //构建Excel表头,此处需与data中数据一一对应
            List<String> headers = new ArrayList<String>();
            headers.add("sendnumber");
            headers.add("receivingunit");
            headers.add("receivenumber");
            headers.add("sendline");
            ExcelHelper.exportExcel(headers, data, "downloadFile","xlsx", response);       //downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码
            log.info("导出成功");
        } catch (UnsupportedEncodingException e) {
            log.error(e.toString());
        }
    }
    @RequestMapping(value = "/queryoutbox", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> outboxLists(Integer pageNo, Integer pageSize, String  sendnumber, String receivenumber, String receivingunit, String sendline, String message,String beginDate ,String endDate) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        Map<String,Object> searchMap=new HashMap();
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate=beginDate.trim();  //2019-12-01 12:00:00
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate=endDate.trim();
        }
        searchMap.put("sendnumber",sendnumber);
        searchMap.put("receivingunit",receivingunit);
        searchMap.put("receivenumber",receivenumber);
        searchMap.put("sendline",sendline);
        searchMap.put("message",message);
        searchMap.put("beginDate",beginDate);
        searchMap.put("endDate",endDate);
        int count = outBoxService.queryTotalCount(searchMap);
        if ( count > 0 ) {
            List<Outbox> outboxLists = outBoxService.queryoutBox(searchMap, pageNo, pageSize);
            result.put("outboxLists", outboxLists);
            result.put("totalCount", count);
        }
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }

    //TODO 删除记录
    @RequestMapping(value = "/deloutbox", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> deloutbox(String str) {
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
                outBoxService.deloutbox(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
            log.info("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败");
            result.put("msg", e.getMessage());
        }

        return result;
    }
}
