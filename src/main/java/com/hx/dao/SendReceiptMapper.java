package com.hx.dao;/*
 */

import com.hx.modle.Send_Receipt;
import org.apache.ibatis.annotations.Param;

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
}
