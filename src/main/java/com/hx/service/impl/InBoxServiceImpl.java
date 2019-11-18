package com.hx.service.impl;

import com.hx.dao.InboxMapper;
import com.hx.modle.Inbox;
import com.hx.service.InBoxService;
import com.hx.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/11 22:36
 * @desc
 */
@Service
public class InBoxServiceImpl implements InBoxService {

    @Resource
    private InboxMapper inboxMapper;

    @Autowired
    RecycleBinService recycleBinService;


    @Override
    public List<Inbox> getAll(String[] ids) {
        return inboxMapper.getAll(ids);
    }

    @Override
    public int queryTotalCount(Map<String, Object> searchMap) {
        return inboxMapper.queryTotalCount(searchMap);
    }

    @Override
    public List<Inbox> queryALLMail(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);

        return inboxMapper.queryALLMail(searchMap);
    }

    @Override
    public List<Inbox> queryALLMailList() {
        return inboxMapper.queryALLMailList();
    }

    @Override
    public int deinbox(Integer readerId) {
        Inbox inbox = inboxMapper.getById(readerId);
        recycleBinService.insertRecycleBin("inbox", new Date(),
                inbox.getSendnumber(),
                inbox.getSenderunit(),
                String.valueOf(readerId),
                inbox.getReceivenumber(),
                inbox.getFilsavepath(),
                (Date) inbox.getCreate_time(),
                inbox.getReceiptpath(),
                inbox.getIsreceipt(),
                "","",
                inbox.getIsLink(),
                inbox.getBarCode(),
                "");
        return inboxMapper.deinbox(readerId);
    }

    @Override
    public boolean delinbox(String ids) {
        return false;
    }

    @Override
    public int insert(Inbox inbox) {
        return inboxMapper.insert(inbox);
    }
}
