package com.hx.util;



import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    /**
     * 简化特殊查询 的判断语句
     * @param map
     * @return
     */
    public static Map<String, Object> DelNull(Map<String, Object> map) {
        Map<String, Object> map2 = new HashMap<String, Object>();
        for (String key : map.keySet()) {
            if (map.get(key) != null && StringUtils.isNotEmpty(map.get(key).toString())) {
                map2.put(key, map.get(key));
            }
        }
        return map2;
    }

    /**
     * 如果map中某个字段为null变成""
     * @param map
     * @return
     */
    public static Map<String, Object> TransNullToStr(Map<String, Object> map) {
        Map<String, Object> map2 = new HashMap<String, Object>();
        for (String key : map.keySet()) {
            String o = String.valueOf(map.get(key)).equals("null") ? "" : map.get(key).toString();
            map2.put(key, o);
        }
        return map2;

    }

    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }
}
