package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/10 10:39
 *@功能:
 */

import com.hx.service.PrintFileService;
import com.hx.util.PrintImage;
import com.hx.util.PrintPdf;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.PrintException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@Service("printService")
public class PrintFileServiceImpl implements PrintFileService {
    @Override
    public String printFile(MultipartFile[] files) {
        int fileLength=files.length;
        if(fileLength==0){
            return "请上传有效文件";
        }
        for(int i=0;i<fileLength;i++){
            //先判断文件是否为空,不为空则继续,为空循环获取下一个文件
            if(!files[i].isEmpty()){
                //通过文件名获取后缀名
                int begin = files[i].getOriginalFilename().indexOf(".");
                int last = files[i].getOriginalFilename().length();
                String a = files[i].getOriginalFilename().substring(begin, last);
                //根据后缀名判断文件类型
                String fileEnd=null;
                InputStream inputStream=null;
                try{
                    inputStream=files[i].getInputStream();
                    if(a.equals(".jpg")){
                        fileEnd="jpg";
                        PrintImage.printImage(fileEnd,inputStream);
                    }else if(a.equals(".tif")){
                        fileEnd="tif";
                        PrintImage.printImage(fileEnd,inputStream);
                    }else if(a.equals(".pdf")){
                        PrintPdf.printPdf(inputStream);
                    }else{
                        return "暂不支持打印该文件格式";
                    }
                }catch (PrintException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    return e.toString();
                }finally{
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "打印结束";
    }

    @Override
    public void printScan() throws Exception{
        File file=new File("C:\\Program Files (x86)\\HP\\HP ColorLaserJet MFP M178-M181\\bin\\HPScan.exe");
        if(file.exists()){
            try {
                java.awt.Desktop.getDesktop().open(file);
            } catch (IOException e) {
                throw new Exception("打开扫描文件IO异常"+e);
            }
        }else{
            throw new Exception("未找到Scan.exe");
        }
    }
}
