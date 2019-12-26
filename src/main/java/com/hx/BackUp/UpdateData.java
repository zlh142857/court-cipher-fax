package com.hx.BackUp;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/24 15:46
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.hx.common.StaticFinal.BACKUPGETFAXFILE;
import static com.hx.common.StaticFinal.BACKUPSENDFAXFILE;
//更改数据库中的文件保存路径
public class UpdateData {
    private static InboxMapper inboxMapper;
    private static SendReceiptMapper sendReceiptMapper;
    private static OutboxMapper outboxMapper;
    private static ReturnReceiptMapper returnReceiptMapper;
    private static UpdateData updateData;
    @PostConstruct
    public void init() {
        updateData=this;
        updateData.inboxMapper=this.inboxMapper;
        updateData.sendReceiptMapper=this.sendReceiptMapper;
        updateData.outboxMapper=this.outboxMapper;
        updateData.returnReceiptMapper=this.returnReceiptMapper;
    }
    public  void setInboxMapper(InboxMapper inboxMapper) {
        this.inboxMapper = inboxMapper;
    }
    public  void setSendReceiptMapper(SendReceiptMapper sendReceiptMapper) {
        this.sendReceiptMapper = sendReceiptMapper;
    }
    public  void setOutboxMapper(OutboxMapper outboxMapper) {
        this.outboxMapper = outboxMapper;
    }
    public  void setReturnReceiptMapper(ReturnReceiptMapper returnReceiptMapper) {
        this.returnReceiptMapper = returnReceiptMapper;
    }
    //收件箱
    public static boolean updateDataFileSavePathInbox(String dateName){
        List<Inbox> list = new ArrayList<Inbox>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = updateData.inboxMapper.selectCount();
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        if(list_count<page_size) {
            page_size=list_count;
        }
        // 按次数更改数据
        for (int j = 0; j < export_times; j++) {
            //分页
            int pageStart = page_size * j;
            int pageEnd = page_size;
            list = updateData.inboxMapper.selectIdAndPathByPage( pageStart, pageEnd );
            for(Inbox inbox:list){
                String filePath=inbox.getFilsavepath();
                if(null == filePath || "".equals( filePath ) ){

                }else{
                    int length=filePath.length();
                    int sub=filePath.lastIndexOf( "/" );
                    String subPath=filePath.substring( sub,length );
                    String path=BACKUPGETFAXFILE+"/"+dateName+subPath;
                    inbox.setFilsavepath( path );
                }
            }
            updateData.inboxMapper.updateMuchInbox(list);
        }
        return true;
    }
    //发回执箱
    public static boolean updateDataFileSavePathSendReceipt(String dateName){
        List<Send_Receipt> list = new ArrayList<Send_Receipt>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = updateData.sendReceiptMapper.selectCount();
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        if(list_count<page_size) {
            page_size=list_count;
        }
        // 按次数更改数据
        for (int j = 0; j < export_times; j++) {
            //分页
            int pageStart = page_size * j;
            int pageEnd = page_size;
            list = updateData.sendReceiptMapper.selectIdAndPathByPage( pageStart, pageEnd );
            for(Send_Receipt sendReceipt:list){
                String filePath=sendReceipt.getFilsavepath();
                if(null == filePath || "".equals( filePath ) ){

                }else{
                    int length=filePath.length();
                    int sub=filePath.lastIndexOf( "/" );
                    String subPath=filePath.substring( sub,length );
                    String path=BACKUPSENDFAXFILE+"/"+dateName+subPath;
                    sendReceipt.setFilsavepath( path );
                }
            }
            updateData.sendReceiptMapper.updateMuchSendReceipt(list);
        }
        return true;
    }
    //发件箱
    public static boolean updateDataFileSavePathOutbox(String dateName){
        List<Outbox> list = new ArrayList<Outbox>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = updateData.outboxMapper.selectCount();
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        if(list_count<page_size) {
            page_size=list_count;
        }
        // 按次数更改数据
        for (int j = 0; j < export_times; j++) {
            //分页
            int pageStart = page_size * j;
            int pageEnd = page_size;
            list = updateData.outboxMapper.selectIdAndPathByPage( pageStart, pageEnd );
            for(Outbox outbox:list){
                String filePath=outbox.getFilsavepath();
                if(null == filePath || "".equals( filePath ) ){

                }else{
                    int length=filePath.length();
                    int sub=filePath.lastIndexOf( "/" );
                    String subPath=filePath.substring( sub,length );
                    String path=BACKUPSENDFAXFILE+"/"+dateName+subPath;
                    outbox.setFilsavepath( path );
                }
            }
            updateData.outboxMapper.updateMuchOutbox(list);
        }
        return true;
    }
    //收回执箱
    public static boolean updateDataFileSavePathReturnReceipt(String dateName){
        List<Return_Receipt> list = new ArrayList<Return_Receipt>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = updateData.returnReceiptMapper.selectCount();
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        if(list_count<page_size) {
            page_size=list_count;
        }
        // 按次数更改数据
        for (int j = 0; j < export_times; j++) {
            //分页
            int pageStart = page_size * j;
            int pageEnd = page_size;
            list = updateData.returnReceiptMapper.selectIdAndPathByPage( pageStart, pageEnd );
            for(Return_Receipt returnReceipt:list){
                String filePath=returnReceipt.getFilsavepath();
                if(null == filePath || "".equals( filePath ) ){

                }else{
                    int length=filePath.length();
                    int sub=filePath.lastIndexOf( "/" );
                    String subPath=filePath.substring( sub,length );
                    String path=BACKUPGETFAXFILE+"/"+dateName+subPath;
                    returnReceipt.setFilsavepath( path );
                }
            }
            updateData.returnReceiptMapper.updateMuchReturnReceipt(list);
        }
        return true;
    }
}
