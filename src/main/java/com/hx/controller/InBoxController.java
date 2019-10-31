package com.hx.controller;

import com.hx.modle.Inbox;
import com.hx.service.InBoxService;
import com.hx.util.ExcelHelper;
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
    private static Logger log = Logger.getLogger(ExcelController.class);// 日志文件

    /**
     * 收件箱导出
     */
    @RequestMapping(value = "/export.do", produces = "application/form-data; charset=utf-8")
    public void OutputExcel2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html,charset=utf-8");

        //查询需要导出的数据
        List<Inbox> list = inBoxService.getAll();

        List<Object[]> data = new ArrayList<>();    //转换数据
        Iterator<Inbox> it = list.iterator();
        while (it.hasNext()) {
            Inbox m = it.next();
            //data.add(new Object[]{m.getId(), m.getLinknumber(), m.getTypeid(), m.getLinkname()});
           // data.add(new Object[]{  m.getSendname(), m.getAccepttime(), m.getFileaddress()});
        }

        //构建Excel表头,此处需与data中数据一一对应
        List<String> headers = new ArrayList<String>();
        headers.add("Sendname");
        headers.add("Accepttime");
        headers.add("Fileaddress");
        ExcelHelper.exportExcel(headers, data, "downloadFile", "xlsx", response);       //downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码
        log.error("");
        log.info("");
    }

   //收件箱查询
   @RequestMapping(value = "/queryinbox", method = RequestMethod.POST)
   @ResponseBody
   public Map<String, Object> inboxs(Integer pageNo, Integer pageSize, String mailListId,String beginDate ,String endDate) {
        //mailListId="m";
        beginDate=beginDate.trim();
        endDate=endDate.trim();
       Map<String, Object> result = new HashMap<>();
       result.put("state", 0); //0代表失败，1代表成功

       Map<String,Object> searchMap=new HashMap();
       searchMap.put("mailListId",mailListId);
       searchMap.put("beginDate",beginDate);
       searchMap.put("endDate",endDate);
       int count = inBoxService.queryTotalCount(searchMap);

       List<Inbox> mails = inBoxService.queryALLMail(searchMap, pageNo, pageSize);
       result.put("mails", mails);
       result.put("totalCount", count);
       result.put("state", 1); //0代表失败，1代表成功
       return result;
   }
    @RequestMapping(value = "/inboxList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> mailLists() {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        //TODO 查询全部
        List<Inbox> mailLists = inBoxService.queryALLMailList();
        result.put("mailLists", mailLists); //

        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }

    //TODO 删除记录
    @RequestMapping(value = "/deinbox", method = RequestMethod.POST)
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
                inBoxService.deinbox(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
        }
        return result;

    }


    }

