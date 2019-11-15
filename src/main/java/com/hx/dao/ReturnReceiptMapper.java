package com.hx.dao;

import com.hx.modle.Return_Receipt;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/16 17:20
 * @desc
 */
public interface ReturnReceiptMapper {
        int queryTotalCount(@Param("params") Map<String, Object> searchMap);

        List<Return_Receipt> queryALLMail(@Param("params") Map<String, Object> searchMap);

        List<Return_Receipt> queryALLMailList();

        Return_Receipt getById(int id);

        int deinbox(Integer readerId);

        int insert(@Param("return_receipt") Return_Receipt return_receipt);

        void insertReceipt(Return_Receipt returnReceipt);

}