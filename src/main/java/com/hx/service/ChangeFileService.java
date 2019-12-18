package com.hx.service;/*
 */

import com.hx.modle.Elec_Sign;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ChangeFileService {
    Map<String,Object> changeFileSend(MultipartFile file);

    Map<String,Object> getFileList(String tifPath);

    boolean addNewElecSign(String signName,String type);

    boolean delElecSign(Integer id, String savePath);

    List<Elec_Sign> selectElecSign();

    List<Elec_Sign> selectElecSignName();

    boolean sealOnFile(String tifPath,Integer id);
}
