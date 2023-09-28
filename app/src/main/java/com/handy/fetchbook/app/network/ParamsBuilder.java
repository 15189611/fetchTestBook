package com.handy.fetchbook.app.network;

import java.util.Map;
import java.util.TreeMap;

/**
 * - Author: Charles
 * - Date: 2023/9/1
 * - Description:
 */
public class ParamsBuilder extends TreeMap<String, Object> {

    private ParamsBuilder() {
    }

    public static ParamsBuilder newParams() {
        return new ParamsBuilder();
    }

    public static ParamsBuilder newParams(Map<String, Object> map) {
        ParamsBuilder paramsBuilder = new ParamsBuilder();
        if (map != null && map.size() > 0) {
            paramsBuilder.putAll(map);
        }
        return paramsBuilder;
    }

    public ParamsBuilder addParams(String key, Object value) {
        put(key, value);
        return this;
    }

    public ParamsBuilder addParams(Map<String, Object> map) {
        putAll(map);
        return this;
    }
}
