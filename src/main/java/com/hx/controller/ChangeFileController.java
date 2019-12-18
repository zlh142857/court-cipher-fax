package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/19 15:56
 *@功能:转换文件
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.modle.Elec_Sign;
import com.hx.service.ChangeFileService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
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
                response.setContentType( "text/html; charset=UTF-8" );
                response.setContentType( "image/jpeg" );
                FileInputStream fis = null;
                OutputStream os=null;
                try {
                    fis = new FileInputStream( photoUrl );
                    os = response.getOutputStream();
                    int count = 0;
                    byte[] buffer = new byte[1024 * 1024];
                    while ((count = fis.read( buffer )) != -1)
                        os.write( buffer, 0, count );
                    os.flush();
                } catch (FileNotFoundException e) {
                    logger.error( e.toString() );
                } catch (IOException e) {
                    logger.error( e.toString() );
                }finally {
                    if(os!=null){
                        try {
                            os.close();
                        } catch (IOException e) {
                            logger.error( e.toString() );
                        }
                    }
                    if(fis!=null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            logger.error( e.toString() );
                        }
                    }
                }

            }
        }
    }

    //电子签章接口:1.新增电子签章
    //2.加盖电子签章时查询电子签章以供用户选择
    //3.删除电子签章
    //4.加盖签章,生成新文件
    @RequestMapping("addNewElecSign")
    @ResponseBody
    public boolean addNewElecSign(String signName,String type){
        if(null == signName || "".equals( signName )){
            return false;
        }
        if(null == type || "".equals( type )){
            return false;
        }
        boolean message=changeFileService.addNewElecSign(signName,type);
        return message;
    }

    @RequestMapping("delElecSign")
    @ResponseBody
    public boolean delElecSign(Integer id,String savePath){
        if(null == savePath || "".equals( savePath )){
            return false;
        }
        if(null == id){
            return false;
        }
        boolean message=changeFileService.delElecSign(id,savePath);
        return message;
    }
    @RequestMapping("selectElecSign")
    @ResponseBody
    public String selectElecSign(){
        List<Elec_Sign> list=changeFileService.selectElecSign();
        return JSONObject.toJSONStringWithDateFormat( list,"yyyy-MM-dd HH:mm:ss" );
    }
    @RequestMapping("selectElecSignName")
    @ResponseBody
    public String selectElecSignName(){
        List<Elec_Sign> list=changeFileService.selectElecSignName();
        return JSONObject.toJSONString(list);
    }
    //盖章
    @RequestMapping("sealOnFile")
    @ResponseBody
    public boolean sealOnFile(String tifPath,Integer id){
        if(null ==tifPath || "".equals( tifPath )){
            return false;
        }
        if(null == id){
            return false;
        }
        boolean flag=changeFileService.sealOnFile(tifPath,id);
        return flag;
    }

}
