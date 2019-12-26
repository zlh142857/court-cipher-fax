package com.hx.dao;

import com.hx.modle.Mail;
import com.hx.modle.Mail_List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/3 11:17
 * @desc
 */
@Repository
public interface MailMapper {

    int queryTotalCount(@Param("params") Map<String, Object> searchMap);

    String selectCourtName(String callerId);

    List<Mail_List> selectMailList();

    List<Mail> selectMailById(int id);

    String selectNotifyPhone(String receiveNumber);

    List<Mail_List> queryALLMailList();

    List<Mail_List> queryALLMail(@Param("params") Map<String, Object> searchMap);

    int deleteUserById(int id);

    Mail_List getMailByLinknumber(String linknumber);

    Mail_List getMailBytypename(String typename);

    void AddMailList(Mail_List ml);

    void addMail(Mail m);

    int delmaillist(Integer id);

    int deletemail(Integer id);

    void modifymail(Map<String,Object> searchMap);
}
