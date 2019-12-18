package com.hx.service;/*
 */

import com.hx.modle.Program_Setting;
import java.util.Map;

public interface PrintFileService {

    String printFile(String tifPath) throws Exception;

    String printScan() throws Exception;

    Map<String,Object> selectPrintService();

    boolean updatePrintService(Program_Setting programSetting);

    String downFileSend(String tifPath) throws Exception;

    String downTifSign(String tifPath);
}
