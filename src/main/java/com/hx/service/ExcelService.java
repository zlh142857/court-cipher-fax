package com.hx.service;

import java.io.InputStream;
import java.util.List;

import com.hx.modle.Mail;
import com.hx.modle.Mail_List;


public interface ExcelService {

	String InputExcel(InputStream is, String originalFilename);

	List<Mail> OutputExcel();

	List<Mail> getAll();

    Integer deleteMany(int[] chk_value);

	List<Mail> getById(String id);
}