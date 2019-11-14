package com.hx.service.impl;

import com.hx.dao.InboxMapper;
import com.hx.dao.SendReceiptMapper;
import com.hx.modle.Sch_Task;
import com.hx.modle.Send_Receipt;
import com.hx.service.RecycleBinService;
import com.hx.service.SendReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hx.util.ColorReverse.writeJpgOne;
import static com.hx.util.TiffToJPEG.readerTiffOne;

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
                send_receipt.getSendline(),send_receipt.getMessage());
        return sendReceiptMapper.deinbox(readerId);

    }

    @Override
    public Map<String, Object> selectReceiptNoLink(Integer pageNow, Integer pageSize) {
        Map<String,Object> map=new HashMap<>(  );
        int page=pageNow-1;
        List<Sch_Task> list=sendReceiptMapper.selectReceiptNoLink(page,pageSize);
        Long total=sendReceiptMapper.selectCountNoLink();
        map.put( "list",list);
        map.put( "total",total );
        return map;
    }

    @Override
    public boolean updateIsLink(String intBoxId, String receiptId) {
        inboxMapper.updateIsLink(Integer.valueOf( intBoxId ));
        //根据收件箱的intBoxId,查询一次正文的路径,吧正文的路径赋值给发回执箱的tifPath
        String tifPath=inboxMapper.selectTifPath(Integer.valueOf( intBoxId ));
        if(tifPath!=null || tifPath!=""){
            sendReceiptMapper.updateIsLink(Integer.valueOf( receiptId ),tifPath);
        }
        return true;
    }
    //file转换成jpg,然后获取第一页jpg,颜色转换之后,获取最新的路径,然后转换成base64
    @Override
    public String checkText(String tifPath) throws IOException {
        String filePath=readerTiffOne(tifPath);
        String newJpg=writeJpgOne(filePath);
        File file = new File(newJpg);
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


}
