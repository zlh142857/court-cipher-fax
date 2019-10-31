package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/8/26 16:30
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.dao.ProgramSettingDao;
import com.hx.service.PrintFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import uk.co.mmscomputing.device.scanner.ScannerIOException;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.util.Map;

@Controller
public class PrintFileController{

    @Autowired
    private PrintFileService printFileService;
    //打开扫描
    @RequestMapping("printScan")
    public void printScan(String[] var0) throws ScannerIOException {
        /*try {
            printFileService.printScan();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //new testTwain_Source(var0);
    }


    //扫描进电脑保存格式为PDF或者为jpg,png,接收到对面所发送传真格式为PDF,所以这里只打印PDF或者是jpg,png格式文件
    //在发送时需要转换为PDF格式再发送
    //此打印方法可以选择多份文件直接打印
    @RequestMapping("print")
    @ResponseBody
    public String print(@RequestParam("files") MultipartFile[] files){
        String str=printFileService.printFile(files);
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

}
