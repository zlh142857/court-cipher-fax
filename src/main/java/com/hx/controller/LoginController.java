package com.hx.controller;

import com.hx.modle.Login;
import com.hx.service.LoginService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/20 10:21
 * @desc
 */
@Controller
@RequestMapping("/signin")
public class LoginController {

    @Autowired
    private LoginService loginService;
    private static Logger log = Logger.getLogger(ExcelController.class);// 日志文件

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestParam("user") String user, @RequestParam("password")
            String password, ModelMap model) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        //TODO 验证账号密码不为空
        if(null == user || "".equals(user)) {
            result.put("msg", "参数错误");
            return result;
        }
        //TODO 验证账户号和密码是对的，如果通过，返回1，失败返回0
        Login u = loginService.getUserByUsername(user);
        if ( u == null ) {
            result.put("msg", "账号或密码不一致");
        if(null == password || "".equals(password)) {
            result.put("msg", "参数错误");
            return result;
        }

            return result;
        }

        //TODO 验证密码是否正确
        if ( !password.equals(u.getPassword()) ) {
            result.put("msg", "账号或密码不一致");
            return result;
        }

        //程序走到此处代表用户名和密码验证通过
        result.put("state", 1); //0代表失败，1代表成功
        log.error("");
        log.info("");
        return result;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> register(@RequestParam("user") String user,
                           @RequestParam("password") String password, ModelMap model) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        //TODO 验证账号密码不为空

        //根据用户名查询用户，如果查出来的用户已经存在，那就是数据库中已经有一个账户是这个名字，账户名冲突，否则不冲突可以新增用户
        Login u = loginService.getUserByUsername(user);
        if ( u != null ) {
            result.put("msg", "用户名已存在");
            return result;
        }

        //TODO 保存账号密码到数据库
        loginService.addUser(user, password);
        result.put("state", 1); //0代表失败，1代表成功
        log.error("");
        log.info("");
        return result;
    }

    @RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyPassword(@RequestParam("user") String user,
                                 @RequestParam("password") String password,
                                 @RequestParam("newpassword") String newpassword, ModelMap model) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        //TODO 验证账号密码不为空
        if(null == user || "".equals(user)) {
            result.put("msg", "参数错误");
            return result;
        }
        if(null == password || "".equals(password)) {
            result.put("msg", "参数错误");
            return result;
        }
        if(null == newpassword || "".equals(newpassword)) {
            result.put("msg", "参数错误");
            return result;
        }

        //TODO 根据用户名查询用户信息
        Login u = loginService.getUserByUsername(user);
        if ( u == null ) {
            result.put("msg", "用户名不存在");
            return result;
        }

        //TODO 验证原密码是否正确
        if ( !password.equals(u.getPassword()) ) {
            result.put("msg", "原密码错误");
            return result;
        }

        //TODO 更新用户密码为新密码
        loginService.modifyPasswordByUsername(user, newpassword);
        result.put("state", 1); //0代表失败，1代表成功
        log.error("");
        log.info("");
        return result;
    }



}
