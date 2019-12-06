package com.hx.service.impl;

import com.hx.dao.InboxMapper;
import com.hx.modle.Inbox;
import com.hx.service.InBoxService;
import com.hx.service.OutBoxService;
import com.hx.service.ReturnReceiptService;
import com.hx.service.SendReceiptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    ReturnReceiptService returnReceiptService;

    @Autowired
    SendReceiptService sendReceiptService;

    @Autowired
    OutBoxService outBoxService;

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
                outBoxService.reductionoutbox(id);
            } else if ( "3".equals(bs) ) {
                returnReceiptService.reductionReturnReceipt(id);
            } else if ( "4".equals(bs) ) {
                sendReceiptService.reductionSendReceipt(id);
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
                outBoxService.deleteoutbox(ids);
            } else if ( "3".equals(bs) ) {
                returnReceiptService.deleteReturnReceipt(ids);
            } else if ( "4".equals(bs) ) {
                sendReceiptService.deleteSendReceipt(ids);
            }
        }
        return true;
    }
}
