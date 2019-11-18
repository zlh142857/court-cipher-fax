package com.hx.service;

import com.hx.modle.RecycleBin;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/18 17:30
 * @desc 回收站服务
 */
public interface RecycleBinService {

    List<RecycleBin> queryList(Map<String, Object> searchMap, Integer pageNo, Integer pageSize);

    int getTotal(Map<String, Object> searchMap);

    boolean updateRestore(String ids);

    boolean deleteById(String id);

    /**
     * 插入到回收站
     * @param recoverytime 回收时间
     * @param isLink
     */
    int insertRecycleBin(String relateType,
                         Date recoverytime,
                         String sendernumber,
                         String senderunit,
                         String receivingunit,
                         String receivenumber,
                         String filsavepath,
                         Date create_time,
                         String receiptpath,
                         int isreceipt,
                         String sendline,
                         String message,
                         int isLink,
                         String barCode,
                         String tifPath);


   }
