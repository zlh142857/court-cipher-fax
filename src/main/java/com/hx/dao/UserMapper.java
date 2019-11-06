package com.hx.dao;/*
 */

import com.hx.modle.Login;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    List<Login> selectUserList()throws Exception;

    void deleteUser(int id)throws Exception;

    void updateUser(@Param( "id" ) int id,@Param( "password" ) String password)throws Exception;
}
