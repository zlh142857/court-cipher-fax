package com.hx.dao;/*
 */

import com.hx.modle.Inbox;
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

    void updateIsLinkByBar(@Param( "barCode" )String barCode,@Param( "filsavepath" )String tifPath);

    int queryTotalCount(@Param("params") Map<String, Object> searchMap);

    List<Send_Receipt> queryALLMail(@Param("params") Map<String, Object> searchMap);

    List<Send_Receipt> queryALLMailList();

    Send_Receipt getById(Integer id);

    List<Send_Receipt> getAll(@Param("ids") String[] ids);

    void modifysendReceipt(Map<String, Object> searchMap);

    void modifSendReceipt(int id);

    List<Send_Receipt> RecoveryoSendReceipt(@Param("params") Map<String, Object> searchMap);

    void reductionSendReceipt(String id);

    void deleteSendReceipt(String id);


    int selectCount();

    List<Send_Receipt> selectIdAndPathByPage(@Param( "pageStart" ) int pageStart,@Param( "pageEnd" ) int pageEnd);

    void updateMuchSendReceipt(List<Send_Receipt> list);

    List<Send_Receipt> selectListByPage(@Param( "pageStart" ) int pageStart,@Param( "pageEnd" ) int pageEnd);

    int insertMuchSendReceipt(List<Send_Receipt> list);

    void deleteAll();
}
