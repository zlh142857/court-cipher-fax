package com.hx.service.impl;

import com.hx.dao.RecycleBinMapper;
import com.hx.modle.*;
import com.hx.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/18 17:29
 * @desc
 */
@Service
public class RecycleBinServiceImpl implements RecycleBinService {

    @Autowired
    private RecycleBinMapper recycleBinMapper;

    @Resource
    private InBoxService inBoxService;

    @Autowired
    private OutBoxService outBoxService;

    @Resource
    private ReturnReceiptService returnReceiptService;

    @Resource
    private SendReceiptService sendReceiptService;

    @Override
    public List<RecycleBin> queryList(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {

        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);

        return recycleBinMapper.queryList(searchMap);
    }

    @Override
    public int getTotal(Map<String, Object> searchMap) {
        return recycleBinMapper.getTotal(searchMap);
    }


    @Override
    public boolean updateRestore(String ids) {
        if ( StringUtils.isEmpty(ids) ) return false;

        String[] idArr = ids.split(",");
        for (String id : idArr) {
            RecycleBin recycleBin = recycleBinMapper.getById(id);
            //此处逻辑要求,Relate_type保存的就是表名,如果不是表名,需要根据relateType的值判断具体更新的表
            String relateType = recycleBin.getRelate_type();
            int rows = 0;
            if ( "inbox".equals(relateType) ) {
                Inbox inbox = new Inbox();
                //inbox.setId(Integer.parseInt(recycleBin.getReceivingunit()));
                inbox.setSendnumber(recycleBin.getSendnumber());
                inbox.setSenderunit(recycleBin.getSenderunit());
                inbox.setReceivenumber(recycleBin.getReceivenumber());
                inbox.setFilsavepath(recycleBin.getFilsavepath());
                inbox.setCreate_time(recycleBin.getCreate_time());
                inbox.setReceiptpath(recycleBin.getReceiptpath());
                inbox.setIsreceipt(recycleBin.getIsreceipt());
                inbox.setBarCode(recycleBin.getBarCode());
                inbox.setIsLink(recycleBin.getIsLink());

                rows = inBoxService.insert(inbox);
            } else if ( "outbox".equals(relateType) ) {

                Outbox outbox = new Outbox();
                //outbox.setId(Integer.parseInt(recycleBin.getSenderunit()));
                outbox.setSendnumber(recycleBin.getSendnumber());
                outbox.setReceivenumber(recycleBin.getReceivenumber());
                outbox.setReceivingunit(recycleBin.getReceivingunit());
                outbox.setCreate_time(recycleBin.getCreate_time());
                outbox.setSendline(recycleBin.getSendline());
                outbox.setMessage(recycleBin.getMessage());
                outbox.setFilsavepath(recycleBin.getFilsavepath());
                rows = outBoxService.insert(outbox);

            } else if ( "Return_Receipt".equals(relateType) ) {

                Return_Receipt return_receipt = new Return_Receipt();
                //return_receipt.setId(Integer.parseInt(recycleBin.getReceivingunit()));
                return_receipt.setReceivenumber(recycleBin.getReceivenumber());
                return_receipt.setSenderunit(recycleBin.getSenderunit());
                return_receipt.setSendnumber(recycleBin.getSendnumber());
                return_receipt.setCreate_time(recycleBin.getCreate_time());
                return_receipt.setFilsavepath(recycleBin.getReceiptpath());

                rows = returnReceiptService.insert(return_receipt);
            } else if ( "Send_Receipt".equals(relateType) ) {

                Send_Receipt send_receipt = new Send_Receipt();
                // send_receipt.setId(Integer.parseInt(recycleBin.getSenderunit()));
                send_receipt.setReceivingunit(recycleBin.getReceivingunit());
                send_receipt.setReceivenumber(recycleBin.getReceivenumber());
                send_receipt.setCreate_time(recycleBin.getCreate_time());
                send_receipt.setSendline(recycleBin.getSendline());
                send_receipt.setMessage(recycleBin.getMessage());
                send_receipt.setSendnumber(recycleBin.getSendnumber());
                send_receipt.setIsLink(recycleBin.getIsLink());
                send_receipt.setFilsavepath(recycleBin.getTifPath());
                send_receipt.setBarCode(recycleBin.getBarCode());
                rows = sendReceiptService.insert(send_receipt);
            } else {
                continue;
            }
            if ( rows > 0 ) {
                //删除回收站的记录
                recycleBinMapper.deleteById(id);
            }
        }

        return true;
    }


    @Override
    public boolean deleteById(String ids) {
        if ( StringUtils.isEmpty(ids) ) return false;

        String[] idArr = ids.split(",");
        for (String id : idArr) {
            //删除回收站
            recycleBinMapper.deleteById(id);
        }

        return true;
    }

    @Override
    public int insertRecycleBin(String relateType, Date recoverytime, String
            sendernumber, String senderunit, String receivingunit, String receivenumber,
                                String filsavepath, Date create_time, String receiptpath, int isreceipt,
                                String sendline, String message, int isLink, String barCode, String tifPath) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setRecoverytime(recoverytime);
        recycleBin.setSendnumber(sendernumber);
        recycleBin.setSenderunit(senderunit);
        recycleBin.setReceivingunit(receivingunit);
        recycleBin.setReceivenumber(receivenumber);
        recycleBin.setFilsavepath(filsavepath);
        recycleBin.setCreate_time(create_time);
        recycleBin.setReceiptpath(receiptpath);
        recycleBin.setIsreceipt(isreceipt);
        recycleBin.setRelate_type(relateType);
        recycleBin.setSendline(sendline);
        recycleBin.setMessage(message);
        recycleBin.setIsLink(isLink);
        recycleBin.setBarCode(barCode);
        recycleBin.setTifPath(tifPath);
        return recycleBinMapper.insert(recycleBin);
    }


}






