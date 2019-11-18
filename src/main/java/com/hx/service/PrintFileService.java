package com.hx.service;/*
 */

import com.hx.modle.Program_Setting;
import java.util.Map;

public interface PrintFileService {

    String printFile(String tifPath) throws Exception;

    String printScan() throws Exception;

    Map<String,Object> selectPrintService();

    void updatePrintService(Program_Setting programSetting);
}
