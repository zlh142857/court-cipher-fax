package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/27 16:21
 *@功能:
 */

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata.Type;
import uk.co.mmscomputing.device.twain.jtwain;

public class TwainExample  implements ScannerListener {
    static TwainExample app;
    Scanner scanner = Scanner.getDevice();

    public TwainExample(String[] var1) throws ScannerIOException {
        this.scanner.addListener(this);
        jtwain.getSource().setShowUI(false);
        this.scanner.acquire();
    }

    public void update(Type var1, ScannerIOMetadata var2) {
        if (var1.equals(ScannerIOMetadata.ACQUIRED)) {
            BufferedImage var3 = var2.getImage();
            System.out.println("Have an image now!");

            try {
                ImageIO.write(var3, "tif", new File("D:\\tifToPdf\\mmsc_image.tif"));
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        } else if (var1.equals(ScannerIOMetadata.NEGOTIATE)) {
            ScannerDevice var6 = var2.getDevice();
        } else if (var1.equals(ScannerIOMetadata.STATECHANGE)) {
            System.err.println(var2.getStateStr());
            if (var2.isFinished()) {
                System.exit(0);
            }
        } else if (var1.equals(ScannerIOMetadata.EXCEPTION)) {
            var2.getException().printStackTrace();
        }

    }

    public static void main(String[] var0) {
        try {
            app = new TwainExample(var0);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

}
