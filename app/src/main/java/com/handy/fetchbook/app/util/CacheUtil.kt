package com.handy.fetchbook.app.util

import android.text.TextUtils
import com.google.gson.Gson
import com.handy.fetchbook.data.bean.me.UserInfoBean
import com.handy.fetchbook.data.bean.model.TokenInfoModel
import com.tencent.mmkv.MMKV

object CacheUtil {
    /**
     * 获取token信息
     */
    fun getTokenInfo(): TokenInfoModel? {
        val kv = MMKV.mmkvWithID("app")
        val userInfoString = kv.decodeString(SpKey.TOKEN_INFO)
        return if (TextUtils.isEmpty(userInfoString)) {
            null
        } else {
            Gson().fromJson(userInfoString, TokenInfoModel::class.java)
        }
    }

    /**
     * 设置token信息
     */
    fun saveTokenInfo(tokenResponse: TokenInfoModel?) {
        val kv = MMKV.mmkvWithID("app")
        if (tokenResponse == null) {
            kv.encode(SpKey.TOKEN_INFO, "")
        } else {
            kv.encode(SpKey.TOKEN, tokenResponse.token)
            kv.encode(SpKey.TOKEN_INFO, Gson().toJson(tokenResponse))
        }
    }

    fun saveUserInfo(userInfoBean: UserInfoBean?) {
        val kv = MMKV.mmkvWithID("app")
        if (userInfoBean == null) {
            kv.encode(SpKey.USER_INFO, "")
        } else {
            kv.encode(SpKey.USER_INFO, Gson().toJson(userInfoBean))
        }
    }

    fun getUserInfo(): UserInfoBean? {
        val kv = MMKV.mmkvWithID("app")
        val userInfoString = kv.decodeString(SpKey.USER_INFO)
        return if (TextUtils.isEmpty(userInfoString)) {
            null
        } else {
            Gson().fromJson(userInfoString, UserInfoBean::class.java)
        }
    }

    fun getToken(): String? {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeString(SpKey.TOKEN, "")
    }

    /**
     * 清空账户信息
     */
    fun clearUserAll() {
        val kv = MMKV.mmkvWithID("app")
        kv.encode(SpKey.TOKEN, "")
        saveTokenInfo(null)
        saveUserInfo(null)
        setIsLogin(false)
    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool(SpKey.IS_LOG_IN, false)
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode(SpKey.IS_LOG_IN, isLogin)
    }

    /**
     * 获取翻译语种
     */
    fun getLanguage(): String {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeString(SpKey.LANGUAGE, "kr").toString()
    }

    /**
     * 设置翻译内容
     */
    fun setLanguage(language: String) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode(SpKey.LANGUAGE, language)
    }

    /**
     * 获取翻译内容
     */
    fun getTranslate(): String {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeString(SpKey.TRANSLATE, "").toString()
    }

    /**
     * 设置翻译内容
     */
    fun setTranslate(translate: String) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode(SpKey.TRANSLATE, translate)
    }

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