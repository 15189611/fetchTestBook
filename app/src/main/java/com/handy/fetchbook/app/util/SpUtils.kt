package com.handy.fetchbook.app.util

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * MMKV使用封装
 *
 * @author Handy
 * @since 8/28/20
 */
object SpUtils {

    /**
     * 初始化
     */
    fun initMMKV(context: Context): String? = MMKV.initialize(context)

    /**
     * 保存数据（简化）
     * 根据value类型自动匹配需要执行的方法
     */
    fun put(key: String, value: Any) =
        when (value) {
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> putDouble(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            else -> false
        }

    fun putString(key: String, value: String): Boolean? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.encode(key, value)

    fun getString(key: String, defValue: String): String? =
        MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.decodeString(key, defValue)

    fun putInt(key: String, value: Int): Boolean? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.encode(key, value)

    fun getInt(key: String, defValue: Int): Int? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.decodeInt(key, defValue)

    fun putLong(key: String, value: Long): Boolean? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.encode(key, value)

    fun getLong(key: String, defValue: Long): Long? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.decodeLong(key, defValue)

    fun putDouble(key: String, value: Double): Boolean? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.encode(key, value)

    fun getDouble(key: String, defValue: Double): Double? =
        MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.decodeDouble(key, defValue)

    fun putFloat(key: String, value: Float): Boolean? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.encode(key, value)

    fun getFloat(key: String, defValue: Float): Float? =
        MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.decodeFloat(key, defValue)

    fun putBoolean(key: String, value: Boolean): Boolean? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.encode(key, value)

    fun getBoolean(key: String, defValue: Boolean): Boolean? =
        MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.decodeBool(key, defValue)

    fun contains(key: String): Boolean? = MMKV.mmkvWithID(SpKey.MMKV_WITH_ID)?.contains(key)
}