package com.hx.service.impl;

import com.hx.dao.MailMapper;
import com.hx.modle.Mail;
import com.hx.modle.Mail_List;
import com.hx.service.MailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/20 23:26
 * @desc
 */
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private MailMapper mailMapper;


    @Override
    public List<Mail_List> queryALLMailList() {
        return mailMapper.queryALLMailList();
    }


    @Override

    public int deleteByPrimaryKey(Integer id) {

        return mailMapper.deleteUserById(id);
    }

    @Override
    public Mail_List getMailByLinknumber(String linknumber) {
        return mailMapper.getMailByLinknumber(linknumber);
    }

    @Override
    public void addMail(String typename, String linknumber, String typeid, String phone, String e_mail, String wor, String address,String telNotify) {
        Mail m = new Mail();
        m.setTypename(typename);
        m.setLinknumber(linknumber);
        m.setTypeid(typeid);
        m.setPhone(phone);
        m.setE_mail(e_mail);
        m.setWor(wor);
        m.setAddress(address);
        m.setTelNotify( telNotify );
        mailMapper.addMail(m);
    }

    @Override
    public Mail_List getMailBytypename(String typename) {
        return mailMapper. getMailBytypename(typename);
    }

    @Override
    public void AddMailList(String typename) {
        Mail_List ml = new Mail_List();
        ml.setTypename(typename);
        mailMapper.AddMailList(ml);
    }



    @Override
    public int delmaillist(Integer id) {
        mailMapper.deletemail(id);

        return mailMapper.delmaillist(id);
    }

    @Override
    public List<Mail_List> queryALLMail(Map<String, Object> searchMap) {

        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
//        int start = (pageNo - 1) * pageSize;
//        int offset = pageSize;
//        searchMap.put("start", start);
//        searchMap.put("offset", offset);
        return mailMapper.queryALLMail(searchMap);
    }

    @Override
    public void modifymail(Map<String, Object> searchMap) {
        mailMapper.modifymail(searchMap);
    }

    //    @Override
//    public int queryTotalCount(Map<String, Object> searchMap) {
//        return mailMapper.queryTotalCount(searchMap);
//    }
    @Override
    public List<Mail_List> sendViewMail() throws Exception{
        List<Mail_List> mailLists=mailMapper.selectMailList();
        int length=mailLists.size();
        for(int i=0;i<length;i++){
            List<Mail> mailList=mailMapper.selectMailById(mailLists.get( i ).getId());
            mailLists.get( i ).setChildren( mailList );
        }
        return mailLists;
    }
}
