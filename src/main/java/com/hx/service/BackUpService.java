package com.hx.service;/*
 */

import java.util.Map;

public interface BackUpService {
    Map<String,Object> selectBackUpFilePath(Integer flag) throws Exception;

    Map<String,Object> recoverBackUp(String filePath,Integer flag) throws Exception;

    Map<String,Object> startBackUp() throws Exception;
}
