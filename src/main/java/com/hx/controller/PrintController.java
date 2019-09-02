package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/8/26 16:30
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.*;

@Controller
public class PrintController implements Printable {
    @RequestMapping("print")
    @ResponseBody
    public String print(){
        try {
            JFileChooser fileChooser = new JFileChooser( "D://ss.pdf" ); // 创建打印作业
            System.out.println("创建打印作业");
            int state = fileChooser.showOpenDialog( null );
            if (state == fileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile(); // 获取选择的文件
                System.out.println("获取选择的文件");
                // 构建打印请求属性集
                HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                // 设置打印格式，因为未确定类型，所以选择autosense
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                // 查找所有的可用的打印服务
                PrintService printService[] = PrintServiceLookup
                        .lookupPrintServices( flavor, pras );
                System.out.println( "查找所有的可用的打印服务"+printService.length );
                // 定位默认的打印服务
                PrintService defaultService = PrintServiceLookup
                        .lookupDefaultPrintService();
                System.out.println( "定位默认的打印服务" );
                // 显示打印对话框
                PrintService service = ServiceUI.printDialog( null, 200, 200,
                        printService, defaultService, flavor, pras );
                System.out.println( "显示打印对话框" );
                if (service != null) {
                    DocPrintJob job = service.createPrintJob(); // 创建打印作业
                    System.out.println( "创建打印作业" );
                    FileInputStream fis = new FileInputStream( file ); // 构造待打印的文件流
                    System.out.println( "构造待打印的文件流" );
                    System.out.println( file.getName() );
                    DocAttributeSet das = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc( fis, flavor, das );
                    System.out.println( "doc.getAttributes():"+doc.getAttributes() );
                    job.print( doc, pras ); // 进行每一页的具体打印操作
                }
            } else {
                // 如果打印内容为空时，提示用户打印将取消
                JOptionPane.showConfirmDialog( null,
                        "Sorry, Printer Job is Empty, Print Cancelled!",
                        "Empty", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE );
            }
        }catch (PrintException pe) {
            pe.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString( "成功" );
    }
    @RequestMapping("printT")
    @ResponseBody
    public String printT(){
        // 构建打印请求属性集
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        // 设置打印格式，因为未确定类型，所以选择autosense
        DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
        // 打印参数
        DocAttributeSet das = new HashDocAttributeSet();
        InputStream fis = null;
        das.add(new Copies(2)); //份数
        // aset.add(MediaSize.ISO.A4); //纸张
        // aset.add(Finishings.STAPLE);//装订
        // 查找所有的可用的打印服务
        PrintService printService[] = PrintServiceLookup
                .lookupPrintServices( flavor, pras );
        for(int i=0;i<printService.length;i++){
            String servicename=printService[i].getName();
            String subname=servicename.substring( 0,2 );
            if(subname.equals("HP" )){
                System.out.println("存在");
                // 定位默认的打印服务
                PrintService defaultService = PrintServiceLookup
                        .lookupDefaultPrintService();
                DocPrintJob docPrintJobs=printService[i].createPrintJob();
                docPrintJobs.addPrintJobListener(new PrintJobListener() {
                    @Override
                    public void printJobRequiresAttention(PrintJobEvent arg0) {
                        System.out.println("printJobRequiresAttention");
                    }

                    @Override
                    public void printJobNoMoreEvents(PrintJobEvent arg0) {
                        System.out.println("通知客户端,不需要再提供事件");
                    }

                    @Override
                    public void printJobFailed(PrintJobEvent arg0) {
                        System.out.println("通知客户端无法完成作业,必须重新提交");

                    }

                    @Override
                    public void printJobCompleted(PrintJobEvent arg0) {
                        System.out.println("打印结束");

                    }

                    @Override
                    public void printJobCanceled(PrintJobEvent arg0) {
                        System.out.println("作业已被用户或者程序取消");

                    }

                    @Override
                    public void printDataTransferCompleted(PrintJobEvent arg0) {
                        System.out.println("数据已成功传输打印机");

                    }
                });
                File file=new File( "D:\\软件架构.jpg" );
                try {
                    fis = new FileInputStream(file);
                    Doc doc = new SimpleDoc(fis, flavor, das);
                    //3.生成一个打印属性设置对象
                    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                    docPrintJobs.print( doc,aset );
                    System.out.println("打印结束");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (PrintException  e) {
                    System.out.println("当前异常:"+e);
                } catch (Exception e) {
                    System.out.println("当前打印异常:"+e);
                }finally {
                    // 关闭打印的文件流
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                System.out.println("当前服务名VU为:"+printService[i].getName());
            }
        }
        return JSONObject.toJSONString( "成功" );
    }
    @RequestMapping("printD")
    public void printD(){
        File file=new File("C:\\Program Files (x86)\\HP\\HP ColorLaserJet MFP M178-M181\\bin\\HPScan.exe");
        if(file.exists()){
            try {
                java.awt.Desktop.getDesktop().open(file);
            } catch (IOException e) {
                System.out.println("异常"+e);
            }
        }else{
            System.out.println("HPScan.exe不存在");
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
    @RequestMapping("printW")
    @ResponseBody
    public String printW(){
        String pdfFile = "D:\\ss.pdf";//文件路径
        File file = new File(pdfFile);
        String printerName = "HP";
        try {
            PDFprint(file,printerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString( "成功" );
    }
    public static void PDFprint(File file ,String printerName) throws Exception {
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setJobName(file.getName());
            if (printerName != null) {
                // 查找并设置打印机
                //获得本台电脑连接的所有打印机
                PrintService[] printServices = PrinterJob.lookupPrintServices();
                if(printServices == null || printServices.length == 0) {
                    System.out.print("打印失败，未找到可用打印机，请检查。");
                    return ;
                }
                PrintService printService = null;
                //匹配指定打印机
                for (int i = 0;i < printServices.length; i++) {
                    System.out.println(printServices[i].getName());
                    if (printServices[i].getName().contains(printerName)) {
                        printService = printServices[i];
                        break;
                    }
                }
                if(printService!=null){
                    printJob.setPrintService(printService);
                }else{
                    System.out.print("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
                    return ;
                }
            }
            //设置纸张及缩放
            PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
            //设置多页打印
            Book book = new Book();
            PageFormat pageFormat = new PageFormat();
            //设置打印方向
            pageFormat.setOrientation(PageFormat.PORTRAIT);//纵向

            pageFormat.setPaper(getPaper());//设置纸张

            book.append(pdfPrintable, pageFormat, document.getNumberOfPages());
            printJob.setPageable(book);
            printJob.setCopies(2);//设置打印份数
            //添加打印属性
            HashPrintRequestAttributeSet pars = new HashPrintRequestAttributeSet();
            pars.add(Sides.DUPLEX); //设置单双页
            printJob.print(pars);
        }finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public static Paper getPaper() {
        Paper paper = new Paper();
        // 默认为A4纸张，对应像素宽和高分别为 595, 842
        int width = 595;
        int height = 842;
        // 设置边距，单位是像素，10mm边距，对应 28px
        int marginLeft = 10;
        int marginRight = 0;
        int marginTop = 10;
        int marginBottom = 0;
        paper.setSize(width, height);
        // 下面一行代码，解决了打印内容为空的问题
        paper.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
        return paper;
    }
}
