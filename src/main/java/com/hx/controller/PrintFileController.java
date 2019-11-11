package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/8/26 16:30
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hx.modle.Program_Setting;
import com.hx.modle.TempModel;
import com.hx.service.PrintFileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.co.mmscomputing.device.scanner.ScannerIOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PrintFileController{
    private static Logger logger=Logger.getLogger( PrintFileController.class );
    @Autowired
    private PrintFileService printFileService;
    //打开扫描
    @RequestMapping("printScan")
    @ResponseBody
    public String printScan() {
        String tifPath="";
        try {
            tifPath=printFileService.printScan();
        } catch (ScannerIOException e) {
            logger.error( e.toString() );
        } catch (InterruptedException e) {
            logger.error( e.toString() );
        } catch (Exception e){
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( tifPath );
    }
    /**
     *
     * 功能描述: 选中文件进行打印,文件类型为tif
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/11/11 9:40
     */
    @RequestMapping("printTifs")
    @ResponseBody
    public String print(String tempModels){
        String str="";
        ObjectMapper mapper = new ObjectMapper();
        JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, TempModel.class);
        try{
            List<TempModel> tempList =  (List<TempModel>)mapper.readValue(tempModels, jt);
            str=printFileService.printFile(tempList);
        }catch (Exception e){
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( str );
    }
    /**
     *
     * 功能描述: 程序设置页面查询打印机服务名称
     *
     * 业务逻辑:
     * 
     * @param: 
     * @return: 
     * @auther: 张立恒
     * @date: 2019/10/31 16:45
     */
    @RequestMapping("selectPrintService")
    @ResponseBody
    public String selectPrintService(){
        Map<String,Object> map=printFileService.selectPrintService();
        return JSONObject.toJSONString( map );
    }
    /**
     *
     * 功能描述: 设置打印机服务,法院名称,是否自动打印
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/1 13:46
     */
    @RequestMapping("updatePrintService")
    @ResponseBody
    public void updatePrintService(Program_Setting programSetting){
        printFileService.updatePrintService(programSetting);
    }

}
