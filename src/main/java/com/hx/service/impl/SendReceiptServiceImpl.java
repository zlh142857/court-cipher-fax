package com.hx.service.impl;

import com.hx.dao.InboxMapper;
import com.hx.dao.SendReceiptMapper;
import com.hx.modle.Inbox;
import com.hx.modle.Send_Receipt;
import com.hx.service.RecycleBinService;
import com.hx.service.SendReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.hx.change.ImgToPdf.imgToPdf;
import static com.hx.util.TiffToJPEG.readerTiff;

/**
 * @author 范聪敏
 * @date 2019/10/31 15:45
 * @desc
 */
@Service
public class SendReceiptServiceImpl implements SendReceiptService {
    @Resource
    private SendReceiptMapper sendReceiptMapper;

    @Autowired
    RecycleBinService recycleBinService;
    @Autowired
    private InboxMapper inboxMapper;
    @Override
    public int insert(Send_Receipt send_receipt) {
        return sendReceiptMapper.insert(send_receipt);
    }

    @Override
    public int queryTotalCount(Map<String, Object> searchMap) {
        return sendReceiptMapper.queryTotalCount(searchMap) ;
    }

    @Override
    public List<Send_Receipt> queryALLMail(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);

        return sendReceiptMapper.queryALLMail(searchMap);
    }

    @Override
    public List<Send_Receipt> queryALLMailList() {
        return sendReceiptMapper.queryALLMailList();
    }

    @Override
    public int deinbox(Integer readerId) {
        Send_Receipt send_receipt = sendReceiptMapper.getById(readerId);

        recycleBinService.insertRecycleBin("Send_Receipt", new Date(),
                send_receipt.getSendnumber(),
                String.valueOf(readerId),
                send_receipt.getReceivingunit(),
                send_receipt.getReceivenumber(),
                "",
                send_receipt.getCreate_time(),
                "",
                0,
                send_receipt.getSendline(),
                send_receipt.getMessage(),
                send_receipt.getIsLink(),

                send_receipt.getFilsavepath(),
                send_receipt.getBarCode());

        return sendReceiptMapper.deinbox(readerId);

    }

    @Override
    public List<Send_Receipt> getAll(String[] ids) {
        return sendReceiptMapper.getAll(ids);


    }
    //点击关联回执,然后获取收件箱的id,然后根据id,查询收件箱barCode和tifpath ,
    //根据barCode去发回执箱查找相同条形码的数据,更改状态为1已关联,并且tifPath=tifPath
    //根据收件箱的intBoxId,查询一次正文的路径,吧正文的路径赋值给发回执箱的tifPath
    @Override
    public boolean updateIsLink(String inBoxId) {
        boolean flag=true;
        Inbox inbox=inboxMapper.selectBarAndTifPath(Integer.valueOf( inBoxId ));
        if(null != inbox){
            sendReceiptMapper.updateIsLinkByBar(inbox.getBarCode(),inbox.getFilsavepath());
            inboxMapper.updateIsLink(Integer.valueOf( inBoxId ));
        }else{
            flag=false;
        }
        return flag;
    }
    //file转换成jpg,获取最新的路径,转换为PDF,然后转换成base64
    @Override
    public String checkText(String pdfPath,String tifPath) throws Exception {
        List<String> filePath=readerTiff(tifPath);
        imgToPdf(filePath,pdfPath);
        Thread.sleep( 300 );
        File file = new File(pdfPath);
        FileInputStream fileInputStream=null;
        byte[] buffer=null;
        if(file.exists()){
            fileInputStream=new FileInputStream(file);
            buffer = new byte[(int)file.length()];
            fileInputStream.read(buffer);
            fileInputStream.close();
        }
        return new BASE64Encoder().encode(buffer);
    }

    @Override
    public void modifysendReceipt(Map<String, Object> searchMap) {
        sendReceiptMapper.modifysendReceipt(searchMap);
    }


}
