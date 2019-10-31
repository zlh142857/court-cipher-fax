package com.hx.service;

import com.hx.modle.Outbox;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/12 10:44
 * @desc
 */
public interface OutBoxService {
     List<Outbox> getAll();

    int queryTotalCount(Map<String, Object> searchMap);

    List<Outbox> queryALLMailList(Map<String, Object> searchMap, Integer pageNo, Integer pageSize);

    int deloutbox(Integer readerId);

    int insert(Outbox outbox);

}
