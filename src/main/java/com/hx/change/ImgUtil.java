package com.hx.change;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/12 11:14
 *@功能:
 */

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
//读取tif图片,并进行最合适的缩放
public class ImgUtil {
    public BufferedImage rotateImage(String imgFile) throws Exception{
        BufferedImage bufferedImage = null;
        try {
            if(imgFile != "" || imgFile != null){
                File _img_file_ = new File(imgFile);
                if(_img_file_.exists()){
                    Integer angel = 270;
                    BufferedImage src = ImageIO.read(_img_file_);
                    BigDecimal height = BigDecimal.valueOf(src.getHeight());
                    BigDecimal width = BigDecimal.valueOf(src.getWidth());
                    BigDecimal bd = height.divide(width,0,BigDecimal.ROUND_CEILING);
                    BigDecimal theValue = BigDecimal.valueOf(1.1700);

                    if (bd.compareTo(theValue) == -1 ) {
                        bufferedImage = Thumbnails.of(src).rotate(angel).scale(1).asBufferedImage();
                        bufferedImage = calcScale(bufferedImage);
                    }else {
                        bufferedImage = calcScale(src);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bufferedImage;
    }


    /**
     * @Author Ragty
     * @Description  计算最佳放缩比
     * @Date 9:19 2019/3/11
     * @return
     **/
    public BufferedImage calcScale(BufferedImage bufferedImage) throws IOException {
        BigDecimal standaraHeight = BigDecimal.valueOf(842);
        BigDecimal standaraWeight = BigDecimal.valueOf(595);
        BigDecimal height = BigDecimal.valueOf(bufferedImage.getHeight());
        BigDecimal width = BigDecimal.valueOf(bufferedImage.getWidth());

        if (height.compareTo(standaraHeight) == 1 || width.compareTo(standaraWeight) == 1) { //不符合标准，缩放
            BigDecimal scaleHeight = standaraHeight.divide(height,4,BigDecimal.ROUND_CEILING);
            BigDecimal scaleWidth = standaraWeight.divide(width,4,BigDecimal.ROUND_CEILING);
            if (scaleHeight.compareTo(BigDecimal.valueOf(1)) == -1) {
                bufferedImage = Thumbnails.of(bufferedImage).scale(scaleHeight.floatValue()).outputQuality(1.0f).asBufferedImage();
                calcScale(bufferedImage);
            }else if (scaleWidth.compareTo(BigDecimal.valueOf(1)) == -1) {
                bufferedImage = Thumbnails.of(bufferedImage).scale(scaleWidth.floatValue()).outputQuality(1.0f).asBufferedImage();
                calcScale(bufferedImage);
            }
        }
        return  bufferedImage;
    }
}
