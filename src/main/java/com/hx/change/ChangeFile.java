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

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
            pdfPath=null;
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
    public static final float DPI2 = 209.1f;
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
        }finally {
            doc.close();
        }
        return bres;
    }
    public static boolean txtPdfToTiff(InputStream is,ImageOutputStream os) throws IOException {
        boolean bres = false;
        PDDocument doc = null;
        doc = PDDocument.load(is);
        int pageCount = doc.getNumberOfPages();
        PDFRenderer renderer = new PDFRenderer(doc); // 根据PDDocument对象创建pdf渲染器
        List<BufferedImage> biList = new ArrayList<BufferedImage>();
        for (int i = 0; i < pageCount; i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, DPI2,ImageType.BINARY);
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
        }finally {
            doc.close();
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
        TIFFField fieldRowsPS = new TIFFField(tagRowsPS, TIFFTag.TIFF_SSHORT, 1, new short[] { 24440});//设置RowsPerStrip,根据纵向像素进行设置
        TIFFField fieldWidth = new TIFFField(tagWidth, TIFFTag.TIFF_RATIONAL, 1, new long[][]{{1728}});//设置RowsPerStrip,根据纵向像素进行设置
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
    //加盖电子签章
    public static boolean mergeBothImageRightbottom(String negativeImagePath, String additionImagePath, String toPath) throws IOException{
        boolean flag=false;
        InputStream is= null;
        InputStream is2= null;
        OutputStream os = null;
        FileImageOutputStream fios=null;
        try{
            is=new FileInputStream(negativeImagePath);
            is2=new FileInputStream(additionImagePath);
            BufferedImage image=ImageIO.read(is);
            BufferedImage image2=ImageIO.read(is2);
            Graphics g=image.getGraphics();
            g.drawImage(image2,image.getWidth()-image2.getWidth(),image.getHeight()-image2.getHeight(),null);
            File file=new File( toPath );
            fios = new FileImageOutputStream(file);
            TIFFImageWriterSpi tiffImageWriterSpi = new TIFFImageWriterSpi();
            TIFFImageWriter tiffImageWriter = new TIFFImageWriter(tiffImageWriterSpi);
            tiffImageWriter.setOutput(fios);
            tiffImageWriter.write(image);
            flag=true;
        }catch(Exception e){
            logger.error( e.toString() );
            flag=false;
        }finally{
            if(os != null){
                os.close();
            }
            if(is2 != null){
                is2.close();
            }
            if(is != null){
                is.close();
            }
            if(fios != null){
                fios.close();
            }
        }
        return flag;
    }
    //生成电子签章,message最长19个字
    private static final int WIDTH = 500;//图片宽度
    private static final int HEIGHT = 500;//图片高度
    public static BufferedImage startGraphics2D(String message,String year,String centerName){
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 增加下面代码使得背景透明
        buffImg = g.getDeviceConfiguration().createCompatibleImage(WIDTH, HEIGHT, Transparency.TRANSLUCENT);
        g.dispose();
        g = buffImg.createGraphics();
        // 背景透明代码结束
        g.setColor(Color.RED);
        //设置锯齿圆滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //绘制圆
        int radius = HEIGHT/3;//周半径
        int CENTERX = WIDTH/2;//画图所出位置
        int CENTERY = HEIGHT/2;//画图所处位置
        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(CENTERX, CENTERY, CENTERX + radius, CENTERY + radius);
        g.setStroke(new BasicStroke(10));//设置圆的宽度
        g.draw(circle);
        //绘制中间的五角星
        g.setFont(new Font("宋体", Font.BOLD, 120));
        g.drawString("★", CENTERX-(120/2), CENTERY+(120/3));
        //添加姓名
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 30));// 写入签名
        g.drawString(centerName, CENTERX -(40), CENTERY +(30+50));
        //添加年份
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 20));// 写入签名
        g.drawString(year, CENTERX -(60), CENTERY +(30+80));
        //根据输入字符串得到字符数组
        String[] messages2 = message.split("",0);
        String[] messages = new String[messages2.length];
        System.arraycopy(messages2,0,messages,0,messages2.length);
        //输入的字数
        int ilength = messages.length;
        //设置字体属性
        int fontsize = 40;
        Font f = new Font("Serif", Font.BOLD, fontsize);
        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(message, context);
        //字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / ilength);
        //上坡度
        double ascent = -bounds.getY();

        int first = 0,second = 0;
        boolean odd = false;
        if (ilength%2 == 1){
            first = (ilength-1)/2;
            odd = true;
        }
        else{
            first = (ilength)/2-1;
            second = (ilength)/2;
            odd = false;
        }
        double radius2 = radius - ascent;
        double x0 = CENTERX;
        double y0 = CENTERY - radius + ascent;
        //旋转角度
        double a = 2*Math.asin(char_interval/(2*radius2));
        if (odd){
            g.setFont(f);
            g.drawString(messages[first], (float)(x0 - char_interval/2), (float)y0);
            //中心点的右边
            for (int i=first+1;i<ilength;i++){
                double aa = (i - first) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay - char_interval/2* Math.sin(aa)));
            }
            //中心点的左边
            for (int i=first-1;i>-1;i--){
                double aa = (first - i) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }
        } else {
            //中心点的右边
            for (int i=second;i<ilength;i++){
                double aa = (i - second + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay - char_interval/2* Math.sin(aa)));
            }
            //中心点的左边
            for (int i=first;i>-1;i--){
                double aa = (first - i + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }
        }
        return buffImg;
    }
    //logoText 水印内容
    //jpg添加页码水印   srcImgPath 源文件路径   newImagePath  新文件路径
    public static boolean markImageByText(String logoText, String srcImgPath,String newImagePath) {
        boolean flag=false;
        Color color=new Color( 0,0,0);
        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            java.awt.Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(new java.awt.Font("黑体", java.awt.Font.BOLD, 30));
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            g.drawString(logoText,  buffImg.getWidth()/2 , buffImg.getHeight()-140);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(newImagePath);
            ImageIO.write(buffImg, "JPG", os);
            flag=true;
        } catch (Exception e) {
            logger.error( e.toString() );
            flag=false;
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                logger.error( e.toString() );
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                logger.error( e.toString() );
            }
        }
        return flag;
    }
    //jpg和png转换成普通 tiff文件
    public static String pngToTif(MultipartFile file) throws IOException {
        String docName=file.getOriginalFilename();
        String docPath=TEMPDIR+"/"+docName;//Word保存路径,Document加载此路径
        File dest = new File(docPath);
        file.transferTo(dest); // 保存文件
        String pdfPath=fileTemp()+".tif";
        File f2 = new File(pdfPath);
        try {
            File file1=new File( docPath );
            BufferedImage bufferedImage = ImageIO.read(file1);
            BufferedImage newBufferedImage = new BufferedImage(1728, 2444,
                    BufferedImage.TYPE_BYTE_BINARY);//TYPE_BYTE_BINARY 压缩大小为原来的 24分之一
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, null);
            boolean rn = ImageIO.write(newBufferedImage, "tiff",f2);
            if (!rn) {
                pdfPath=null;
            }
        } catch (IOException e) {
            pdfPath=null;
            logger.error( e.toString());
        }
        return pdfPath;
    }
}
