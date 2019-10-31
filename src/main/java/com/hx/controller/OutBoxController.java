package com.hx.controller;



import com.hx.modle.Outbox;
import com.hx.service.OutBoxService;
import com.hx.util.ExcelHelper;
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
@RequestMapping("/outbox")
public class OutBoxController {
    @Autowired
    private OutBoxService outBoxService;
    private static Logger log = Logger.getLogger(ExcelController.class);// 日志文件
    /**
     * 发件箱导出
     */
    @RequestMapping(value = "/outbox/export.do", produces = "application/form-data; charset=utf-8")
    public void OutputExcel2(HttpServletRequest request, HttpServletResponse response) throws Exception {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html,charset=utf-8");

            //查询需要导出的数据
            List<Outbox> list = outBoxService.getAll();

            List<Object[]> data = new ArrayList<>();    //转换数据
            Iterator<Outbox> it = list.iterator();
            while (it.hasNext()) {
                Outbox m = it.next();
                //data.add(new Object[]{m.getId(), m.getLinknumber(), m.getTypeid(), m.getLinkname()});
                //data.add(new Object[]{  m.getAcceptname(), m.getSendtime()});
            }

            //构建Excel表头,此处需与data中数据一一对应
            List<String> headers = new ArrayList<String>();
            headers.add("Acceptname");
            headers.add("Sendtime");

            ExcelHelper.exportExcel(headers, data, "downloadFile","xlsx", response);       //downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码


            }
        @RequestMapping(value = "/outboxList", method = RequestMethod.GET)
        @ResponseBody
        public Map<String, Object> mailLists(Integer pageNo, Integer pageSize) {
            Map<String, Object> result = new HashMap<>();
            result.put("state", 0); //0代表失败，1代表成功

            Map<String,Object> searchMap=new HashMap();
            //TODO
            List<Outbox> mailLists = outBoxService.queryALLMailList(searchMap, pageNo, pageSize);
            result.put("mailLists", mailLists); //
            int count = outBoxService.queryTotalCount(searchMap);

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
                    return result;
                }
                String[] split = str.split(",");
                try {
                    for (int i = 0; i < split.length; i++) {
                        outBoxService.deloutbox(Integer.parseInt(split[i]));
                    }
                    result.put("state", 1); //0代表失败，1代表成功
                } catch (Exception e) {
                    e.printStackTrace();
                    result.put("msg", e.getMessage());
                }
                log.error("");
                log.info("");
                return result;
}
}
