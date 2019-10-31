package com.hx.service.impl;

import com.hx.dao.OutboxMapper;
import com.hx.modle.Outbox;
import com.hx.service.OutBoxService;
import com.hx.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/9/28 3:17
 * @desc
 */
@Service
public class OuBoxServiceImpl implements OutBoxService {
    @Resource
    private OutboxMapper outboxMapper;

    @Autowired
    RecycleBinService recycleBinService;

    @Override
    public List<Outbox> getAll() {

        return outboxMapper.getAll();
    }

    @Override
    public int queryTotalCount(Map<String, Object> searchMap) {
        return outboxMapper.queryTotalCount(searchMap);
    }

    @Override
    public List<Outbox> queryALLMailList(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);
        return outboxMapper.queryALLMailList(searchMap);
    }

    @Override
    public int deloutbox(Integer id) {

        Outbox outbox = outboxMapper.getById(id);

        recycleBinService.insertRecycleBin("outbox", new Date(), outbox.getSendernumber(),
                String.valueOf(id), outbox.getReceivingunit(),outbox.getReceivenumber(),"",
                (Date) outbox.getCreate_time(),"",0, outbox.getSendline(),outbox.getMessage());

        return outboxMapper.deloutbox(id);
    }

    @Override
    public int insert(Outbox outbox) {
        return outboxMapper.insert(outbox);
    }
}
