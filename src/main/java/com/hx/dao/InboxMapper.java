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

    void insertInbox(Inbox inbox);

    void updateIsReceiptById(int id);

    void updateIsLink(Integer id);

    void updateIsReceiptByBarCode(String barCode);

    Inbox selectBarAndTifPath(Integer id);

    List<Inbox> getAll(@Param("ids") String[] ids);

    int queryTotalCount(@Param("params") Map<String, Object> searchMap);

    List<Inbox> queryALLMail(@Param("params") Map<String, Object> searchMap);

    List<Inbox> queryALLMailList();

    Inbox getById(Integer id);

    List<Inbox> RecoveryInbox(@Param("params") Map<String, Object> searchMap);

    void reductioninbox(String id);

    void modifinbox(Integer id);

    void delinbox(String id);

    List<Inbox> readinboxALLMail(@Param("params")Map<String,Object> searchMap);

    int Signfornbox(int id);

    int queryTotalCountw(Map<String, Object> searchMap);



    int selectCount();

    List<Inbox> selectListByPage(@Param( "pageStart" ) int pageStart,@Param( "pageEnd" ) int pageEnd);

    int insertMuchInbox(List<Inbox> list);

    void updateMuchInbox(List<Inbox> list);

    List<Inbox> selectIdAndPathByPage(@Param( "pageStart" ) int pageStart,@Param( "pageEnd" ) int pageEnd);

    void deleteAll();


    List<Inbox> checkInbox(@Param("params") Map<String, Object> searchMap);

    int totalTotalCount(@Param("params") Map<String, Object> searchMap);
}
