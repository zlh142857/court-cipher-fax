package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/19 15:56
 *@功能:转换文件
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.service.ChangeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.hx.service.impl.SendFaxServiceImpl.deleteFiles;

@Controller
public class ChangeFileController {
    @Autowired
    private ChangeFileService changeFileService;
    /**
     *
     * 功能描述: 转换文件为tiff文件
     *
     * 业务逻辑:点击新建任务,上传文件,后台判断文件格式,是否支持转换,支持就转换,不支持返回信息
     * 
     * @param: 文件file
     * @return:
     * @auther: 张立恒
     * @date: 2019/10/23 14:45
     */
    @RequestMapping("changeFileSend")
    @ResponseBody
    public String changeFileSend(MultipartFile file){
        String message=changeFileService.changeFileSend(file);
        return JSONObject.toJSONString( message );
    }
    /**
     *
     * 功能描述: 关闭发送文件窗口,取消发送.点击关闭前调用,删除转换的tiff文件
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return:
     * @auther: 张立恒
     * @date: 2019/10/24 14:50
     */
    @RequestMapping("deleteTiff")
    @ResponseBody
    public void deleteTiff(@RequestParam("tifPath") String tifPath,@RequestParam("receiptPath") String receiptPath){
        deleteFiles(tifPath,receiptPath);
    }
}
