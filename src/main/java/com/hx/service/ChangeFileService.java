package com.hx.service;/*
 */

import org.springframework.web.multipart.MultipartFile;

public interface ChangeFileService {
    String changeFileSend(MultipartFile file);
}
