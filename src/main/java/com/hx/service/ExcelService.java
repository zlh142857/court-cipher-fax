package com.hx.service;

import java.io.InputStream;
import java.util.List;

import com.hx.modle.Mail;


public interface ExcelService {

	String InputExcel(InputStream is, String originalFilename, String id);

	List<Mail> getById(String id);
}
