package com.hx.service.impl;

import com.hx.dao.InboxMapper;
import com.hx.dao.OutboxMapper;
import com.hx.dao.ReturnReceiptMapper;
import com.hx.dao.SendReceiptMapper;
import com.hx.modle.Inbox;
import com.hx.service.InBoxService;
import com.hx.service.OutBoxService;
import com.hx.service.ReturnReceiptService;
import com.hx.service.SendReceiptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    ReturnReceiptMapper returnReceiptMapper;

    @Autowired
    SendReceiptMapper sendReceiptMapper;

    @Autowired
    OutboxMapper outboxMapper;
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
    public List<Inbox> RecoveryInbox(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);
        return inboxMapper.RecoveryInbox(searchMap);
    }

    @Override
    public void modifinbox(Integer id) {
        inboxMapper.modifinbox(id);
    }

    @Override
    public boolean reductioninbox(String ids, String bs) {
        if ( StringUtils.isEmpty(ids) ) return false;
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            if ( "1".equals(bs) ) {
                inboxMapper.reductioninbox(id);
            } else if ( "2".equals(bs) ) {
                outboxMapper.reductionoutBox(id);
            } else if ( "3".equals(bs) ) {
                returnReceiptMapper.reductionReturnReceipt(id);
            } else if ( "4".equals(bs) ) {
                sendReceiptMapper.reductionSendReceipt(id);
            }
        }
        return true;
    }
    @Override
    public boolean delinbox(String ids, String bs) {
        if ( StringUtils.isEmpty(ids) ) return false;
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            if ( "1".equals(bs) ) {
                inboxMapper.delinbox(id);
            } else if ( "2".equals(bs) ) {
                outboxMapper.deleteoutbox(id);
            } else if ( "3".equals(bs) ) {
                returnReceiptMapper.deleteReturnReceipt(id);
            } else if ( "4".equals(bs) ) {
                sendReceiptMapper.deleteSendReceipt(id);
            }
        }
        return true;
    }
    @Override
    public List<Inbox> readinboxALLMail(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);
        return inboxMapper.readinboxALLMail(searchMap);
    }

    @Override
    public int Signfornbox(int id) {
        int count=inboxMapper.Signfornbox(id);
        return count;
    }

    @Override
    public int queryTotalCountw(Map<String, Object> searchMap) {
        return inboxMapper.queryTotalCountw(searchMap);
    }

    @Override
    public int totalTotalCount(Map<String, Object> searchMap) {
        return inboxMapper.totalTotalCount(searchMap);
    }


    @Override
    public List<Inbox> checkInbox(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);
        return inboxMapper.checkInbox( searchMap);
    }

}
