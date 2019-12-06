package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/23 9:20
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hx.modle.ChMsg;
import com.hx.modle.Device_Setting;
import com.hx.modle.TempModel;
import com.hx.service.SendFaxService;
import com.hx.util.GetTimeToFileName;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static com.hx.common.StaticFinal.TEMPDIR;

@Controller
public class SendFaxController {
    private static Logger logger=Logger.getLogger(SendFaxController.class);
    @Autowired
    private SendFaxService sendFaxService;
    /**
     *
     * 功能描述: 发送前,自动选择线路发送还是指定号码发送,下拉列表框查询,从device_setting表中查询
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/10/24 14:54
     */
    @RequestMapping("selectSeatNumber")
    @ResponseBody
    public String selectSeatNumber(){
        List<Device_Setting> numbers=sendFaxService.selectSeatNumber();
        return JSONObject.toJSONString( numbers );
    }
    /**
     *
     * 功能描述: 发送传真
     *
     * 业务逻辑:先呼叫对方号码,然后才能调用传真函数
     * 
     * @param: 正文文件路径,回执页base64,收文单位,收文号码,指定发送号码,是否包含回执页 isBack 0包含回执页,1不包含回执页,2只有回执页,文件名称
     * @return: 
     * @auther: 张立恒
     * @date: 2019/10/8 17:13
     */
    @RequestMapping("sendFax")
    @ResponseBody
    public String sendFax(String tifPath,String receiptPath,String tempModels,String sendNumber,
                          String isBack,String ch,String filename,String id){
        String mes="";
        ObjectMapper mapper = new ObjectMapper();
        JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, TempModel.class);
        List<TempModel> tempList = null;
        try {
            tempList = (List<TempModel>)mapper.readValue(tempModels, jt);
            int size=tempList.size();
            for(int i=0;i<size;i++){
                mes=sendFaxService.sendFax(tifPath,receiptPath,tempList.get( i ).getTypename(),tempList.get(i).getLinknumber(),sendNumber,isBack,filename,id,ch);
                try {
                    Thread.sleep( 3000 );
                } catch (InterruptedException e) {
                    logger.error( e.toString() );
                }
            }
        } catch (IOException e) {
            logger.error( e.toString() );
        } catch (Exception e) {
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( mes );
    }
    /**
     *
     * 功能描述: base转换为base64
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/10/29 13:50
     */
    @RequestMapping("baseToTif")
    @ResponseBody
    public String baseToTif(String base64){
        String receiptPath=sendFaxService.baseToTif(base64);
        return JSONObject.toJSONString( receiptPath );
    }
    @RequestMapping("createBarCode")
    @ResponseBody
    public String createBarCode(){
        String file=null;
        try {
            file=sendFaxService.createBarCode();
        } catch (IOException e) {
            logger.error( e );
        }
        return JSONObject.toJSONString( file );
    }
    @RequestMapping("rateOfAdvance")
    @ResponseBody
    public synchronized String rateOfAdvance(String ch){
        List<ChMsg> list=new ArrayList<>(  );
        try {
            list=sendFaxService.rateOfAdvance(ch);
        } catch (Exception e) {
            logger.error( e );
        }
        return JSONObject.toJSONString( list );
    }
    @RequestMapping("selectChAndSeatNumber")
    @ResponseBody
    public String selectChAndSeatNumber(){
        List<Device_Setting> list=new ArrayList<>(  );
        try {
            list=sendFaxService.selectChAndSeatNumber();
        } catch (Exception e) {
            logger.error( e );
        }
        return JSONObject.toJSONString( list );
    }
    /**
     *
     * 功能描述: 新增定时任务
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/13 18:49
     */
    @RequestMapping("insertSchTask")
    @ResponseBody
    public boolean insertSchTask(String tifPath,String receiptPath,String tempModels,String sendNumber,
                                 String isBack,String ch,String filename,String id,String sendTime){
        boolean flag=false;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, TempModel.class);
            List<TempModel> tempList =  (List<TempModel>)mapper.readValue(tempModels, jt);
            flag=sendFaxService.insertSchTask(tifPath,receiptPath,tempList,sendNumber,isBack,filename,id,ch,sendTime);
        } catch (Exception e) {
            logger.error( e );
        }
        return flag;
    }
    @RequestMapping("deleteSchTask")
    @ResponseBody
    public boolean deleteSchTask(String id){
        boolean flag=false;
        try {
            if(null !=id){
                flag=sendFaxService.deleteSchTask(Integer.valueOf( id ));
            }else{
                logger.error( "id为空" );
            }
        } catch (Exception e) {
            logger.error( e );
        }
        return flag;
    }
    @RequestMapping("updateTaskDate")
    @ResponseBody
    public boolean updateTaskDate(String id,String date){
        boolean flag=false;
        try {
            flag=sendFaxService.updateTaskDate(id,date);
        } catch (Exception e) {
            logger.error( e );
        }
        return flag;
    }
    @RequestMapping("selectTaskLimit")
    @ResponseBody
    public String selectTaskLimit(String pageNow,String pageSize){
        Map<String,Object> map=null;
        try {
            if(null!=pageNow&&null!=pageSize){
                map=sendFaxService.selectTaskLimit(Integer.valueOf( pageNow ),Integer.valueOf( pageSize ));
            }else{
                logger.error( "分页参数为空" );
            }
        } catch (Exception e) {
            logger.error( e );
        }
        return JSONObject.toJSONStringWithDateFormat( map,"yyyy-MM-dd HH:mm:ss" );
    }
    /**
     *
     * 功能描述: 发送前预览文件,给前端发送PDFbase64
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/15 14:42
     *//*
    @RequestMapping("tifView")
    @ResponseBody
    public String tifView(String tifPath, HttpServletResponse response){
        String map="";
        String filename=GetTimeToFileName.GetTimeToFileName();
        String pdfPath=TEMPDIR+"/"+filename+".pdf";
        try {
            if(null!=tifPath){
                map=sendFaxService.tifView(pdfPath,tifPath);
            }else{
                logger.error( "参数为空" );
            }
        } catch (Exception e) {
            logger.error( e );
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
            logger.error( e.toString() );
        } catch (IOException e) {
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( map);
    }*/
    /**
     *
     * 功能描述:取消发送
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/11/18 9:50
     */
    @RequestMapping("undoSend")
    @ResponseBody
    public boolean undoSend(String ch){
        boolean flag=false;
        try {
            if(null!=ch){
                flag=sendFaxService.undoSend(ch);
            }else{
                logger.error( "参数为空" );
            }
        } catch (Exception e) {
            logger.error( e );
        }
        return flag;
    }
    /**
     *
     * 功能描述: 右键发送回执,然后拆分文件,反转颜色,返回文件路径
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/22 10:54
     */
    @RequestMapping("returnFaxGetPath")
    @ResponseBody
    public String returnFaxGetPath(String tifPath){
        String path="";
        try {
            if(null!=tifPath){
                path=sendFaxService.returnFaxGetPath(tifPath);
            }else{
                logger.error( "参数为空" );
            }
        } catch (Exception e) {
            logger.error( e );
        }
        return JSONObject.toJSONString( path );
    }

}
