package com.hx.change;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/12 11:15
 *@功能:
 */

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static com.hx.util.TempDir.fileTemp;

public class ImgToPdf {
    private static Logger logger=Logger.getLogger( ImgToPdf.class );
    //参数,文件路径的list集合,要保存的PDF路径
    public static void imgToPdf(List<String> files,String pdfPath) throws Exception{
        ImgUtil imageUtil = new ImgUtil();
        List<String> newList=new ArrayList<>(  );
        for (int i=0;i<files.size();i++) {
            String file = files.get( i );
            //读取图片
            BufferedImage bi = imageUtil.rotateImage(file);
            if (bi==null)
                continue;
            //将每一份图片都单独转换成一份PDF,然后将PDF路径替换图片路径
            String pdffile = Img2PDF(file,bi);
            newList.add( pdffile );
        }
        mergePDF(newList,pdfPath);
    }
    public static String Img2PDF(String imagePath, BufferedImage img){
        String pdfPath = "";
        FileOutputStream fos=null;
        try {
            //图片操作
            Image image = null;
            pdfPath = fileTemp()+".pdf";
            String type = imagePath.substring(imagePath.lastIndexOf(".")+1);
            Document doc = new Document(null, 0, 0, 0, 0);

            //更换图片图层
            BufferedImage bufferedImage = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
            bufferedImage.getGraphics().drawImage(img, 0,0, img.getWidth(), img.getHeight(), null);
            bufferedImage=new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null).filter (bufferedImage,null);

            //图片流处理
            doc.setPageSize(new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, type, out);
            byte[] b = out.toByteArray();
            image = Image.getInstance(b);

            fos = new FileOutputStream(pdfPath);
            PdfWriter writer = PdfWriter.getInstance(doc, fos);
            doc.open();
            doc.add(image);
            doc.close();

        } catch (IOException e) {
            logger.error( e.toString() );
        } catch (BadElementException e) {
            logger.error( e.toString() );
        } catch (DocumentException e) {
            logger.error( e.toString() );
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
        }
        return pdfPath;
    }

    /**
     * @Author Ragty
     * @Description 合成PDF
     * @Date 17:25 2019/3/7
     * @return
     **/
    public static void mergePDF(List<String> files,String pdfPath) throws Exception{
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        for (String file :files) {
            if (file.toLowerCase().endsWith("pdf"))
                mergePdf.addSource(file);
        }
        mergePdf.setDestinationFileName(pdfPath);
        mergePdf.mergeDocuments();
    }



}
