package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/26 11:27
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.service.BackUpService;
import com.hx.util.GetTimeToFileName;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BackUpController {
    private static Logger logger=Logger.getLogger( BackUpController.class );
    @Autowired
    private BackUpService backUpService;
    //根据前台传递的标识进行判断是哪个箱,然后查询出相应箱的所有备份文件夹
    @RequestMapping("selectBackUpFilePath")
    @ResponseBody
    public String selectBackUpFilePath(Integer flag){
        Map<String,Object> map=new HashMap<>(  );
        try{
            if(null == flag){
                map.put( "message","标识符为空" );
                map.put( "flag",false );
                return JSONObject.toJSONString(map);
            }
            map=backUpService.selectBackUpFilePath(flag);
        }catch (Exception e){
            map.put( "message","查询失败" );
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString(map);
    }
    //根据选中的备份文件进行恢复
    @RequestMapping("recoverBackUp")
    @ResponseBody
    public String recoverBackUp(String filePath,Integer flag){
        Map<String,Object> map=new HashMap<>(  );
        try{
            if(null == flag){
                map.put( "message","标识符为空" );
                map.put( "flag",false );
                return JSONObject.toJSONString(map);
            }
            if(null == filePath || "".equals( filePath )){
                map.put( "message","备份文件路径为空" );
                map.put( "flag",false );
                return JSONObject.toJSONString(map);
            }
            map=backUpService.recoverBackUp(filePath,flag);
        }catch (Exception e){
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString(map);
    }
    //手动进行备份
    @RequestMapping("startBackUp")
    @ResponseBody
    public String startBackUp(){
        Map<String,Object> map=new HashMap<>(  );
        try{
            map=backUpService.startBackUp();
        }catch (Exception e){
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString(map);
    }
    //备份导出下载
    @RequestMapping("downBackUp")
    @ResponseBody
    public void downBackUp(String filePath,HttpServletResponse response){
        String fileName=GetTimeToFileName.GetTimeToFileName()+".xlsx";
        try {
            if(null==filePath || "".equals( filePath )){
                logger.error( "参数为空" );
            }
            //设置文件路径
            File file = new File(filePath);
            if (file.exists()) {
                response.setHeader("Content-Disposition", "attachment;filename=" +fileName);
                response.setContentType("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    logger.error( e.toString() );
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            logger.error( e.toString() );
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            logger.error( e.toString() );
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error( e );
        }
    }
}
