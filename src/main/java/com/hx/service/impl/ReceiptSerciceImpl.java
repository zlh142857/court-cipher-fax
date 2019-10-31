package com.hx.service.impl;

import com.hx.dao.ReceiptMapper;
import com.hx.modle.Return_Receipt;
import com.hx.service.ReceiptService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 范聪敏
 * @date 2019/10/16 17:20
 * @desc
 */
public class ReceiptSerciceImpl implements ReceiptService {
        @Resource
        private ReceiptMapper receiptMapper;
        @Override
        public List<Return_Receipt> queryReturn() {
            return receiptMapper.queryReturn();
}
}
