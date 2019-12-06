package com.hx.service;

import com.hx.modle.Send_Receipt;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/31 15:43
 * @desc
 */
@Service
public interface SendReceiptService {
    int queryTotalCount(Map<String, Object> searchMap);

    List<Send_Receipt> queryALLMail(Map<String,Object> searchMap, Integer pageNo, Integer pageSize);

    List<Send_Receipt> queryALLMailList();

    List<Send_Receipt> getAll(String[] ids);

    void modifysendReceipt(Map<String,Object> searchMap);

    void modifSendReceipt(int id);

    List<Send_Receipt> RecoverySendReceipt(Map<String,Object> searchMap, Integer pageNo, Integer pageSize);

    void reductionSendReceipt(String id);

    boolean deleteSendReceipt(String id);

    boolean updateIsLink(String inBoxId);

    String checkText(String pdfPath,String tifPath) throws Exception;

}
