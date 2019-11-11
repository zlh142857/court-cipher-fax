package com.hx.dao;

import com.hx.modle.RecycleBin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/18 17:29
 * @desc 回收站
 */
public interface RecycleBinMapper {

    List<RecycleBin> queryList(@Param("params") Map<String, Object> searchMap);

    int getTotal(@Param("params") Map<String, Object> searchMap);

    RecycleBin getById(@Param("id") String id);

    int updateRestore(@Param("relateId") String relateId, @Param("relateType") String relateType);

    int deleteById(@Param("id") String id);

    int insert(@Param("recycleBin") RecycleBin recycleBin);


}
