package com.hx.dao;/*
 */

import com.hx.modle.Send_Receipt;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * 功能描述: 发回执箱
 *
 * 业务逻辑:
 *
 * @param:
 * @return:
 * @auther: 张立恒
 * @date: 2019/10/31 14:34
 */
public interface SendReceiptMapper {
    void insertNewMessage(Send_Receipt sendReceipt);

    int queryTotalCount(@Param("params") Map<String, Object> searchMap);

    List<Send_Receipt> queryALLMail(@Param("params") Map<String, Object> searchMap);

    List<Send_Receipt> queryALLMailList();

    Send_Receipt getById(Integer readerId);

    int deinbox(Integer readerId);

    int insert(@Param("send_receipt") Send_Receipt send_receipt);
}
