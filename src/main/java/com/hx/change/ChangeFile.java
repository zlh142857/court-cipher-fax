package com.hx.change;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/23 15:01
 *@功能:
 */

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.github.jaiimageio.impl.plugins.tiff.TIFFImageReader;
import com.github.jaiimageio.impl.plugins.tiff.TIFFImageReaderSpi;
import com.github.jaiimageio.impl.plugins.tiff.TIFFImageWriter;
import com.github.jaiimageio.impl.plugins.tiff.TIFFImageWriterSpi;
import com.github.jaiimageio.plugins.tiff.BaselineTIFFTagSet;
import com.github.jaiimageio.plugins.tiff.TIFFDirectory;
import com.github.jaiimageio.plugins.tiff.TIFFField;
import com.github.jaiimageio.plugins.tiff.TIFFTag;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import sun.misc.BASE64Decoder;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import static com.hx.common.StaticFinal.LICENSE;
import static com.hx.common.StaticFinal.TEMPDIR;
import static com.hx.util.TempDir.fileTemp;

public class ChangeFile {
    private static Logger logger=Logger.getLogger(ChangeFile.class);
    //将Word转换为PDF文档,返回值为PDF文件保存路径
    public static String wordToPDF(MultipartFile file)throws IOException{
        String docName=file.getOriginalFilename();
        String docPath=TEMPDIR+"/"+docName;//Word保存路径,Document加载此路径
        File dest = new File(docPath);
        file.transferTo(dest); // 保存文件
        String pdfPath=fileTemp()+".pdf";
        FileOutputStream os=null;
        if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
            return null;
        }
        try {
            File pdfFile = new File(pdfPath);  //新建一个空白pdf文档
            os = new FileOutputStream(pdfFile);
            Document doc = new Document(docPath);                    //Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
        } catch (Exception e) {
            logger.error( e.toString() );
        }finally {
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
        }
        return pdfPath;
    }
    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is =new FileInputStream(new File(LICENSE)); //
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            logger.error( e.toString() );
        }
        return result;
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
    //将文件转换成BufferedImage,再将BufferedImage转换为tiff,转换为tiff时,设置metadata,像素设置,颜色设置是在生成BufferedImage的时候进行设置
    public static final float DPI = 209f;
    public static boolean PdfToTiff(InputStream is,ImageOutputStream os) throws IOException {
        boolean bres = false;
        PDDocument doc = null;
        doc = PDDocument.load(is);
        int pageCount = doc.getNumberOfPages();
        PDFRenderer renderer = new PDFRenderer(doc); // 根据PDDocument对象创建pdf渲染器
        List<BufferedImage> biList = new ArrayList<BufferedImage>();
        for (int i = 0; i < pageCount; i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, DPI,ImageType.BINARY);
            biList.add(image);
        }
        //tiff格式图片输出流
        TIFFImageWriter tiffImageWriter = new TIFFImageWriter(new TIFFImageWriterSpi());
        ImageWriteParam writerParams = tiffImageWriter.getDefaultWriteParam();
        writerParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writerParams.setCompressionType("CCITT T.6");
        IIOMetadata metadata=createMetadata(tiffImageWriter,writerParams);
        try {
            //先指定一个文件用于存储输出的数据
            tiffImageWriter.setOutput(os);
            //指定第一个tif文件写到指定的文件中
            BufferedImage bufferedImage_0 = biList.get(0);
            //IIOImage类是用于存储    图片/缩略图/元数据信息    的引用类
            IIOImage iioImage_0 = new IIOImage(bufferedImage_0, null, metadata);
            //write方法,将给定的IIOImage对象写到文件系统中;
            tiffImageWriter.write(metadata,iioImage_0,writerParams);
            for (int i = 1; i < biList.size(); i++) {
                //判断该输出流是否可以插入新图片到文件系统中的
                if(tiffImageWriter.canInsertImage(i)){
                    //根据顺序获取缓冲中的图片;
                    BufferedImage bufferedImage = biList.get(i);
                    IIOImage iioImage = new IIOImage(bufferedImage, null, metadata);
                    //将文件插入到输出的多图片文件中的指定的下标处
                    tiffImageWriter.writeInsert(i, iioImage,writerParams);
                }
            }
            bres = true;
        } catch (IOException e) {
            logger.error( e.toString() );
            bres = false;
        }
        return bres;
    }
    public static boolean tiffMerge(List<String> strList,ImageOutputStream os) throws IOException {
        boolean bres = false;
        List<File> fileList=new ArrayList<>(  );
        for(int i=0;i<strList.size();i++){
            File file=new File( strList.get( i ) );
            fileList.add( file );
        }
        //tiff格式的图片读取器;
        TIFFImageReader tiffImageReader = new TIFFImageReader(new TIFFImageReaderSpi());
        FileImageInputStream fis = null;
        List<BufferedImage> biList = new ArrayList<BufferedImage>();
        for(File f: fileList) {
            try {
                fis = new FileImageInputStream(f);
                tiffImageReader.setInput(fis);
                biList.add(tiffImageReader.read(0));
            }catch (Exception e) {
                logger.error( e.toString() );
            }finally {
                if(tiffImageReader != null) {
                    tiffImageReader.dispose();
                }
                if (fis != null) {
                    try {
                        fis.flush();
                        fis.close();
                    } catch (IOException e) {
                        logger.error( e.toString() );
                    }
                }
            }
        }
        //tiff格式图片输出流
        TIFFImageWriter tiffImageWriter = new TIFFImageWriter(new TIFFImageWriterSpi());
        ImageWriteParam writerParams = tiffImageWriter.getDefaultWriteParam();
        writerParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writerParams.setCompressionType("CCITT T.6");
        writerParams.setCompressionQuality( 1f );
        IIOMetadata metadata=createMetadata(tiffImageWriter,writerParams);
        try {
            //先指定一个文件用于存储输出的数据
            tiffImageWriter.setOutput(os);
            //指定第一个tif文件写到指定的文件中
            BufferedImage bufferedImage_0 = biList.get(0);
            //IIOImage类是用于存储    图片/缩略图/元数据信息    的引用类
            IIOImage iioImage_0 = new IIOImage(bufferedImage_0, null, metadata);
            //write方法,将给定的IIOImage对象写到文件系统中;
            tiffImageWriter.write(metadata,iioImage_0,writerParams);
            for (int i = 1; i < biList.size(); i++) {
                //判断该输出流是否可以插入新图片到文件系统中的
                if(tiffImageWriter.canInsertImage(i)){
                    //根据顺序获取缓冲中的图片;
                    BufferedImage bufferedImage = biList.get(i);
                    IIOImage iioImage = new IIOImage(bufferedImage, null, metadata);
                    //将文件插入到输出的多图片文件中的指定的下标处
                    tiffImageWriter.writeInsert(i, iioImage,writerParams);
                }
            }
            bres = true;
        } catch (IOException e) {
            logger.error( e.toString() );
            bres = false;
        }
        return bres;
    }
    private static IIOMetadata createMetadata(ImageWriter writer, ImageWriteParam writerParams)throws
            IIOInvalidTreeException {
        ImageTypeSpecifier type = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_BYTE_BINARY);
        IIOMetadata meta = writer.getDefaultImageMetadata(type, writerParams);
        TIFFDirectory dir = TIFFDirectory.createFromMetadata(meta);
        BaselineTIFFTagSet base = BaselineTIFFTagSet.getInstance();
        TIFFTag tagXRes = base.getTag(BaselineTIFFTagSet.TAG_X_RESOLUTION);
        TIFFTag tagWidth = base.getTag(BaselineTIFFTagSet.TAG_IMAGE_WIDTH);
        TIFFTag tagResu = base.getTag(BaselineTIFFTagSet.TAG_RESOLUTION_UNIT);
        TIFFTag tagYRes = base.getTag(BaselineTIFFTagSet.TAG_Y_RESOLUTION);
        TIFFTag tagRowsPS = base.getTag(BaselineTIFFTagSet.TAG_ROWS_PER_STRIP);
        TIFFField fieldXRes = new TIFFField(tagXRes, TIFFTag.TIFF_RATIONAL, 1, new long[][] { { 408, 2 } });//设置纵向分辨率
        TIFFField fieldYRes = new TIFFField(tagYRes, TIFFTag.TIFF_RATIONAL, 1, new long[][] { { 392, 2 } });//设置垂直分辨率
        TIFFField fieldResu = new TIFFField(tagResu, TIFFTag.TIFF_BYTE, 1, new byte[] { 2 });//设置分辨率单位
        TIFFField fieldRowsPS = new TIFFField(tagRowsPS, TIFFTag.TIFF_SSHORT, 1, new short[] { 2444});//设置RowsPerStrip,根据纵向像素进行设置
        TIFFField fieldWidth = new TIFFField(tagWidth, TIFFTag.TIFF_LONG, 1, new long[][]{{1724}});//设置RowsPerStrip,根据纵向像素进行设置
        dir.addTIFFField(fieldXRes);
        dir.addTIFFField(fieldYRes);
        dir.addTIFFField(fieldResu);
        dir.addTIFFField(fieldRowsPS);
        dir.addTIFFField(fieldWidth);
        return dir.getAsMetadata();
    }

    //拆分多页tif为多个单页tif,返回单页文件路径的list集合
    public static List<String> makeSingleTif(File fTiff) {
        boolean bres = false;
        FileImageInputStream fis = null;
        List<String> list=new ArrayList<>(  );
        try {
            TIFFImageReaderSpi tiffImageReaderSpi = new TIFFImageReaderSpi();
            TIFFImageReader tiffImageReader = new TIFFImageReader(tiffImageReaderSpi);
            fis = new FileImageInputStream(fTiff);
            tiffImageReader.setInput(fis);
            int numPages = tiffImageReader.getNumImages(true);
            for(int i=0; i<numPages; i++) {
                BufferedImage bi = tiffImageReader.read(i);
                String path=fileTemp()+".tif";
                File tif = new File(path);
                FileImageOutputStream fios = new FileImageOutputStream(tif);
                TIFFImageWriterSpi tiffImageWriterSpi = new TIFFImageWriterSpi();
                TIFFImageWriter tiffImageWriter = new TIFFImageWriter(tiffImageWriterSpi);
                tiffImageWriter.setOutput(fios);
                tiffImageWriter.write(bi);
                list.add( path );
            }
            bres=true;
        } catch (Exception e) {
            logger.error( e.toString() );
            bres = false;
        }finally {
            if (fis != null) {
                try {
                    fis.flush();
                    fis.close();
                } catch (IOException e) {
                    logger.error( e.toString() );
                }
            }
        }
        if(bres){
            return list;
        }else{
            return null;
        }
    }

}
