package com.hx.service.impl;

import com.hx.dao.OutboxMapper;
import com.hx.modle.Outbox;
import com.hx.service.OutBoxService;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
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
    @Override
    public List<Outbox> getAll(String[] ids) {

        return outboxMapper.getAll(ids);
    }
    @Override
    public int queryTotalCount(Map<String, Object> searchMap) {
        return outboxMapper.queryTotalCount(searchMap);
    }

    @Override
    public List<Outbox> queryoutBox(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);
        return outboxMapper.queryALLMailList(searchMap);
    }
    @Override
    public void modifyoutBox(Map<String, Object> searchMap) {

        outboxMapper.modifyoutBox(searchMap);
    }
    @Override
    public void modifoutbox(int id) {
        outboxMapper.modifoutBox(id);
    }
    @Override
    public List<Outbox> Recoveryoutbox(Map<String, Object> searchMap, Integer pageNo, Integer pageSize) {
        //mysql LIMIT语句 参数生成  LIMIT [start] [offset]
        int start = (pageNo - 1) * pageSize;
        int offset = pageSize;
        searchMap.put("start", start);
        searchMap.put("offset", offset);
        return outboxMapper.RecoveryoutBox(searchMap);
    }
    @Override
    public void reductionoutbox(String id) {
        outboxMapper.reductionoutBox(id);
    }
    @Override
    public boolean deleteoutbox(String ids) {
        if ( StringUtils.isEmpty(ids) ) return false;
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            //删除回收站
            outboxMapper.deleteoutbox(id);
        }
        return true;
    }
}
