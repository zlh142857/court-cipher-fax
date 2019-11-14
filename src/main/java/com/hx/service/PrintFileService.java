package com.hx.service;/*
 */

import com.hx.modle.Program_Setting;
import com.hx.modle.TempModel;

import java.util.List;
import java.util.Map;

public interface PrintFileService {

    String printFile(String tifPath) throws Exception;

    String printScan() throws Exception;

    Map<String,Object> selectPrintService();

    void updatePrintService(Program_Setting programSetting);
}
