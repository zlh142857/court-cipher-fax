package com.hx.controller;

import com.hx.modle.Mail_List;
import com.hx.service.MailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/20 23:23
 * @desc
 */
@Controller
@RequestMapping("mail")
public class MailController {

    @Autowired
    private MailService mailService;
    private static Logger log = Logger.getLogger(MailController.class);// 日志文件
    /**
     * 查询所有通讯录列表
     */
    @RequestMapping(value = "/mailList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> mailLists() {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 查询全部通讯录列表
        List<Mail_List> mailLists = mailService.queryALLMailList();
        result.put("mailLists", mailLists); //
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }
    /**
     * 查询通讯录下的联系人列表，参数通讯录ID，根据通讯录ID查询联系人列表
     */
    @RequestMapping(value = "/mail", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> mails( String mailListId, String linkname, String linknumber, String typeid) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        Map<String,Object> searchMap=new HashMap();
        searchMap.put("linkname", linkname);
        searchMap.put("linknumber", linknumber);
        searchMap.put("typeid", typeid);
        searchMap.put("mailListId", mailListId);
        //根据通讯录ID查询联系人列表
        List<Mail_List> mails = mailService.queryALLMail(searchMap);
        result.put("mails", mails);;
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }
    @RequestMapping(value = "/addmail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addmail(@RequestParam("linkname") String linkname,
                                       @RequestParam("linknumber") String linknumber, ModelMap model,
                                       @RequestParam("typeid") String typeid) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //联系人不能为空
        if ( null == linkname || "".equals(linkname) ) {
            result.put("msg", "联系人不能为空");
            log.error("联系人不能为空"+linkname);
            return result;
        }
        if ( null == linknumber || "".equals(linknumber) ) {
            result.put("msg", "联系人手机号不能为空");
            log.error("联系人手机号不能为空"+linknumber);
            return result;
        }
        if ( null == typeid || "".equals(typeid) ) {
            result.put("msg", "联系人类型不能为空");
            log.error("联系人类型不能为空"+typeid);
            return result;
        }


        //根据用户名查询用户，如果查出来的用户已经存在，那就是数据库中已经有一个账户是这个名字，账户名冲突，否则不冲突可以新增用户
        Mail_List u = mailService.getMailByLinknumber(linknumber);
        if ( u != null ) {
            result.put("msg", "该号码已存在");
            log.error("该号码已存在"+u);
            return result;
        }

        //TODO 保存联系人 电话入数据库
        mailService.addMail(linkname, linknumber, typeid);
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }
    //TODO 批量删除联系人""
    @RequestMapping(value = "/dle", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> deleteByPrimaryKey(String str) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( null == str || "".equals(str.trim()) ) {
            result.put("msg", "参数错误");
            log.error("参数为空"+str);
            return result;
        }
        String[] split = str.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                mailService.deleteByPrimaryKey(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            log.error("参数异常");
            e.printStackTrace();
            result.put("msg", e.getMessage());
        }
        return result;
    }
    //TODO 新建通讯薄
    @RequestMapping(value = "/addmaillist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addmaillist(@RequestParam("typename") String typename) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //通讯薄名不能为空
        if ( null == typename || "".equals(typename) ) {
            result.put("msg", "通讯薄不能为空");
            log.error("通讯薄不能为空"+typename);
            return result;
        }
        Mail_List u = mailService.getMailBytypename(typename);
        if ( u != null ) {
            result.put("msg", "通讯薄已存在");
            log.error("通讯薄已存在"+u);
            return result;
        }
        //TODO 保存通讯薄入数据库
        mailService.AddMailList(typename);
        result.put("state",1); //0代表失败，1代表成功
        return result;
    }
    //TODO 删除通讯薄
    @RequestMapping(value = "/dlemaillist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delmaillist(String str) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( null == str || "".equals(str.trim()) ) {
            result.put("msg", "参数错误");
            log.error("参数错误"+str);
            return result;
        }
        String[] split = str.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                mailService.delmaillist(Integer.parseInt(split[i]));
            }
            result.put("state", 1); //0代表失败，1代表成功
        } catch (Exception e) {
            log.error("参数错误"+split);
            result.put("msg", e.getMessage());
        }
        return result;
    }

}

