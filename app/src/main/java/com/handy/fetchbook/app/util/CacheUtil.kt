package com.handy.fetchbook.app.util

import android.text.TextUtils
import com.google.gson.Gson
import com.handy.fetchbook.data.bean.me.UserInfoBean
import com.handy.fetchbook.data.bean.model.TokenInfoModel
import com.tencent.mmkv.MMKV

object CacheUtil {
    /**
     * 获取token账户信息
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
}