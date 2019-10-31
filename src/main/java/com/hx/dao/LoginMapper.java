package com.hx.dao;

import com.hx.modle.Login;

/**
 * @author 范聪敏
 * @date 2019/9/3 9:57
 * @desc
 */
public interface LoginMapper {


    Login findUsersByUsername(String username);

    void addUser(Login user);

    void modifyPasswordByUsername(Login user);

    Login getUserByUsername(String user);
}
