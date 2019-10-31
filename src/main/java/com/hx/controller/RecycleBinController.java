package com.hx.controller;

import com.hx.modle.RecycleBin;
import com.hx.service.RecycleBinService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 范聪敏
 * @date 2019/10/18 17:42
 * @desc
 */
@Controller
@RequestMapping("/recycle")
    public class RecycleBinController {

    private static Logger log = Logger.getLogger(ExcelController.class);// 日志文件

    @Resource
    private RecycleBinService recycleBinService;

    /**
     * 分页查询列表
     */
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryList(Integer pageNo, Integer pageSize, String title) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功

        //查询全部回收站列表
        List<RecycleBin> recycleBinList = recycleBinService.queryList(pageNo, pageSize, title);     //查询指定页的数据

        int totalCount = recycleBinService.getTotal(title);   //查询总条数
        result.put("list", recycleBinList);
        result.put("totalCount", totalCount);
        result.put("state", 1); //0代表失败，1代表成功
        return result;
    }

    /**
     * 还原,批量操作,多个id之间英文逗号分隔
     */
    @RequestMapping(value = "/restore", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> restore(String ids) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( StringUtils.isEmpty(ids) ) {
            result.put("msg", "参数错误");
            return result;
        }

        boolean b = recycleBinService.updateRestore(ids);
        if ( b ) {
            result.put("state", 1); //0代表失败，1代表成功
        }

        return result;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = new HashMap<>();
        result.put("state", 0); //0代表失败，1代表成功
        if ( StringUtils.isEmpty(ids) ) {
            result.put("msg", "参数错误");
            return result;
        }

        boolean b = recycleBinService.deleteById(ids);
        if ( b ) {
            result.put("state", 1); //0代表失败，1代表成功
        }

        return result;
    }
}