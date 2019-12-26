package com.hx.BackUp;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/23 17:08
 *@功能:
 */

import com.hx.dao.InboxMapper;
import com.hx.dao.OutboxMapper;
import com.hx.dao.ReturnReceiptMapper;
import com.hx.dao.SendReceiptMapper;
import com.hx.modle.Inbox;
import com.hx.modle.Outbox;
import com.hx.modle.Return_Receipt;
import com.hx.modle.Send_Receipt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
//备份 数据从数据库导出到Excel
public class BackUpExcel {
    private static InboxMapper inboxMapper;
    private static OutboxMapper outboxMapper;
    private static SendReceiptMapper sendReceiptMapper;
    private static ReturnReceiptMapper returnReceiptMapper;
    private static BackUpExcel backUpExcel;
    @PostConstruct
    public void init() {
        backUpExcel=this;
        backUpExcel.inboxMapper=this.inboxMapper;
        backUpExcel.outboxMapper=this.outboxMapper;
        backUpExcel.sendReceiptMapper=this.sendReceiptMapper;
        backUpExcel.returnReceiptMapper=this.returnReceiptMapper;
    }
    public  void setInboxMapper(InboxMapper inboxMapper) {
        this.inboxMapper = inboxMapper;
    }
    public  void setOutboxMapper(OutboxMapper outboxMapper) {
        this.outboxMapper = outboxMapper;
    }
    public  void setSendReceiptMapper(SendReceiptMapper sendReceiptMapper) {
        this.sendReceiptMapper = sendReceiptMapper;
    }
    public  void setReturnReceiptMapper(ReturnReceiptMapper returnReceiptMapper) {
        this.returnReceiptMapper = returnReceiptMapper;
    }
    //收件箱
    public static void writeExcelInbox(String outPath)
            throws IOException {
        // 最重要的就是使用SXSSFWorkbook，表示流的方式进行操作
        // 在内存中保持100行，超过100行将被刷新到磁盘
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet(); // 建立新的sheet对象
        Row row = sh.createRow(0);   // 创建第一行对象
        // -----------定义表头-----------
        Cell cel0 = row.createCell(0);
        cel0.setCellValue("ID");
        Cell cel1 = row.createCell(1);
        cel1.setCellValue("发送方号码");
        Cell cel2 = row.createCell(2);
        cel2.setCellValue("发送方单位");
        Cell cel3 = row.createCell(3);
        cel3.setCellValue("接收号码");
        Cell cel4 = row.createCell(4);
        cel4.setCellValue("传真文件保存路径");
        Cell cel5 = row.createCell(5);
        cel5.setCellValue("接收时间");
        Cell cel7 = row.createCell(6);
        cel7.setCellValue("是否已回执");
        Cell cel8 = row.createCell(7);
        cel8.setCellValue("条形码");
        Cell cel9 = row.createCell(8);
        cel9.setCellValue("是否已关联");
        Cell cel11 = row.createCell(9);
        cel11.setCellValue("是否已删除");
        Cell cel12 = row.createCell(10);
        cel12.setCellValue("是否已签收");
        Cell cel13 = row.createCell(11);
        cel13.setCellValue("页码");
        // ---------------------------
        List<Inbox> list = new ArrayList<Inbox>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = backUpExcel.inboxMapper.selectCount();
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        if(list_count<page_size) {
            page_size=list_count;
        }
        // 按次数将数据写入文件
        for (int j = 0; j < export_times; j++) {
            //分页
            int pageStart=page_size*j;
            int pageEnd=page_size;
            list=backUpExcel.inboxMapper.selectListByPage(pageStart,pageEnd);

            int len = list.size() < page_size ? list.size() : page_size;
            for (int i = 0; i < len; i++) {
                Row row_value = sh.createRow(j * page_size + i + 1);
                Cell cel0_value = row_value.createCell(0);
                cel0_value.setCellValue(new XSSFRichTextString(list.get(i).getId().toString()));
                Cell cel1_value = row_value.createCell(1);
                cel1_value.setCellValue(new XSSFRichTextString(list.get(i).getSendnumber()));
                Cell cel2_value = row_value.createCell(2);
                cel2_value.setCellValue(new XSSFRichTextString(list.get(i).getSenderunit()));
                Cell cel3_value = row_value.createCell(3);
                cel3_value.setCellValue(new XSSFRichTextString(list.get(i).getReceivenumber()));
                Cell cel4_value = row_value.createCell(4);
                cel4_value.setCellValue(new XSSFRichTextString(list.get(i).getFilsavepath()));
                Cell cel5_value = row_value.createCell(5);
                if(null ==list.get(i).getCreate_time()){
                    cel5_value.setCellValue("");
                }else{
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cel5_value.setCellValue(new XSSFRichTextString(sdf.format( list.get(i).getCreate_time() )));
                }
                Cell cel7_value = row_value.createCell(6);
                if(null == list.get(i).getIsreceipt()){
                    cel7_value.setCellValue("");
                }else{
                    cel7_value.setCellValue(new XSSFRichTextString(list.get(i).getIsreceipt().toString()));
                }

                Cell cel8_value = row_value.createCell(7);
                cel8_value.setCellValue(new XSSFRichTextString(list.get(i).getBarCode()));
                Cell cel9_value = row_value.createCell(8);
                if(null ==list.get(i).getIsLink()){
                    cel9_value.setCellValue("");
                }else {
                    cel9_value.setCellValue(new XSSFRichTextString(list.get(i).getIsLink().toString()));
                }
                Cell cel11_value = row_value.createCell(9);
                if(null ==list.get(i).getState()){
                    cel11_value.setCellValue("");
                }else {
                    cel11_value.setCellValue(new XSSFRichTextString(list.get(i).getState().toString()));
                }
                Cell cel12_value = row_value.createCell(10);
                if(null ==list.get(i).getIsSign()){
                    cel12_value.setCellValue("");
                }else {
                    cel12_value.setCellValue(new XSSFRichTextString(list.get(i).getIsSign().toString()));
                }
                Cell cel13_value = row_value.createCell(11);
                if(null==list.get(i).getPageNum()){
                    cel13_value.setCellValue("");
                }else{
                    cel13_value.setCellValue(new XSSFRichTextString( list.get(i).getPageNum().toString() ));
                }
            }
            list.clear(); // 每次存储len行，用完了将内容清空，以便内存可重复利用
        }
        File file=new File( outPath );
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        wb.dispose();
    }
    //发件箱
    public static void writeExcelOutbox(String outPath)
            throws IOException {
        // 最重要的就是使用SXSSFWorkbook，表示流的方式进行操作
        // 在内存中保持100行，超过100行将被刷新到磁盘
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet(); // 建立新的sheet对象
        Row row = sh.createRow(0);   // 创建第一行对象
        // -----------定义表头-----------
        Cell cel0 = row.createCell(0);
        cel0.setCellValue("ID");
        Cell cel1 = row.createCell(1);
        cel1.setCellValue("发送方号码");
        Cell cel2 = row.createCell(2);
        cel2.setCellValue("接收号码");
        Cell cel3 = row.createCell(3);
        cel3.setCellValue("接收单位");
        Cell cel4 = row.createCell(4);
        cel4.setCellValue("发送时间");
        Cell cel5 = row.createCell(5);
        cel5.setCellValue("文件标题");
        Cell cel7 = row.createCell(6);
        cel7.setCellValue("发送结果");
        Cell cel8 = row.createCell(7);
        cel8.setCellValue("文件保存路径");
        Cell cel9 = row.createCell(8);
        cel9.setCellValue("是否已删除");
        Cell cel11 = row.createCell(9);
        cel11.setCellValue("是否发送成功标识");
        Cell cel12 = row.createCell(10);
        cel12.setCellValue("电话通话结果");
        Cell cel13 = row.createCell(11);
        cel13.setCellValue("页码");
        // ---------------------------
        List<Outbox> list = new ArrayList<Outbox>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = backUpExcel.outboxMapper.selectCount();
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        if(list_count<page_size) {
            page_size=list_count;
        }
        // 按次数将数据写入文件
        for (int j = 0; j < export_times; j++) {
            //分页
            int pageStart=page_size*j;
            int pageEnd=page_size;
            list=backUpExcel.outboxMapper.selectListByPage(pageStart,pageEnd);

            int len = list.size() < page_size ? list.size() : page_size;
            for (int i = 0; i < len; i++) {
                Row row_value = sh.createRow(j * page_size + i + 1);
                Cell cel0_value = row_value.createCell(0);
                cel0_value.setCellValue(new XSSFRichTextString(list.get(i).getId().toString()));
                Cell cel1_value = row_value.createCell(1);
                cel1_value.setCellValue(new XSSFRichTextString(list.get(i).getSendnumber()));
                Cell cel2_value = row_value.createCell(2);
                cel2_value.setCellValue(new XSSFRichTextString(list.get(i).getReceivenumber()));
                Cell cel3_value = row_value.createCell(3);
                cel3_value.setCellValue(new XSSFRichTextString(list.get(i).getReceivingunit()));
                Cell cel4_value = row_value.createCell(4);
                if(null ==list.get(i).getCreate_time()){
                    cel4_value.setCellValue("");
                }else{
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cel4_value.setCellValue(new XSSFRichTextString(sdf.format( list.get(i).getCreate_time() )));
                }
                Cell cel5_value = row_value.createCell(5);
                cel5_value.setCellValue(new XSSFRichTextString(list.get(i).getSendline()));
                Cell cel6_value = row_value.createCell(6);
                cel6_value.setCellValue(new XSSFRichTextString(list.get(i).getMessage()));
                Cell cel7_value = row_value.createCell(7);
                cel7_value.setCellValue(new XSSFRichTextString(list.get(i).getFilsavepath()));

                Cell cel8_value = row_value.createCell(8);
                if(null ==list.get(i).getState()){
                    cel8_value.setCellValue("");
                }else {
                    cel8_value.setCellValue(new XSSFRichTextString(list.get(i).getState().toString()));
                }
                Cell cel9_value = row_value.createCell(9);
                boolean bool=list.get(i).getIsSuccess();
                if(bool){
                    cel9_value.setCellValue(new XSSFRichTextString("true"));
                }else{
                    cel9_value.setCellValue(new XSSFRichTextString("false"));
                }
                Cell cel10_value = row_value.createCell(10);
                cel10_value.setCellValue(new XSSFRichTextString(list.get(i).getTelResult()));
                Cell cel11_value = row_value.createCell(11);
                if(null==list.get(i).getPageNum()){
                    cel11_value.setCellValue("");
                }else{
                    cel11_value.setCellValue(new XSSFRichTextString( list.get(i).getPageNum().toString() ));
                }
            }
            list.clear(); // 每次存储len行，用完了将内容清空，以便内存可重复利用
        }
        File file=new File( outPath );
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        wb.dispose();
    }
    //发回执箱
    public static void writeExcelSendReceipt(String outPath)
            throws IOException {
        // 最重要的就是使用SXSSFWorkbook，表示流的方式进行操作
        // 在内存中保持100行，超过100行将被刷新到磁盘
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet(); // 建立新的sheet对象
        Row row = sh.createRow(0);   // 创建第一行对象
        // -----------定义表头-----------
        Cell cel0 = row.createCell(0);
        cel0.setCellValue("ID");
        Cell cel1 = row.createCell(1);
        cel1.setCellValue("接收单位");
        Cell cel2 = row.createCell(2);
        cel2.setCellValue("接收号码");
        Cell cel3 = row.createCell(3);
        cel3.setCellValue("发送时间");
        Cell cel4 = row.createCell(4);
        cel4.setCellValue("文件标题");
        Cell cel5 = row.createCell(5);
        cel5.setCellValue("发送结果");
        Cell cel7 = row.createCell(6);
        cel7.setCellValue("发送号码");
        Cell cel8 = row.createCell(7);
        cel8.setCellValue("是否已关联");
        Cell cel9 = row.createCell(8);
        cel9.setCellValue("文件保存路径");
        Cell cel11 = row.createCell(9);
        cel11.setCellValue("条形码");
        Cell cel12 = row.createCell(10);
        cel12.setCellValue("是否已删除");
        // ---------------------------
        List<Send_Receipt> list = new ArrayList<Send_Receipt>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = backUpExcel.outboxMapper.selectCount();
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        if(list_count<page_size) {
            page_size=list_count;
        }
        // 按次数将数据写入文件
        for (int j = 0; j < export_times; j++) {
            //分页
            int pageStart=page_size*j;
            int pageEnd=page_size;
            list=backUpExcel.sendReceiptMapper.selectListByPage(pageStart,pageEnd);

            int len = list.size() < page_size ? list.size() : page_size;
            for (int i = 0; i < len; i++) {
                Row row_value = sh.createRow(j * page_size + i + 1);
                Cell cel0_value = row_value.createCell(0);
                cel0_value.setCellValue(new XSSFRichTextString(list.get(i).getId().toString()));
                Cell cel1_value = row_value.createCell(1);
                cel1_value.setCellValue(new XSSFRichTextString(list.get(i).getReceivingunit()));
                Cell cel2_value = row_value.createCell(2);
                cel2_value.setCellValue(new XSSFRichTextString(list.get(i).getReceivenumber()));
                Cell cel3_value = row_value.createCell(3);
                if(null ==list.get(i).getCreate_time()){
                    cel3_value.setCellValue("");
                }else{
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cel3_value.setCellValue(new XSSFRichTextString(sdf.format( list.get(i).getCreate_time() )));
                }
                Cell cel4_value = row_value.createCell(4);
                cel4_value.setCellValue(new XSSFRichTextString(list.get(i).getSendline()));
                Cell cel5_value = row_value.createCell(5);
                cel5_value.setCellValue(new XSSFRichTextString(list.get(i).getMessage()));
                Cell cel6_value = row_value.createCell(6);
                cel6_value.setCellValue(new XSSFRichTextString(list.get(i).getSendnumber()));
                Cell cel7_value = row_value.createCell(7);
                if(null ==list.get(i).getIsLink()){
                    cel7_value.setCellValue("");
                }else{
                    cel7_value.setCellValue(new XSSFRichTextString(list.get(i).getIsLink().toString()));
                }
                Cell cel8_value = row_value.createCell(8);
                cel8_value.setCellValue(new XSSFRichTextString(list.get(i).getFilsavepath()));
                Cell cel9_value = row_value.createCell(8);
                cel9_value.setCellValue(new XSSFRichTextString(list.get(i).getBarCode()));

                Cell cel10_value = row_value.createCell(9);
                if(null ==list.get(i).getState()){
                    cel10_value.setCellValue("");
                }else{
                    cel10_value.setCellValue(new XSSFRichTextString(list.get(i).getState().toString()));
                }
            }
            list.clear(); // 每次存储len行，用完了将内容清空，以便内存可重复利用
        }
        File file=new File( outPath );
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        wb.dispose();
    }
    //收回执箱
    public static void writeExcelReturnReceipt(String outPath)
            throws IOException {
        // 最重要的就是使用SXSSFWorkbook，表示流的方式进行操作
        // 在内存中保持100行，超过100行将被刷新到磁盘
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet(); // 建立新的sheet对象
        Row row = sh.createRow(0);   // 创建第一行对象
        // -----------定义表头-----------
        Cell cel0 = row.createCell(0);
        cel0.setCellValue("ID");
        Cell cel1 = row.createCell(1);
        cel1.setCellValue("发送方单位");
        Cell cel2 = row.createCell(2);
        cel2.setCellValue("接收号码");
        Cell cel3 = row.createCell(3);
        cel3.setCellValue("发送号码");
        Cell cel4 = row.createCell(4);
        cel4.setCellValue("接收时间");
        Cell cel5 = row.createCell(5);
        cel5.setCellValue("文件保存路径");
        Cell cel7 = row.createCell(6);
        cel7.setCellValue("是否已删除");
        // ---------------------------
        List<Return_Receipt> list = new ArrayList<Return_Receipt>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = backUpExcel.returnReceiptMapper.selectCount();
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        if(list_count<page_size) {
            page_size=list_count;
        }
        // 按次数将数据写入文件
        for (int j = 0; j < export_times; j++) {
            //分页
            int pageStart=page_size*j;
            int pageEnd=page_size;
            list=backUpExcel.returnReceiptMapper.selectListByPage(pageStart,pageEnd);

            int len = list.size() < page_size ? list.size() : page_size;
            for (int i = 0; i < len; i++) {
                Row row_value = sh.createRow(j * page_size + i + 1);
                Cell cel0_value = row_value.createCell(0);
                cel0_value.setCellValue(new XSSFRichTextString(list.get(i).getId().toString()));
                Cell cel1_value = row_value.createCell(1);
                cel1_value.setCellValue(new XSSFRichTextString(list.get(i).getSenderunit()));
                Cell cel2_value = row_value.createCell(2);
                cel2_value.setCellValue(new XSSFRichTextString(list.get(i).getReceivenumber()));
                Cell cel3_value = row_value.createCell(3);
                cel3_value.setCellValue(new XSSFRichTextString(list.get(i).getSendnumber()));
                Cell cel4_value = row_value.createCell(4);
                if(null ==list.get(i).getCreate_time()){
                    cel4_value.setCellValue("");
                }else{
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cel4_value.setCellValue(new XSSFRichTextString(sdf.format( list.get(i).getCreate_time() )));
                }
                Cell cel6_value = row_value.createCell(5);
                cel6_value.setCellValue(new XSSFRichTextString(list.get(i).getFilsavepath()));
                Cell cel7_value = row_value.createCell(6);
                if(null ==list.get(i).getState()){
                    cel7_value.setCellValue("");
                }else{
                    cel7_value.setCellValue(new XSSFRichTextString(list.get(i).getState().toString()));
                }

            }
            list.clear(); // 每次存储len行，用完了将内容清空，以便内存可重复利用
        }
        File file=new File( outPath );
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        wb.dispose();
    }
}
