package com.hx.service;/*
 */

import com.hx.modle.Log_Tables;
import com.hx.modle.Login;

import java.util.List;

public interface UserService {
    List<Login> selectUserList() throws Exception;

    void deleteUser(int id) throws Exception;

    void updateUser(int id, String password) throws Exception;

    List<Log_Tables> selectLog(Integer pageNow, Integer pageSize,String level) throws Exception;
}
