package com.hx.service;

import com.hx.modle.Inbox;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/11 22:35
 * @desc
 */
public interface InBoxService {

    List<Inbox> getAll(String[] id);

    int queryTotalCount(Map<String, Object> searchMap);

    List<Inbox> queryALLMail(Map<String, Object> searchMap, Integer pageNo, Integer pageSize);

    List<Inbox> queryALLMailList();

    List<Inbox> RecoveryInbox(Map<String, Object> searchMap, Integer pageNo, Integer pageSize);

    void modifinbox(Integer id);

    boolean reductioninbox(String ids, String bs);

    boolean delinbox(String ids, String bs);

    List<Inbox> readinboxALLMail(Map<String,Object> searchMap, Integer pageNo, Integer pageSize);

    int Signfornbox(int id);

    int queryTotalCountw(Map<String, Object> searchMap);
}
