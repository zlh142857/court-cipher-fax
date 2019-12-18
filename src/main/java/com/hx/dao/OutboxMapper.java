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
    void insertNewMessage(Outbox outbox);

    List<Outbox> getAll(@Param("ids") String[] ids);

    int queryTotalCount(@Param("params") Map<String, Object> searchMap);

    List<Outbox> queryALLMailList(@Param("params") Map<String, Object> searchMap);

    Outbox getById(Integer id);

    void modifyoutBox(Map<String, Object> searchMap);

    List<Outbox> RecoveryoutBox(@Param("params") Map<String, Object> searchMap);

    void reductionoutBox(String id);

    void deleteoutbox(String id);

    void modifoutBox(int id);

    List<String> selectFilePath(int fileDays);

    void updateTelNotifyResultById(@Param( "id" ) Integer id,@Param( "message" ) String message);
}
