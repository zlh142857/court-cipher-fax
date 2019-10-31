package com.hx.service;/*
 */

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Map;

public interface PrintFileService {

    String printFile(MultipartFile[] files);

    void printScan() throws Exception;

    Map<String,Object> selectPrintService();
}
