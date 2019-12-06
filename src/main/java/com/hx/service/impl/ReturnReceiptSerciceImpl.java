package com.hx.service.impl;

import com.hx.dao.ReturnReceiptMapper;
import com.hx.modle.Return_Receipt;
import com.hx.service.ReturnReceiptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/16 17:20
 * @desc
 */
@Service
public class ReturnReceiptSerciceImpl implements ReturnReceiptService {
    @Resource
    private ReturnReceiptMapper returnReceiptMapperr;

    @Override
    public int queryTotalCount(Map<String, Object> searchMap) {
        return returnReceiptMapperr.queryTotalCount(searchMap);
    }

    @Override
    public List<Return_Receipt> queryALLMail(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);
        return returnReceiptMapperr.queryALLMail(searchMap);
    }

    @Override
    public List<Return_Receipt> queryALLMailList() {
        return returnReceiptMapperr.queryALLMailList();
    }

    @Override
    public List<Return_Receipt> getAll(String[] ids) {
        return returnReceiptMapperr.getAll(ids);
    }

    @Override
    public void modifReturnReceipt(int id) {
        returnReceiptMapperr.modifReturnReceipt(id);
    }

    @Override
    public List<Return_Receipt> RecoveryReturnReceipt(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);
        return returnReceiptMapperr.RecoveryoReturnReceipt(searchMap);
    }

    @Override
    public void reductionReturnReceipt(String id) {
        returnReceiptMapperr.reductionReturnReceipt(id);
    }

    @Override
    public boolean deleteReturnReceipt(String ids) {
        if ( StringUtils.isEmpty(ids) ) return false;
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            //删除回收站
            returnReceiptMapperr.deleteReturnReceipt(id);

        }
        return true;
    }


}

