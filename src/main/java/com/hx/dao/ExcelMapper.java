package com.hx.dao;

import java.util.List;
import java.util.Map;

import com.hx.modle.Mail;
import org.apache.ibatis.annotations.Param;


public interface ExcelMapper {

	void InputExcel(@Param("params") Map<String, Object> ginsengMap);

	List<Mail> getAll();

    Integer deleteMany(int[] id_arr);

    List<Mail> getById(@Param("id") String id);


}
