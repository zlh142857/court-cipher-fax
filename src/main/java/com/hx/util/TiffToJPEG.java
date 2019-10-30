package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/17 10:22
 *@功能:
 */


import com.sun.media.jai.codec.*;
import com.sun.media.jai.codecimpl.TIFFImageDecoder;
import org.apache.log4j.Logger;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hx.common.StaticFinal.TEMPDIR;

public class TiffToJPEG {
    private static Logger logger=Logger.getLogger(TiffToJPEG.class);
    public static Map<String,Object> readerTiff(String tiffPath){
        Map<String,Object> map=new HashMap<>(  );
        List<File> list = new ArrayList<File>();
        List<String> pathList = new ArrayList<>();
        String filePre = TEMPDIR+"\\"+GetTimeToFileName.GetTimeToFileName();
        FileSeekableStream fss = null;
        RenderedOp op = null;
        String filePath="";
        try{
            fss = new FileSeekableStream(tiffPath);
            TIFFImageDecoder dec = new TIFFImageDecoder(fss,null);
            JPEGEncodeParam param = new JPEGEncodeParam();
            int page = dec.getNumPages();
            for(int i = 0; i < page; i++){
                RenderedImage render = dec.decodeAsRenderedImage(i);
                filePath=filePre+i+".jpg";
                File file = new File(filePath);
                ParameterBlock pb = new ParameterBlock();
                pb.addSource(render);
                pb.add(file.toString());
                pb.add("JPEG");
                pb.add(param);
                op = JAI.create("filestore", pb);
                list.add(file);
                pathList.add(filePath);
            }
        }catch(Exception e){
            logger.error( e.toString() );
        }finally{
            try {
                op.dispose();
                fss.close();
            } catch (IOException e) {
                logger.error( e.toString() );
            }
        }
        map.put("fileList",list);
        map.put("pathList",pathList);
        return map;
    }
}
