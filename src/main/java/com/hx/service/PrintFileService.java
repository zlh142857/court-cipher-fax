package com.hx.service;/*
 */

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

public interface PrintFileService {

    String printFile(MultipartFile[] files);

    void printScan() throws Exception;
}
