package com.hx.service.impl;

import com.hx.dao.LoginMapper;

import com.hx.modle.Login;
import com.hx.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 范聪敏
 * @date 2019/9/20 10:23
 * @desc
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Resource
    private LoginMapper loginMapper;


    @Override
    public boolean isUserExist(String username) {
        if (loginMapper.findUsersByUsername(username) == null) {
            return false;
        } else {

            return true;

        }
    }

    @Override
    public String getPasswordByUsername(String user) {
        return null;
    }

    @Override
    public void addUser(String username, String password) {

        Login u = new Login();
        u.setUser(username);
        u.setPassword(password);
        loginMapper.addUser(u);
    }

    @Override
    public void modifyPasswordByUsername(String user, String newpassword) {

        Login u = loginMapper.findUsersByUsername(user);
        u.setUser(user);
        u.setPassword(newpassword);
        loginMapper.modifyPasswordByUsername(u);
    }

    @Override
    public Login getUserByUsername(String user) {
        return loginMapper.getUserByUsername(user);
    }


}
