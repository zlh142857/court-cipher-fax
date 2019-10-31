package com.hx.service;

import com.hx.modle.Login;

/**
 * @author 范聪敏
 * @date 2019/9/20 10:23
 * @desc
 */
public interface LoginService {
    boolean isUserExist(String username);

    String getPasswordByUsername(String username);

    void addUser(String username, String password);

    void modifyPasswordByUsername(String user, String newpassword);

    Login getUserByUsername(String user);
}
