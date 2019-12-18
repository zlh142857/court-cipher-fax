package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/8/26 16:30
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.modle.Program_Setting;
import com.hx.service.PrintFileService;
import com.hx.util.GetTimeToFileName;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.co.mmscomputing.device.scanner.ScannerIOException;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
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
    public String print(String tifPath){
        String str="";
        try{
            str=printFileService.printFile(tifPath);
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
        return JSONObject.toJSONStringWithDateFormat( map,"HH:mm:ss" );
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
    public boolean updatePrintService(Program_Setting programSetting){
        System.out.println(programSetting.toString());
        boolean flag=printFileService.updatePrintService(programSetting);
        return flag;
    }
    /**
     *
     * 功能描述: 发件箱,发回执箱文件下载,转换成PDF文件
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/22 13:48
     */
    @RequestMapping("downFileSend")
    @ResponseBody
    public void downFileSend(String tifPath, HttpServletResponse response){
        String pdfPath="";
        String fileName=GetTimeToFileName.GetTimeToFileName()+".pdf";
        try {
            if(null!=tifPath){
                pdfPath=printFileService.downFileSend(tifPath);
            }else{
                logger.error( "参数为空" );
            }
            //设置文件路径
            File file = new File(pdfPath);
            if (file.exists()) {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    logger.error( e );
                }
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
    @RequestMapping("downTifSign")
    @ResponseBody
    public void downTifSign(String tifPath, HttpServletResponse response){
        String pngPath="";
        String fileName=GetTimeToFileName.GetTimeToFileName()+".pdf";
        try {
            if(null!=tifPath){
                pngPath=printFileService.downTifSign(tifPath);
            }else{
                logger.error( "参数为空" );
            }
            //设置文件路径
            File file = new File(pngPath);
            if (file.exists()) {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    logger.error( e );
                }
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
        /*String filename=tifPath.substring(tifPath.lastIndexOf("/"),tifPath.length()-1);
        //如果下载文件是中文文件，则文件名需要经过url编码
        try {
            response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(filename,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InputStream in=null;
        OutputStream out=null;
        try{
            in = new FileInputStream(tifPath);
            int len=0;
            byte buffer[] = new byte[1024];
            out = response.getOutputStream();
            while((len = in.read(buffer))>0){
            out.write(buffer,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(in != null){
                try{
                    in.close();
                }catch(Exception e){
                }
            }
        }*/
    }

}
