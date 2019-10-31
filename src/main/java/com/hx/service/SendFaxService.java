package com.hx.service;/*
 */

import com.hx.modle.Device_Setting;

import java.util.List;

public interface SendFaxService {


    String sendFax(String tifPath, String base64, String courtName, String receiveNumber, String sendNumber, int isBack,int ch,String filename);

    List<String> selectSeatNumber();

    List<Device_Setting> selectChType();

    String baseToTif(String base64);
}
