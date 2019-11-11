package com.hx.service;/*
 */

import com.hx.modle.Login;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Login> selectUserList() throws Exception;

    void deleteUser(int id) throws Exception;

    void updateUser(int id, String password) throws Exception;

    Map<String,Object> selectLog(Integer pageNow, Integer pageSize, String level) throws Exception;
}
