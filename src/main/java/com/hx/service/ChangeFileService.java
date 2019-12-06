package com.hx.service;/*
 */

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ChangeFileService {
    Map<String,Object> changeFileSend(MultipartFile file);

    Map<String,Object> getFileList(String tifPath);
}
