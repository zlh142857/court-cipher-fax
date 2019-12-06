package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/27 16:21
 *@功能:
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata.Type;
import uk.co.mmscomputing.device.twain.jtwain;

import static com.hx.util.TempDir.fileTemp;

public class TwainExample implements ScannerListener {
    private static Logger logger=Logger.getLogger( TwainExample.class );
    public static TwainExample app;
    Scanner scanner = Scanner.getDevice();
    public static String tifPath="";
    public static List<String> list=new ArrayList<>(  );;
    public static int count=0;
    public TwainExample() throws ScannerIOException {
        this.scanner.addListener(this);
        jtwain.getSource().setShowUI(false);
        this.scanner.acquire();
    }
    public void update(Type var1, ScannerIOMetadata var2) {
        if (var1.equals(ScannerIOMetadata.ACQUIRED)) {
            BufferedImage var3 = var2.getImage();
            tifPath=fileTemp()+".tif";
            list.add( tifPath );
            try {
                ImageIO.write(var3, "tif", new File(tifPath));
            } catch (Exception e) {
                logger.error( e.toString() );
            }
        } else if (var1.equals(ScannerIOMetadata.NEGOTIATE)) {
            ScannerDevice var6 = var2.getDevice();
        } else if (var1.equals(ScannerIOMetadata.STATECHANGE)) {
            if (var2.isFinished()) {
                count=1;
            }
        } else if (var1.equals(ScannerIOMetadata.EXCEPTION)) {
            logger.error(var2.getException());
        }

    }
}
