package com.hx.service.impl;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/6 17:16
 *@功能:
 */

import com.hx.dao.LogMapper;
import com.hx.dao.UserMapper;
import com.hx.modle.Log_Tables;
import com.hx.modle.Login;
import com.hx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LogMapper logMapper;
    @Override
    public List<Login> selectUserList() throws Exception {
        List<Login> userList=userMapper.selectUserList();
        return userList;
    }

    @Override
    public void deleteUser(int id) throws Exception {
        userMapper.deleteUser(id);
    }

    @Override
    public void updateUser(int id, String password) throws Exception {
        userMapper.updateUser(id,password);
    }

    @Override
    public Map<String,Object> selectLog(Integer pageNow, Integer pageSize, String level) throws Exception {
        Map<String,Object> map=new HashMap<>(  );
        int page=(pageNow-1)*pageSize;
        String levelInfo="";
        if(level != null || level != ""){
            if(level.equals( "1" )){
                levelInfo="ERROR";
            }else{
                levelInfo="INFO";
            }
        }
        List<Log_Tables> logList=logMapper.selectLog(page,pageSize,levelInfo);
        Long total=logMapper.selectCount();
        map.put( "logList",logList);
        map.put( "total",total );
        return map;
    }
}
