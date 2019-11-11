package com.hx.change;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/23 15:01
 *@功能:
 */

import com.hx.util.GetTimeToFileName;
import com.hx.util.TempDir;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codecimpl.TIFFImageDecoder;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.sun.media.jai.codec.TIFFField;
import sun.misc.BASE64Decoder;

import static com.hx.common.StaticFinal.TEMPDIR;
import static com.hx.util.TempDir.fileTemp;

public class ChangeFile {
    private static Logger logger=Logger.getLogger(ChangeFile.class);
    //将Word转换为PDF文档,返回值为PDF文件保存路径
    public static String wordToPDF(MultipartFile file)throws IOException{
        ActiveXComponent app = null;
        Dispatch doc = null;
        String pdfFileName=GetTimeToFileName.GetTimeToFileName()+".pdf";
        String docFilePath="";
        String pdfPath=TEMPDIR+"/"+pdfFileName;
        try {
            //将文件先保存再将路径传给doc
            docFilePath=TempDir.makeTempDir(file);
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();
            //获取文件路径
            doc = Dispatch.call(docs, "Open", docFilePath).toDispatch();
            //转换为PDF文件,保存文件到tempDir文件夹
            Dispatch.call(doc, "SaveAs", pdfPath, // FileName
                    17);//17是pdf格式
        } catch (IOException e) {
            throw new IOException("word转换PDF IO异常：" + e);
        } finally {
            Dispatch.call(doc, "Close", new Variant(false));
            if (app != null){
                app.invoke("Quit", new Variant[]{});
            }
        }
        // 如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
        return pdfPath;
    }


    static {
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
    }
    /** 图片格式 */
    public static final String IMG_FORMAT = "TIFF";

