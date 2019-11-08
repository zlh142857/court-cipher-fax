package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/22 14:09
 *@功能:
 */


import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.hx.common.StaticFinal.TEMPDIR;

public class ColorReverse {
    private static Logger logger=Logger.getLogger( ColorReverse.class );
    public static List<File> writeJpg(List<String> pathList) {
        List<File> fileList=new ArrayList<>(  );
        if(pathList.size()>0){
            for(int i=0;i<pathList.size();i++){
                //文件与BufferedImage间的转换
                BufferedImage bi=file2img(pathList.get( i ));  //读取图片
                BufferedImage bii=img_inverse(bi);
                String newFilePath=TEMPDIR+"/"+GetTimeToFileName.GetTimeToFileName()+i+".jpg";
                img2file(bii,"jpg",newFilePath);  //生成图片
                File file=new File(newFilePath);
                fileList.add( file );
            }
        }
        return fileList;
    }
    public static String writeJpgOne(String tifPath) {
        String newFilePath="";
        if(tifPath.length()>0){
            //文件与BufferedImage间的转换
            BufferedImage bi=file2img(tifPath);  //读取图片
            BufferedImage bii=img_inverse(bi);
            newFilePath=TEMPDIR+"/"+GetTimeToFileName.GetTimeToFileName()+".jpg";
            img2file(bii,"jpg",newFilePath);  //生成图片
        }
        return newFilePath;
    }
    //图片反色
    public static BufferedImage img_inverse(BufferedImage imgsrc) {
        try {
            //创建一个不带透明度的图片
            BufferedImage back=new BufferedImage(imgsrc.getWidth(), imgsrc.getHeight(),BufferedImage.TYPE_INT_RGB);
            int width = imgsrc.getWidth();
            int height = imgsrc.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = imgsrc.getRGB(j, i);
                    back.setRGB(j,i,0xFFFFFF-pixel);
                }
            }
            return back;
        } catch (Exception e) {
            logger.error( e.toString() );
            return null;
        }
    }
    //读取图片
    public static BufferedImage file2img(String imgpath) {
        try {
            BufferedImage bufferedImage=ImageIO.read(new File(imgpath));
            return bufferedImage;
        } catch (Exception e) {
            logger.error( e.toString() );
            return null;
        }
    }
    //保存图片,extent为格式，"jpg"、"png"等
    public static void img2file(BufferedImage img,String extent,String newfile) {
        try {
            ImageIO.write(img, extent, new File(newfile));
        } catch (Exception e) {
            logger.error( e.toString() );
        }
    }

}
