package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/10 10:59
 *@功能:
 */


import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class PrintImage {
    public static void printImageWhenReceive(List<File> list,String printServiceName)throws Exception{
        if(printServiceName.length()>0){
            FileInputStream inputStream=null;
            int count=0;
            for(int i=0;i<list.size();i++){
                File file=list.get( i );
                inputStream=new FileInputStream( file );
                // 构建打印请求属性集
                HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                DocFlavor flavor=DocFlavor.INPUT_STREAM.JPEG;
                // 打印参数
                DocAttributeSet das = new HashDocAttributeSet();
                // 查找所有的可用的打印服务
                PrintService printService[] = PrintServiceLookup.lookupPrintServices( flavor, pras );
                if(printService == null || printService.length == 0) {
                    throw new Exception( "打印失败，未找到可用打印机，请检查。" );
                }else{
                    for(int j=0;j<printService.length;j++) {
                        if (printService[j].getName().contains(printServiceName)) {
                            DocPrintJob docPrintJobs = printService[j].createPrintJob();
                            Doc doc = new SimpleDoc(inputStream, flavor, das);
                            //3.生成一个打印属性设置对象
                            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                            docPrintJobs.print( doc,aset );
                            count=1;
                            break;
                        }
                    }
                }
            }
            if(count==0){
                throw new Exception( "打印失败，未找到指定打印机" );
            }
        }
    }

    public static void printImage(String fileEnd,InputStream inputStream)throws Exception{
        int count=0;
        // 构建打印请求属性集
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        DocFlavor flavor=null;
        if(fileEnd.equals( "jpg" )){
            flavor = DocFlavor.INPUT_STREAM.JPEG;
        }
        // 打印参数
        DocAttributeSet das = new HashDocAttributeSet();
        // 查找所有的可用的打印服务
        PrintService printService[] = PrintServiceLookup.lookupPrintServices( flavor, pras );
        if(printService == null || printService.length == 0) {
            System.out.print("打印失败，未找到可用打印机，请检查。");
            throw new Exception( "打印失败，未找到可用打印机，请检查。" );
        }else{
            for(int i=0;i<printService.length;i++) {
                if (printService[i].getName().contains("HP")) {
                    DocPrintJob docPrintJobs = printService[i].createPrintJob();
                    Doc doc = new SimpleDoc(inputStream, flavor, das);
                    //3.生成一个打印属性设置对象
                    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                    docPrintJobs.print( doc,aset );
                    count=1;
                    break;
                }
            }
        }
        if(count==0){
            throw new Exception( "打印失败，未找到惠普打印机" );
        }

    }
}
