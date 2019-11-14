package com.hx.dao;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/12 14:27
 *@功能:
 */

import com.hx.modle.Sch_Task;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface SchMapper {

    List<Sch_Task> selectTask();

    void insertSchTask(Sch_Task schTask);

    void deleteSchTask(Integer id);

    void updateTaskDate(@Param( "id" ) Integer id,@Param( "sendDate" ) Date sendDate);

    Long selectCount();

    List<Sch_Task> selectTaskLimit(@Param( "page" ) Integer page,@Param( "pageSize")Integer pageSize);
}
