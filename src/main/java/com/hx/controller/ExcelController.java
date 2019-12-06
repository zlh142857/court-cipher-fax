package com.hx.controller;

import com.hx.modle.Mail;
import com.hx.service.ExcelService;
import com.hx.util.ExcelHelper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
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
    @RequestMapping(value = "/InputExcel.do")
    @ResponseBody
    public String InputExcel(@RequestParam("file") MultipartFile file,
                HttpServletRequest request ,String typeid){
        String flag = "Import Fail";// 上传标志
        if (!file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();// 原文件名字
                InputStream is = file.getInputStream();// 获取输入流
                flag = excelService.InputExcel(is, originalFilename,typeid);
            } catch (Exception e) { log.error("方法异常");
                flag = "Import Exception";// 上传出错
            }
        }
        return flag;
    }
    @RequestMapping(value = "/OutputExcel2.do", produces = "application/form-data; charset=utf-8")
    public void OutputExcel2(String ids, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html,charset=utf-8");
            List<Mail> list = new ArrayList<>();
            for (String id : ids.split(",")) {
                list = excelService.getById(id);
            }
            List<Object[]> data = new ArrayList<>();    //转换数据
            Iterator<Mail> it = list.iterator();
            while (it.hasNext()) {
                Mail m = it.next();//data.add(new Object[]{m.getId(), m.getLinknumber(), m.getTypeid(), m.getLinkname()});
                data.add(new Object[]{  m.getLinknumber(), m.getTypename()});
            }
            //构建Excel表头,此处需与data中数据一一对应
            List<String> headers = new ArrayList<String>();
            headers.add("联系人号码");
            headers.add("联系人名称");
            ExcelHelper.exportExcel(headers, data, "通讯录","xlsx", response);//downloadFile为文件名称,可以自定义,建议用英文,中文在部分浏览器会乱码
        } catch (Exception e) {
            log.error( e.toString() );
        }

    }
}





