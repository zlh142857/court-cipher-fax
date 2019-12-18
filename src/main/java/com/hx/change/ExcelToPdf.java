package com.hx.change;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/17 14:13
 *@功能:
 */

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.hx.common.StaticFinal.EXCELlICENSE;
import static com.hx.common.StaticFinal.TEMPDIR;
import static com.hx.util.TempDir.fileTemp;

public class ExcelToPdf {
    private static Logger logger=Logger.getLogger( ExcelToPdf.class );
    public static String excelToPdf(MultipartFile file) throws IOException {
        String docName=file.getOriginalFilename();
        String docPath=TEMPDIR+"/"+docName;//Word保存路径,Document加载此路径
        File dest = new File(docPath);
        file.transferTo(dest); // 保存文件
        String pdfPath=fileTemp()+".pdf";
        // 验证License
        if (!getLicense()) {
            return null;
        }
        try {
            Workbook wb = new Workbook(docPath);// 原始excel路径
            File pdfFile = new File(pdfPath);// 输出路径
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            wb.save(fileOS, SaveFormat.PDF);
        } catch (Exception e) {
            pdfPath=null;
            logger.error( e.toString() );
        }
        return pdfPath;
    }
    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = ExcelToPdf.class.getClassLoader().getResourceAsStream(EXCELlICENSE);
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            logger.error( e.toString() );
        }
        return result;
    }

}
