package com.hx.service.impl;

import com.hx.dao.ReturnReceiptMapper;
import com.hx.modle.Return_Receipt;
import com.hx.service.RecycleBinService;
import com.hx.service.ReturnReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    @Autowired
    RecycleBinService recycleBinService;
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
    public int deinbox(Integer readerId) {
        Return_Receipt return_receipt = returnReceiptMapperr.getById(readerId);
        if ( null == return_receipt  ) {
            return 1;
        }
        recycleBinService.insertRecycleBin("Return_Receipt",
                new Date(),
                return_receipt.getSendnumber(),
                return_receipt.getSenderunit(),
                String.valueOf(readerId),
                return_receipt.getReceivenumber(),
                "",
                return_receipt.getCreate_time(),
                return_receipt.getFilsavepath(),
                0, "","", 0,
                "","");
        return returnReceiptMapperr.deinbox(readerId);
    }

    @Override
    public List<Return_Receipt> getAll(String[] ids) {
        return returnReceiptMapperr.getAll(ids);
    }

    @Override
    public int insert(Return_Receipt return_receipt) {
        return returnReceiptMapperr.insert(return_receipt);
    }


}

