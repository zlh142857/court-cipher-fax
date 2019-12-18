package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/6 17:14
 *@功能:
 */

import com.alibaba.fastjson.JSONObject;
import com.hx.common.Fax;
import com.hx.modle.Login;
import com.hx.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;



@Controller
public class UserController {
    private static Logger logger=Logger.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    /**
     *
     * 功能描述: 查询用户管理所有用户信息
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/6 17:17
     */
    @RequestMapping("selectUserList")
    @ResponseBody
    public String selectUserList(){
        List<Login> userList= null;
        try {
            userList = userService.selectUserList();
        } catch (Exception e) {
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( userList );
    }
    @RequestMapping("deleteUser")
    @ResponseBody
    public String deleteUser(String id){
        String mes="删除失败";
        try {
            if(id != null){
                userService.deleteUser( Integer.parseInt( id ) );
                mes="删除成功";
            }else{
                mes="删除失败";
            }
        } catch (Exception e) {
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( mes );
    }
    @RequestMapping("updateUser")
    @ResponseBody
    public String updateUser(String id,String password){
        String msg="修改失败";
        try {
            if(id != null && password != null){
                userService.updateUser( Integer.parseInt( id ),password);
                msg="修改成功";
            }else{
                msg="修改失败";
            }
        } catch (Exception e) {
            logger.error( e.toString() );
        }
        return JSONObject.toJSONString( msg );
    }
    /**
     *
     * 功能描述: 查询分页日志
     *
     * 业务逻辑:
     *
     * @param:
     * @return:
     * @auther: 张立恒
     * @date: 2019/11/6 17:57
     */
    @RequestMapping("selectLog")
    @ResponseBody
    public String selectLog(String pageNow,String pageSize,String level){
        Map<String,Object> map=null;
        try {
            if(pageNow != null && pageSize != null){
                map = userService.selectLog(Integer.valueOf( pageNow ),Integer.valueOf( pageSize ),level);
            }else{
                logger.error( "分页参数为空" );
            }
        } catch (Exception e) {
            logger.error( e.toString() );
        }
        return JSONObject.toJSONStringWithDateFormat( map,"yyyy-MM-dd HH:mm:ss" );
    }

}
