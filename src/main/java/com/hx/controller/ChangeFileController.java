package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/19 15:56
 *@功能:转换文件
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.service.ChangeFileService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@Controller
public class ChangeFileController {
    private Logger logger=Logger.getLogger( ChangeFileController.class );
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
        Map<String,Object> message=null;
        message=changeFileService.changeFileSend(file);
        return JSONObject.toJSONString( message );
    }
    /**
     *
     * 功能描述: 前台点击收件箱,发件箱信息;或者是发送前预览,发回执箱查看关联原件,给我tifPath,我进行拆分成多个tif文件,返回文件路径的list集合
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/12/5 14:40
     */
    @RequestMapping("getFileList")
    @ResponseBody
    public String getFileList(String tifPath){
        Map<String,Object> map=changeFileService.getFileList(tifPath);
        return JSONObject.toJSONString( map );
    }
    //前台通过img标签进行文件预览
    @RequestMapping("fileView")
    @ResponseBody
    public void fileView(String photoUrl, HttpServletResponse response){
        // photoUrl为接收到的路径
        if(StringUtils.isNotBlank(photoUrl)) {
            File file = new File( photoUrl );
            if (file.exists()) {
                try {
                    FileInputStream fis = new FileInputStream( file );
                    BufferedInputStream bis = new BufferedInputStream( fis, 1024 );
                    ByteArrayOutputStream bos = new ByteArrayOutputStream( 1024 );
                    byte[] cache = new byte[1024];
                    int length = 0;
                    while ((length = bis.read( cache )) != -1) {
                        bos.write( cache, 0, length );
                    }
                    response.getOutputStream().write( bos.toByteArray() );
                } catch (Exception e) {
                    logger.error( e.toString() );
                }
            }
        }
    }
}
