package com.hx.service;/*
 */

import com.hx.modle.ChMsg;
import com.hx.modle.Device_Setting;
import com.hx.modle.TempModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface SendFaxService {


    String sendFax(String tifPath, String base64, String courtName,
                   String receiveNumber, String sendNumber, String isBack,String filename,String id,String ch) throws Exception;

    List<Device_Setting> selectSeatNumber();

    int selectChType(int ch);

    String baseToTif(String base64);

    String createBarCode() throws IOException;

    List<ChMsg> rateOfAdvance(String ch) throws Exception;

    List<Device_Setting> selectChAndSeatNumber();

    boolean insertSchTask(String tifPath, String receiptPath, List<TempModel> tempList, String sendNumber, String isBack, String filename, String id, String ch, String sendTime) throws ParseException, InterruptedException, IOException;

    boolean deleteSchTask(int id);

    boolean updateTaskDate(String id, String date) throws ParseException;

    Map<String,Object> selectTaskLimit(Integer pageNow, Integer pageSize);

    boolean undoSend(String ch);

    String returnFaxGetPath(String tifPath) throws Exception;

    Map<String,Object> bigToSmallTiff(String tifPath, int[] pages);

    Map<String,Object> checkChByHand();

    String sendFaxByHand(String tifPath, String receiptPath, String courtName, String receiveNumber, Integer isBack, Integer ch, String filename, Integer id);

    String selectNotifyPhone(String receiveNumber);

    String telNotify(Integer id,String telNotify) throws InterruptedException;
}
