package com.hx.service;/*
 */

import com.hx.modle.Device_Setting;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SendFaxService {


    String sendFax(String tifPath, String base64, String courtName,
                   String receiveNumber, String sendNumber, String isBack,String filename,String id,String ch);

    List<Device_Setting> selectSeatNumber();

    List<Device_Setting> selectChType();

    String baseToTif(String base64);

    String createBarCode() throws IOException;
}
