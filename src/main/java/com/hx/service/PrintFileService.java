package com.hx.service;/*
 */

import com.hx.modle.Program_Setting;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Map;

public interface PrintFileService {

    String printFile(MultipartFile[] files);

    void printScan() throws Exception;

    Map<String,Object> selectPrintService();

    void updatePrintService(Program_Setting programSetting);
}
