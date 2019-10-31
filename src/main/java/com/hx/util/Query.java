package com.hx.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
public class Query extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    // 偏移量
    private int offset;
    // 每页条数
    private int limit;

    public Query() {
    }

    public Query(Map<String, Object> params) {
        // 过滤空属性
        Map<String, Object> mapParams = MapUtil.DelNull(params);

        this.putAll(mapParams);
        // 分页参数
        if (mapParams.get("page") != null && mapParams.get("limit") != null) {
            int page = Integer.parseInt(mapParams.get("page").toString());
            this.limit = Integer.parseInt(mapParams.get("limit").toString());
            this.offset = (page -1) * this.limit;

            this.put("limit", limit);
            this.put("offset", offset);
            this.put("page", page);
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.put("offset", offset);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
