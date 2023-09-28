package com.handy.fetchbook.app.util

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON.parseObject
import com.alibaba.fastjson.JSONObject

object LanUtil {
    /**
     * Json 转成 Map<>
     */
    fun getText(str: String): String {
        var txt = ""
        try {
            val jsonObject = parseObject(CacheUtil.getTranslate())
            val data: JSONObject = jsonObject.getJSONObject("datas")
            when (CacheUtil.getLanguage()) {
                "zhCn" -> {
                    val zhCn: String = data.getString("zhCn")
                    val mapRes = parseObject<Map<*, *>>(
                        zhCn,
                        MutableMap::class.java
                    )
                    
                    txt = mapRes[str].toString()
                }
                "enUs" -> {
                    val enUs: String = data.getString("enUs")
                    val mapRes = parseObject<Map<*, *>>(
                        enUs,
                        MutableMap::class.java
                    )
                    txt = mapRes[str].toString()
                }
                "kr" -> {
                    val enUs: String = data.getString("kr")
                    val mapRes = parseObject<Map<*, *>>(
                        enUs,
                        MutableMap::class.java
                    )
                    txt = mapRes[str].toString()
                }
            }
        } catch (e: Exception) {
            Log.e("LanUtil", e.toString())
            return str
        }
        return if (TextUtils.isEmpty(txt) || txt == "null") str else txt
    }
}