package com.hx.service;

import com.hx.modle.Return_Receipt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/16 17:21
 * @desc
 */
@Service
public interface ReturnReceiptService {
   int insert(Return_Receipt return_receipt);
   int queryTotalCount(Map<String, Object> searchMap);

   List<Return_Receipt> queryALLMail(Map<String,Object> searchMap, Integer pageNo, Integer pageSize);

   int deinbox(Integer readerId);

   List<Return_Receipt> getAll(String[] split);

}
