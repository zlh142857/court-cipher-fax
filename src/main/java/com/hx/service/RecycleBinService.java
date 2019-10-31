package com.hx.service;

import com.hx.modle.RecycleBin;

import java.util.Date;
import java.util.List;

/**
 * @author 范聪敏
 * @date 2019/10/18 17:30
 * @desc 回收站服务
 */
public interface RecycleBinService {

    List<RecycleBin> queryList(Integer pageNo, Integer pageSize, String title);

    int getTotal(String title);

    boolean updateRestore(String ids);

    boolean deleteById(String id);

    /**
     * 插入到回收站
     * @param recoverytime 回收时间
     * @param sendernumber 删除数据所在表
     * @param senderunit 删除数据的ID
     * @param receivenumber 删除数据标题
     * @param filsavepath 删除时间
     * @param create_time 删除时间
     * @param receiptpath 删除时间
     *  @param isreceipt 删除时间
     * @return 是否成功
     */
    int insertRecycleBin(String relateType, Date recoverytime, String sendernumber, String senderunit, String receivingunit, String receivenumber,
                         String filsavepath, Date create_time, String receiptpath, int isreceipt, String sendline, String message);


}
