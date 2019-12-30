package com.hx.service;/*
 */

import com.hx.modle.Program_Setting;
import java.util.Map;

public interface PrintFileService {

    String printFile(String tifPath) throws Exception;

    String printScan();

    Map<String,Object> selectPrintService();

    boolean updatePrintService(Program_Setting programSetting);

    String downFileSend(String tifPath) throws Exception;

    String downTifSign(String tifPath);
}
