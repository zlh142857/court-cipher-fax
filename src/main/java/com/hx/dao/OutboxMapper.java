package com.hx.dao;

import com.hx.modle.Outbox;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/3 10:55
 * @desc
 */
public interface OutboxMapper {
    List<Outbox> getAll(@Param("ids") String[] ids);

    int queryTotalCount(@Param("params") Map<String, Object> searchMap);

    List<Outbox> queryALLMailList(@Param("params") Map<String, Object> searchMap);

    int deloutbox(Integer id);

    Outbox getById(Integer id);

    int insert(@Param("outbox") Outbox outbox);

    void insertNewMessage(Outbox outbox);
}
