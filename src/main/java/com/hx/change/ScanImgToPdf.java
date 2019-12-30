package com.hx.change;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/30 10:03
 *@功能:
 */
import java.io.*;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;

public class ScanImgToPdf {
    private static Logger logger=Logger.getLogger( ScanImgToPdf.class );
    public static boolean imgOfPdf(List<String> list,String pdfPath) {
        boolean result = false;
        try {
            File file = Pdf(list, pdfPath);//生成pdf
            file.createNewFile();
            result = true;
        } catch (IOException e) {
            result = false;
            logger.error( e.toString() );
        }
        return result;
    }
    public static File Pdf(List<String> list, String mOutputPdfFileName) {
        Document doc = new Document(PageSize.A4, 20, 20, 20, 20); //new一个pdf文档
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName)); //pdf写入
            doc.open();//打开文档
            for (int i = 0; i < list.size(); i++) {  //循环图片List，将图片加入到pdf中
                doc.newPage();  //在pdf创建一页
                Image png1 = Image.getInstance(list.get(i)); //通过文件路径获取image
                float heigth = png1.getHeight();
                float width = png1.getWidth();
                int percent = getPercent2(heigth, width);
                png1.setAlignment(Image.MIDDLE);
                png1.scalePercent(percent+3);// 表示是原来图像的比例;
                doc.add(png1);
            }
            doc.close();
        } catch (FileNotFoundException e) {
            logger.error( e.toString() );
        } catch (DocumentException e) {
            logger.error( e.toString() );
        } catch (IOException e) {
            logger.error( e.toString() );
        }

        File mOutputPdfFile = new File(mOutputPdfFileName);  //输出流
        if (!mOutputPdfFile.exists()) {
            mOutputPdfFile.deleteOnExit();
            return null;
        }
        return mOutputPdfFile; //反回文件输出流
    }

    public static int getPercent(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }
    public static int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }

}
