package com.hx.service.impl;

import com.hx.dao.SendReceiptMapper;
import com.hx.modle.Send_Receipt;
import com.hx.service.RecycleBinService;
import com.hx.service.SendReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

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


}
