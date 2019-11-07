package com.hx.dao;/*
 */

import com.hx.modle.Log_Tables;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogMapper {
    List<Log_Tables> selectLog(@Param("page")int page,@Param("pageSize")int pageSize,@Param("level")String levelInfo)throws Exception;

    Long selectCount()throws Exception;
}