    /** 打印精度设置 */
    public static final float DPI = 209f; //图片的像素
    public static boolean pdfToTiff(MultipartFile file,OutputStream os) throws IOException {
        boolean back=false;
        InputStream is=file.getInputStream();
        PDDocument doc = null;
        try {
            doc = PDDocument.load(is);
            int pageCount = doc.getNumberOfPages();
            PDFRenderer renderer = new PDFRenderer(doc); // 根据PDDocument对象创建pdf渲染器
            List<PlanarImage> piList = new ArrayList<PlanarImage>();
            for (int i = 0+1; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, DPI,
                        ImageType.BINARY);
                PlanarImage pimg = JAI.create("mosaic", image);
                piList.add(pimg);
            }
            TIFFEncodeParam param = new TIFFEncodeParam();// 创建tiff编码参数类
            param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP3_2D);// 压缩参数
            param.setT4PadEOLs( false );
            param.setReverseFillOrder( false );
            param.setT4Encode2D( false );
            param.setWriteTiled( false );
            param.setLittleEndian( false );
            TIFFField[] extras = new TIFFField[2];
            extras[0] = new TIFFField(TIFFImageDecoder.TIFF_X_RESOLUTION,
                    TIFFField.TIFF_RATIONAL, 1, (Object) new long[][] {{ (long) 408, 2 } });
            extras[1] = new TIFFField(TIFFImageDecoder.TIFF_Y_RESOLUTION,
                    TIFFField.TIFF_RATIONAL, 1, (Object) new long[][] {{ (long) 392, 2 } });
            param.setExtraFields(extras);
            param.setExtraImages(piList.iterator());// 设置图片的迭代器
            BufferedImage fimg = renderer.renderImageWithDPI(0, DPI,ImageType.BINARY);
            PlanarImage fpi = JAI.create("mosaic",fimg); // 通过JAI的create()方法实例化jai的图片对象
            ImageEncoder enc = ImageCodec.createImageEncoder(IMG_FORMAT, os,
                    param);
            enc.encode(fpi);// 指定第一个进行编码的jai图片对象,并将输出写入到与此
            back=true;
        } catch (IOException e) {
            throw new IOException( e );
        } finally {
            try {
                if (doc != null)
                    doc.close();
                if (os != null){
                    os.close();
                }
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                throw new IOException( e );
            }
        }
        return back;
    }
    public static boolean pdfToTiffByWord(File file,OutputStream os) throws IOException {
        boolean back=false;
        InputStream is=new FileInputStream( file );
        PDDocument doc = null;
        try {
            doc = PDDocument.load(is);
            int pageCount = doc.getNumberOfPages();
            PDFRenderer renderer = new PDFRenderer(doc); // 根据PDDocument对象创建pdf渲染器
            List<PlanarImage> piList = new ArrayList<PlanarImage>();
            for (int i = 0+1; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, DPI,
                        ImageType.BINARY);
                PlanarImage pimg = JAI.create("mosaic", image);
                piList.add(pimg);
            }
            TIFFEncodeParam param = new TIFFEncodeParam();// 创建tiff编码参数类
            param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP3_2D);// 压缩参数
            param.setT4PadEOLs( false );
            param.setReverseFillOrder( false );
            param.setT4Encode2D( false );
            param.setWriteTiled( false );
            param.setLittleEndian( false );
            TIFFField[] extras = new TIFFField[2];
            extras[0] = new TIFFField(TIFFImageDecoder.TIFF_X_RESOLUTION,
                    TIFFField.TIFF_RATIONAL, 1, (Object) new long[][] {{ (long) 408, 2 } });
            extras[1] = new TIFFField(TIFFImageDecoder.TIFF_Y_RESOLUTION,
                    TIFFField.TIFF_RATIONAL, 1, (Object) new long[][] {{ (long) 392, 2 } });
            param.setExtraFields(extras);
            param.setExtraImages(piList.iterator());// 设置图片的迭代器
            BufferedImage fimg = renderer.renderImageWithDPI(0, DPI,ImageType.BINARY);
            PlanarImage fpi = JAI.create("mosaic",fimg); // 通过JAI的create()方法实例化jai的图片对象
            ImageEncoder enc = ImageCodec.createImageEncoder(IMG_FORMAT, os,
                    param);
            enc.encode(fpi);// 指定第一个进行编码的jai图片对象,并将输出写入到与此
            back=true;
        } catch (IOException e) {
            throw new IOException( e );
        } finally {
            try {
                if (doc != null)
                    doc.close();
                if (os != null){
                    os.close();
                }
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                throw new IOException( e );
            }
        }
        return back;
    }
    public static String baseToPdf(String base64){
        String pdfPath="";
        BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        BufferedInputStream bin = null;
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        try {
            pdfPath=fileTemp()+".pdf";
            // 将base64编码的字符串解码成字节数组
            byte[] bytes = decoder.decodeBuffer(base64);
            // 创建一个将bytes作为其缓冲区的ByteArrayInputStream对象
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            // 创建从底层输入流中读取数据的缓冲输入流对象
            bin = new BufferedInputStream(bais);
            // 指定输出的文件
            File file = new File(pdfPath);
            // 创建到指定文件的输出流
            fout = new FileOutputStream(file);
            // 为文件输出流对接缓冲输出流对象
            bout = new BufferedOutputStream(fout);
            byte[] buffers = new byte[1024];
            int len = bin.read(buffers);
            while (len != -1) {
                bout.write(buffers, 0, len);
                len = bin.read(buffers);
            }
            bout.flush();
            fout.flush();
        } catch (IOException e) {
            logger.error( "IO异常:"+e.toString() );
        } finally {
            try {
                if(bin!=null){
                    bin.close();
                }
                if(fout!=null){
                    fout.close();
                }
                if(bout!=null){
                    bout.close();
                }
            } catch (IOException e) {
                logger.error( "IO异常:"+e.toString() );
            }
        }
        return pdfPath;
    }
    //扫描生成的jpg转换为一份PDF文件
    public static void jpgToPdf(String outPdfFilepath, List<String> list) throws Exception {
        File file = new File(outPdfFilepath);
        // 第一步：创建一个document对象。
        Document document = new Document();
        document.setMargins(0, 0, 0, 0);
        // 创建一个PdfWriter实例，
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        // 第四步：在文档中增加图片。
        int len = list.size();
        for (int i = 0; i < len; i++) {
            String temp = list.get( i );
            Image img = Image.getInstance(temp);
            img.setAlignment(Image.ALIGN_CENTER);
            img.scaleAbsolute(597, 844);// 直接设定显示尺寸
            // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
            //document.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
            document.setPageSize(new Rectangle(597, 844));
            document.newPage();
            document.add(img);
        }
        // 第五步：关闭文档。
        document.close();
    }
}
