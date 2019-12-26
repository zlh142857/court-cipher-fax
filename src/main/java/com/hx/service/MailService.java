package com.hx.service;

import com.hx.modle.Mail_List;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/20 23:26
 * @desc
 */

public interface MailService {

    List<Mail_List> sendViewMail() throws Exception;

    List<Mail_List> queryALLMailList();

    int deleteByPrimaryKey(Integer readerId);

    Mail_List getMailByLinknumber(String linknumber);

    void addMail(String typename, String linknumber, String typeid, String phone, String e_mail, String wor, String address,String telNotify);

    Mail_List getMailBytypename(String typename);

    void AddMailList(String typename);

    int delmaillist(Integer readerId);

    List<Mail_List> queryALLMail(Map<String, Object> searchMap);

    void modifymail(Map<String,Object> searchMap);
}
