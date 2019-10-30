package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/10 12:09
 *@功能:
 */

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import javax.print.PrintService;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.InputStream;

public class PrintPdf {
    public static void printPdf(InputStream inputStream)throws Exception{
        PDDocument document = PDDocument.load(inputStream);
        PrintService printService = null;
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        if(printServices == null || printServices.length == 0) {
            throw new Exception( "打印失败，未找到可用打印机，请检查。" );
        }else{
            for (int i = 0;i < printServices.length; i++) {
                if (printServices[i].getName().contains("HP") && printServices[i].getName().contains("网络")) {
                    printService = printServices[i];
                    break;
                }
            }
        }
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if(printService!=null){
            printJob.setPrintService(printService);
        }else{
            throw new Exception( "打印失败，未找到HP的打印机，请检查。" );
        }
        //设置多页打印
        Book book = new Book();
        //设置纸张及缩放
        PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
        PageFormat pageFormat = new PageFormat();
        //设置打印方向
        pageFormat.setOrientation(PageFormat.PORTRAIT);//纵向
        pageFormat.setPaper(getPaper());//设置纸张
        book.append(pdfPrintable, pageFormat, document.getNumberOfPages());
        printJob.setPageable(book);
        printJob.setCopies(1);//设置打印份数
        printJob.print();
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
