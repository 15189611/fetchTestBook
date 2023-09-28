package com.handy.fetchbook.app.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

/**
 * - Author: Charles
 * - Date: 2023/9/1
 * - Description:
 */
public class PostJsonBody extends RequestBody {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private String content;
    private TreeMap<String, Object> contentMap;

    public PostJsonBody(@NonNull String content) {
        this.content = content;
    }

    public PostJsonBody(@NonNull TreeMap<String, Object> contentMap) {
        this.contentMap = contentMap;
        Iterator<Map.Entry<String, Object>> it = contentMap.entrySet().iterator();
        // 删除空数据
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (entry != null && entry.getValue() == null) {
                it.remove();
            }
        }
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return JSON;
    }

    public static PostJsonBody empty() {
        return create(ParamsBuilder.newParams());
    }

    public static PostJsonBody create(@NonNull String content) {
        return new PostJsonBody(content);
    }

    public static PostJsonBody create(@NonNull ParamsBuilder params) {
        return new PostJsonBody(params);
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        String requestContent = contentMap != null ? toJsonNonNull(contentMap) : content;
        byte[] bytes = requestContent != null ? requestContent.getBytes(CHARSET) : null;
        if (bytes == null) {
            throw new NullPointerException("content == null");
        }
        Util.checkOffsetAndCount(bytes.length, 0, bytes.length);
        sink.write(bytes, 0, bytes.length);
    }

    @NonNull
    public static String toJsonNonNull(@Nullable Object obj) {
        String json = toJson(obj);
        if (json == null) {
            return "";
        }
        return json;
    }

    @Nullable
    public static String toJson(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        if (obj instanceof CharSequence || isPrimitive(clazz)) {
            return valueOf(obj);
        }
        try {
            return new Gson().toJson(obj);
        } catch (Exception e) {
            Map<String, String> extraData = new HashMap<>();
            extraData.put("class", clazz.toString());
            extraData.put("json", obj.toString());
            return null;
        }
    }

    @NonNull
    public static String valueOf(@Nullable Object obj) {
        if (obj instanceof Float || obj instanceof Double) {
            Number number = (Number) obj;
            if (number.doubleValue() == number.longValue()) {
                return String.valueOf(number.longValue());
            }
        }
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }
    }

    //是否为基本类型
    public static <T> boolean isPrimitive(Class<T> rawType) {
        return isTargetType(PRIMITIVE_TYPES, rawType);
    }

    //是否为目标类型
    private static <T> boolean isTargetType(Class<?>[] targets, Class<T> rawType) {
        for (Class<?> standardPrimitive : targets) {
            if (standardPrimitive.isAssignableFrom(rawType)) {
                return true;
            }
        }
        return false;
    }

    //所有基本类型
    private static final Class<?>[] PRIMITIVE_TYPES = {int.class, long.class, short.class,
            float.class, double.class, byte.class, boolean.class, char.class, Integer.class, Long.class,
            Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};

}


