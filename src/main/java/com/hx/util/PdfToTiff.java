package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/19 9:24
 *@功能:
 */


import org.apache.commons.lang.ArrayUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.hx.common.StaticFinal.PdfToFileDir;

public class PdfToTiff {
    public static List<String> pdfToTiff(String pdfPath) throws Exception{
        String imageDirPath="D:\\pdfTtif";
        //将存储在服务器的PDF临时文件转换为byte[]数组
        File file=new File( pdfPath );
        byte[] pdfContent = File2byte(file);
        List<String> pathList=convertPdf2Image(pdfContent, imageDirPath);
        return pathList;
    }

    private static List<String> convertPdf2Image(byte[] pdfContent, String imageDirPath) throws Exception{
        if (ArrayUtils.isEmpty(pdfContent)) {
            return null;
        }
        File file=new File( imageDirPath );
        if(!file.exists()){
            file.mkdir();
        }
        PDDocument document = null;
        BufferedOutputStream outputStream = null;
        List<String> pathList=new ArrayList<>(  );
        try {
            document = PDDocument.load(pdfContent);
            int pageCount = document.getNumberOfPages();
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            String tifSavePath;
            for (int i = 0; i < pageCount; i++) {
                String fileName=GetTimeToFileName.GetTimeToFileName()+".tif";
                tifSavePath = PdfToFileDir + "\\" +fileName;
                outputStream = new BufferedOutputStream(new FileOutputStream(tifSavePath));
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, 200, ImageType.RGB);
                ImageIO.write(image, "tif", outputStream);
                outputStream.close();
                pathList.add( tifSavePath );
            }
        }catch (IOException e) {
            throw new Exception( "IO异常:"+e );
        }finally {
            closeSilently(outputStream);
            closeSilently(document);
        }
        return pathList;
    }

    // IOUtil.closeSilently 代码  关闭io
    public static void closeSilently(Closeable io) throws Exception{
        if (io != null) {
            io.close();
        }
    }
    //转换为byte[]
    public static byte[] File2byte(File tradeFile)throws Exception{
        byte[] buffer = null;
        FileInputStream fis = new FileInputStream(tradeFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1)
        {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        buffer = bos.toByteArray();
        return buffer;
    }

}
