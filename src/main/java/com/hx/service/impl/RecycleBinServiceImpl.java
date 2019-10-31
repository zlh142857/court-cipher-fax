package com.hx.service.impl;

import com.hx.dao.RecycleBinMapper;
import com.hx.modle.Inbox;
import com.hx.modle.Outbox;
import com.hx.modle.RecycleBin;
import com.hx.service.InBoxService;
import com.hx.service.OutBoxService;
import com.hx.service.RecycleBinService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

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


    @Override
    public List<RecycleBin> queryList(Integer pageNo, Integer pageSize, String title) {

        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;

        return recycleBinMapper.queryList(start, offset, title);
    }

    @Override
    public int getTotal(String title) {
        return recycleBinMapper.getTotal(title);
    }



    @Override
    public boolean updateRestore(String ids) {
        if ( StringUtils.isEmpty(ids) )return false;

        String[] idArr = ids.split(",");
        for (String id : idArr) {
            RecycleBin recycleBin = recycleBinMapper.getById(id);
            //此处逻辑要求,Relate_type保存的就是表名,如果不是表名,需要根据relateType的值判断具体更新的表
            String relateType = recycleBin.getRelate_type();
            int rows = 0;
            if ( "inbox".equals(relateType) ) {
                Inbox inbox = new Inbox();
                inbox.setId(Integer.parseInt(recycleBin.getReceivingunit()));
                inbox.setSendernumber(recycleBin.getSendernumber());
                inbox.setSenderunit(recycleBin.getSenderunit());
                inbox.setReceivenumber(recycleBin.getReceivenumber());
                inbox.setFilsavepath(recycleBin.getFilsavepath());
                inbox.setCreate_time(recycleBin.getCreate_time());
                inbox.setReceiptpath(recycleBin.getReceiptpath());
                inbox.setIsreceipt(recycleBin.getIsreceipt());
                rows = inBoxService.insert(inbox);
            } else if ( "outbox".equals(relateType) ) {

                Outbox outbox = new Outbox();
                outbox.setId(Integer.parseInt(recycleBin.getSenderunit()));
                outbox.setSendernumber(recycleBin.getSendernumber());
                outbox.setReceivenumber(recycleBin.getReceivenumber());
                outbox.setReceivingunit(recycleBin.getReceivingunit());
                outbox.setCreate_time(recycleBin.getCreate_time());
                outbox.setSendline(recycleBin.getSendline());
                outbox.setMessage(recycleBin.getMessage());
                rows = outBoxService.insert(outbox);
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
        if ( StringUtils.isEmpty(ids) )return false;

        String[] idArr = ids.split(",");
        for (String id : idArr) {
            //删除回收站
            recycleBinMapper.deleteById(id);
        }

        return true;
    }


    @Override
    public int insertRecycleBin(String relateType, Date recoverytime, int sendernumber, String senderunit, String receivingunit, int receivenumber, String filsavepath,
                                Date create_time, String receiptpath, int isreceipt, String sendline,String message) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setRecoverytime(recoverytime);
        recycleBin.setSendernumber(sendernumber);
        recycleBin.setSenderunit(senderunit);
        recycleBin.setReceivingunit(receivingunit);
        recycleBin.setReceivenumber(receivenumber);
        recycleBin.setFilsavepath(filsavepath);
        recycleBin.setCreate_time(create_time);
        recycleBin.setReceiptpath(receiptpath);
        recycleBin.setIsreceipt(isreceipt);
        recycleBin.setRelate_type(relateType);
        recycleBin.setSendline(sendline);
        recycleBin.setSendline(message);

        return recycleBinMapper.insert(recycleBin);
    }




    }

