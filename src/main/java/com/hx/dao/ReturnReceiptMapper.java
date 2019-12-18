package com.hx.dao;

import com.hx.modle.Return_Receipt;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/16 17:20
 * @desc
 */
public interface ReturnReceiptMapper {
        int queryTotalCount(@Param("params") Map<String, Object> searchMap);

        List<Return_Receipt> queryALLMail(@Param("params") Map<String, Object> searchMap);

        List<Return_Receipt> queryALLMailList();

        Return_Receipt getById(int id);

        int insert(@Param("return_receipt") Return_Receipt return_receipt);

        List<Return_Receipt> getAll(@Param("ids") String[] ids);

        void modifReturnReceipt(int id);

        List<Return_Receipt> RecoveryoReturnReceipt(@Param("params") Map<String, Object> searchMap);

        void reductionReturnReceipt(String id);

        void deleteReturnReceipt(String id);

        void insertReceipt(Return_Receipt returnReceipt);

    List<String> selectFilePath(int fileDays);
}