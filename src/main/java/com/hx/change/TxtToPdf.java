package com.hx.change;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/13 16:09
 *@功能:
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;

import java.io.*;

import static com.hx.util.TempDir.fileTemp;

public class TxtToPdf {
    private static Logger logger=Logger.getLogger( TxtToPdf.class );
    private static final String FONT = "C:\\Windows\\Fonts\\simhei.ttf";
    public static String text2pdf(InputStream is){
        Document document = new Document();
        String pdfPath=fileTemp()+".pdf";
        File file=new File( pdfPath );
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            PdfWriter.getInstance(document, os);
            document.open();
            //方法一：使用Windows系统字体(TrueType)
            BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                document.add(new Paragraph(str, font));
            }
            document.close();
        } catch (FileNotFoundException e) {
            pdfPath=null;
            logger.error( e.toString() );
        } catch (UnsupportedEncodingException e) {
            pdfPath=null;
            logger.error( e.toString() );
        } catch (IOException e) {
            pdfPath=null;
            logger.error( e.toString() );
        } catch (DocumentException e) {
            pdfPath=null;
            logger.error( e.toString() );
        }finally {
            if(os !=null){
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
        }
        return pdfPath;
    }

}
