package com.hx.dao;

import com.hx.modle.Inbox;
import com.hx.util.Query;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/3 10:29
 * @desc
 */
public interface InboxMapper {

    List<Inbox> getAll();

    int queryTotalCount(@Param("params") Map<String, Object> searchMap);

    List<Inbox> queryALLMail(@Param("params") Map<String, Object> searchMap);

    List<Inbox> queryALLMailList();

    int deinbox(Integer id);

    Inbox getById(Integer id);

    List<Inbox> queryALLMaillist(Query query);

    int queryinboxCount(Query query);

    int insert(@Param("inbox") Inbox inbox);

}
