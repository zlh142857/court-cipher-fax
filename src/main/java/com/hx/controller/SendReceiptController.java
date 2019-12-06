package com.hx.controller;

import com.alibaba.fastjson.JSONObject;
import com.hx.modle.Send_Receipt;
import com.hx.service.SendReceiptService;
import com.hx.util.ExcelHelper;
import com.hx.util.GetTimeToFileName;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static com.hx.common.StaticFinal.TEMPDIR;

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
    private static Logger log = Logger.getLogger(SendReceiptController.class);// 日志文件

    //收件箱查询
    @RequestMapping(value = "/queryReturnReceipt", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> inboxs(Integer pageNo, Integer pageSize, String receivingunit, String sendline,
                                      String message, String sendnumber, String beginDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        if ( StringUtils.isNotEmpty(beginDate) ) {
            beginDate = beginDate.trim();
        }
        if ( StringUtils.isNotEmpty(endDate) ) {
            endDate = endDate.trim();
        }
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("receivingunit", receivingunit);
        searchMap.put("beginDate", beginDate);
        searchMap.put("endDate", endDate);
        searchMap.put("sendline", sendline);
        searchMap.put("message", message);
        searchMap.put("sendnumber", sendnumber);
        int count = sendReceiptService.queryTotalCount(searchMap);
        List<Send_Receipt> mails=null;
        if ( count > 0 ) {
            mails = sendReceiptService.queryALLMail(searchMap, pageNo, pageSize);
            result.put("mails", mails);
            result.put("totalCount", count);
            result.put("state", 1);
        }else{
            result.put("state", 0);
            result.put("mails", new ArrayList<Send_Receipt>(  ));
        }
        return result;

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
    @RequestMapping(value = "/deleSendReceipt", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delinbox(String str) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( null == str || "".equals(str.trim()) ) {
            result.put("msg", "参数错误");
            log.error("参数为空" + str);
            return result;
        }
        String[] split = str.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                sendReceiptService.deinbox(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            log.error("删除失败");
            result.put("msg", e.getMessage());
        }
        return result;
    }
    @RequestMapping(value = "/SendReceiptpoi.do", produces = "application/form-data; charset=utf-8")
    public void SendReceiptpoi(String ids, HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html,charset=utf-8");
            //查询需要导出的数据
            List<Send_Receipt> list = sendReceiptService.getAll(ids.split(","));
            List<Object[]> data = new ArrayList<>();    //转换数据
            Iterator<Send_Receipt> it = list.iterator();
            while (it.hasNext()) {
                Send_Receipt m = it.next();
                data.add(new Object[]{m.getSendnumber(), m.getReceivenumber(), m.getReceivenumber()});
                // data.add(new Object[]{  m.getSendname(), m.getAccepttime(), m.getFileaddress()});
            }
            //构建Excel表头,此处需与data中数据一一对应
            List<String> headers = new ArrayList<String>();
            headers.add("发送号码");
            headers.add("文件标题");
            headers.add("接收方号码");
            ExcelHelper.exportExcel(headers, data, "发回执", "xlsx", response);       //downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码
        } catch (Exception e) {
            log.error( e.toString());
        }

    }
    //TODO 修改标题
    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> modifysendReceipt(String sendline , String id) {
        Map<String, Object> result = new HashMap<>();
        //TODO 验证标题不能为空
        if(null == sendline || "".equals(sendline)) {
            result.put("msg", "参数错误");
            log.error("标题为空"+sendline);
            return result;
        }
        Map<String,Object> searchMap=new HashMap();
        searchMap.put("id",id);
        searchMap.put("sendline",sendline);
        //TODO 修改标题
        sendReceiptService.modifysendReceipt(searchMap);
        result.put("state", 1); //0代表失败，1代表成功
        return result;
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
    public boolean updateIsLink(String inBoxId){
        boolean flag=false;
        try {
            if(null !=inBoxId){
                flag=sendReceiptService.updateIsLink(inBoxId);
            }
        } catch (Exception e) {
            log.error( e );
        }
        return flag;
    }
    /**
     *
     * 功能描述: 查看关联原件,收件箱预览
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/13 10:55
     *//*
    @RequestMapping("/checkText")
    @ResponseBody
    public String checkText(String tifPath,HttpServletResponse response){
        String base64="";
        String filename=GetTimeToFileName.GetTimeToFileName();
        String pdfPath=TEMPDIR+"/"+filename+".pdf";
        try {
            if(null != tifPath){
                base64=sendReceiptService.checkText(pdfPath,tifPath);
            }
        } catch (Exception e) {
            log.error( e );
        }
        BufferedInputStream br = null;
        try {
            br = new BufferedInputStream(new FileInputStream("pdfPath"));
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset(); // 非常重要
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "inline; filename="+filename);
            OutputStream out = response.getOutputStream();
            while ((len = br.read(buf)) != -1)
                out.write(buf, 0, len);
            br.close();
            out.close();
        } catch (FileNotFoundException e) {
            log.error( e.toString() );
        } catch (IOException e) {
            log.error( e.toString() );
        }
        return JSONObject.toJSONString( base64 );
    }*/

}
