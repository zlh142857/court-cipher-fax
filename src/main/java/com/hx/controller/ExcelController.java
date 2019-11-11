package com.hx.controller;

import com.hx.modle.Mail;
import com.hx.service.ExcelService;
import com.hx.util.ExcelHelper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author 范聪敏
 * @date 2019/9/6 15:47
 * @desc 通讯簿管理
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {
    @Resource
    private ExcelService excelService;
    private static  Logger log = Logger.getLogger(ExcelController.class);// 日志文件
    @RequestMapping("/getAll")
    @ResponseBody
    public String getAll(HttpServletRequest request, HttpServletResponse response) {

        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html,charset=utf-8");
            List<Mail> list = excelService.getAll();
            log.info("455552");
        } catch (UnsupportedEncodingException e) {
            log.error("12223");
        }

//        ResponseUtil.write(response, GsonUtils.object2json(list));
        return null;
    }

    @RequestMapping(value = "/InputExcel.do")
    @ResponseBody
    public String InputExcel(@RequestParam("file") MultipartFile file,
                             HttpServletRequest request ,String typeid) throws Exception {
        String flag = "Import Fail";// 上传标志
        if (!file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();// 原文件名字
                log.info("文件名：" + originalFilename);

                InputStream is = file.getInputStream();// 获取输入流
                flag = excelService.InputExcel(is, originalFilename,typeid);
            } catch (Exception e) {
                flag = "Import Exception";// 上传出错
                e.printStackTrace();
            }
        }
        return flag;
    }

//    @RequestMapping(value = "/OutputExcel.do", produces = "application/form-data; charset=utf-8")
//    @ResponseBody
//    public String OutputExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html,charset=utf-8");
//
//        List<Mail_List> list = excelService.OutputExcel();
//
//        String message = OutputExcel.OutExcel(request, response, list);
//        if (message.equals("fail")) {
//            ServletOutputStream out = response.getOutputStream();
//            message = "导出失败，请重试";
//            String s = "<!DOCTYPE HTML><html><head><script>alert('" + message + "');</script></head><body></body></html>";
//            out.print(s);
//        }
//        return null;
//    }

    @RequestMapping(value = "/OutputExcel2.do", produces = "application/form-data; charset=utf-8")
    public void OutputExcel2(String ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html,charset=utf-8");

        //查询需要导出的数据
       // List<Mail> list = excelService.OutputExcel();
        List<Mail> list = new ArrayList<>();
        for (String id : ids.split(",")) {
            list = excelService.getById(id);
            //list.add(mail);
       }

        List<Object[]> data = new ArrayList<>();    //转换数据
        Iterator<Mail> it = list.iterator();
        while (it.hasNext()) {
            Mail m = it.next();
            //data.add(new Object[]{m.getId(), m.getLinknumber(), m.getTypeid(), m.getLinkname()});
            data.add(new Object[]{  m.getLinknumber(), m.getLinkname()});
        }

        //构建Excel表头,此处需与data中数据一一对应
        List<String> headers = new ArrayList<String>();
        //headers.add("ID");
        headers.add("联系人号码");
        headers.add("联系人名称");
        ExcelHelper.exportExcel(headers, data, "downloadFile","xlsx", response);       //downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码
        log.error("sdfsdfgsdf");
        log.info("gheryh");
    }


    }





