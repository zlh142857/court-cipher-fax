package com.hx.dao;

import com.hx.modle.Inbox;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/3 10:29
 * @desc
 */
public interface InboxMapper {
    List<Inbox> getAll(@Param("ids") String[] ids);

    int queryTotalCount(@Param("params") Map<String, Object> searchMap);

    List<Inbox> queryALLMail(@Param("params") Map<String, Object> searchMap);

    List<Inbox> queryALLMailList();

    int deinbox(Integer id);

    Inbox getById(Integer id);

    int insert(@Param("inbox") Inbox inbox);

    void insertInbox(Inbox inbox);

    void updateIsReceiptById(int id);

    void updateIsLink(Integer id);

    void updateIsReceiptByBarCode(String barCode);

    Inbox selectBarAndTifPath(Integer id);
}
