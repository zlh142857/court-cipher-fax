package com.hx.service;

import com.hx.modle.Inbox;
import com.hx.util.Query;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/11 22:35
 * @desc
 */
public interface InBoxService {

    List<Inbox> getAll();

    int queryTotalCount(Map<String, Object> searchMap);

    List<Inbox> queryALLMail(Map<String, Object> searchMap, Integer pageNo, Integer pageSize);

    List<Inbox> queryALLMailList();

    int deinbox(Integer readerId);

    List<Inbox> queryALLMaillist(Query query);

    int queryinboxCount(Query query);

    boolean delinbox(String ids);

    int insert(Inbox inbox);



//    List<Inbox> queryALLMail(String mailListId, String beginDate, String endDate);

//    List<Inbox> queryALLMail(String mailListId,String);
}
