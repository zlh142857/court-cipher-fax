package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/10 10:39
 *@功能:
 */

import com.hx.dao.ProgramSettingDao;
import com.hx.modle.Program_Setting;
import com.hx.service.PrintFileService;
import com.hx.util.PrintImage;
import com.hx.util.TwainExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hx.change.ChangeFile.tiffMerge;
import static com.hx.change.ImgToPdf.imgToPdf;
import static com.hx.util.TempDir.fileTemp;
import static com.hx.util.TempDir.schTask;
import static com.hx.util.TiffToJPEG.readerTiff;

@Service("printService")
public class PrintFileServiceImpl implements PrintFileService {
    @Autowired
    private ProgramSettingDao programSettingDao;
    //tif文件先转换成jpg
    @Override
    public String printFile(String tifPath) throws Exception {
        int fileLength=tifPath.length();
        if(fileLength==0){
            return "文件路径丢失";
        }
        File getFile=new File(tifPath);
        if(!getFile.isFile()){
            return "文件不存在";
        }
        if(fileLength>0){
            //查询打印服务名称
            Program_Setting programSetting=programSettingDao.selectProgramSetting();
            String printService=programSetting.getPrintService();
            //先把tif转换为jpg的list集合,然后进行染色反转,再打印
            List<String> jpgList=readerTiff(tifPath);
            List<File> newList=new ArrayList<>(  );
            for(int i=0;i<jpgList.size();i++){
                File file=new File( jpgList.get( i ) );
                newList.add( file );
            }
            //打印
            PrintImage.printImageWhenReceive(newList,printService);
        }
        return "打印结束";
    }
    //扫描功能,返回文件保存路径
    @Override
    public Map<String,Object> printScan() throws Exception{
        Map<String,Object> map=new HashMap<>(  );
        String tifPath=schTask();
        List<String> list=null;
        boolean flag=false;
        TwainExample.app=new TwainExample();
        for(;;){
            Thread.sleep( 1000 );
            int count=TwainExample.count;
            if(count==1){
                list=TwainExample.list;
                if(list.size()>0){
                    File file=new File( tifPath );
                    ImageOutputStream os=new FileImageOutputStream( file);
                    flag=tiffMerge( list, os );
                }
                TwainExample.list=new ArrayList<>(  );
                TwainExample.count=0;
                break;
            }
        }
        if(!flag){
            tifPath="";
            map.put( "tifPath",tifPath );
            return map;
        }else{
            map.put( "tifPath",tifPath );
            map.put( "pathList",list );
            return map;
        }
    }
    //程序管理下拉列表查询
    @Override
    public Map<String,Object> selectPrintService() {
        Map<String,Object> map=new HashMap<>(  );
        List<String> list=new ArrayList<>(  );
        //查询数据库存入的服务名称
        Program_Setting programSetting=programSettingDao.selectProgramSettingAll();
        System.out.println(programSetting.toString());
        //查询所有的服务名称
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        String name="";
        //可用的打印机列表(字符串数组)
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        for(int i=0;i<printService.length;i++){
            name=printService[i].getName();
            list.add( name );
        }
        map.put("programSetting",programSetting );
        map.put("serviceList",list);
        return map;
    }

    @Override
    public void updatePrintService(Program_Setting programSetting) {
        programSettingDao.updatePrintService(programSetting);
    }


    @Override
    public String downFileSend(String tifPath) throws Exception {
        File getFile=new File(tifPath);
        if(!getFile.isFile()){
            return "文件不存在";
        }
        String pdfPath=fileTemp()+".pdf";
        List<String> filePath=readerTiff(tifPath);
        imgToPdf(filePath,pdfPath);
        return pdfPath;
    }
}
