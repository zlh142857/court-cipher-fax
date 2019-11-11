package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/10 10:39
 *@功能:
 */

import com.hx.dao.ProgramSettingDao;
import com.hx.modle.Program_Setting;
import com.hx.modle.TempModel;
import com.hx.service.PrintFileService;
import com.hx.util.PrintImage;
import com.hx.util.TwainExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import static com.hx.change.ChangeFile.jpgToPdf;
import static com.hx.change.ChangeFile.pdfToTiffByWord;
import static com.hx.util.ColorReverse.writeJpg;
import static com.hx.util.TempDir.fileTemp;
import static com.hx.util.TiffToJPEG.readerTiff;


@Service("printService")
public class PrintFileServiceImpl implements PrintFileService {
    @Autowired
    private ProgramSettingDao programSettingDao;
    //tif文件先转换成jpg
    @Override
    public String printFile(List<TempModel> tempModels) throws Exception {
        int fileLength=tempModels.size();
        if(fileLength==0){
            return "请上传有效文件";
        }
        for(int i=0;i<fileLength;i++){
            //先判断文件是否为空,不为空则继续,为空循环获取下一个文件
            int len=tempModels.get( i ).getTifPath().length();
            if(len>0){
                //查询打印服务名称
                Program_Setting programSetting=programSettingDao.selectProgramSetting();
                String printService=programSetting.getPrintService();
                //先把tif转换为jpg的list集合,然后进行染色反转,再打印
                List<String> jpgList=readerTiff(tempModels.get( i ).getTifPath());
                //颜色反转
                List<File> newList=writeJpg(jpgList);
                //打印
                PrintImage.printImageWhenReceive(newList,printService);
            }
        }
        return "打印结束";
    }
    //扫描功能,返回文件保存路径
    @Override
    public String printScan() throws Exception{
        String tifPath=fileTemp()+".tif";
        String pdfPath=fileTemp()+".pdf";
        List<String> list=null;
        boolean flag=false;
        TwainExample.app=new TwainExample();
        for(;;){
            Thread.sleep( 1000 );
            int count=TwainExample.count;
            if(count==1){
                list=TwainExample.list;
                if(list.size()>0){
                    jpgToPdf(pdfPath,list);
                    File file=new File(pdfPath);
                    OutputStream os=new FileOutputStream( new File( tifPath ) );
                    flag=pdfToTiffByWord(file,os);
                }
                TwainExample.count=0;
                break;
            }
        }
        if(!flag){
            tifPath="";
        }
        return tifPath;
    }
    //程序管理下拉列表查询
    @Override
    public Map<String,Object> selectPrintService() {
        Map<String,Object> map=new HashMap<>(  );
        List<String> list=new ArrayList<>(  );
        //查询数据库存入的服务名称
        Program_Setting programSetting=programSettingDao.selectProgramSetting();
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
}
