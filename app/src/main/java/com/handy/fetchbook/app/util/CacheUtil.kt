package com.handy.fetchbook.app.util

import android.text.TextUtils
import com.google.gson.Gson
import com.handy.fetchbook.data.bean.model.TokenInfoModel
import com.tencent.mmkv.MMKV

object CacheUtil {
    /**
     * 获取保存的账户信息
     */
    fun getToken(): TokenInfoModel? {
        val kv = MMKV.mmkvWithID("app")
        val tokenStr = kv.decodeString("token")
        return if (TextUtils.isEmpty(tokenStr)) {
            null
        } else {
            Gson().fromJson(tokenStr, TokenInfoModel::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setToken(tokenResponse: TokenInfoModel?) {
        val kv = MMKV.mmkvWithID("app")
        if (tokenResponse == null) {
            setIsLogin(false)
            kv.encode("token", "")
        } else {
            setIsLogin(true)
            kv.encode("token", Gson().toJson(tokenResponse))
        }
    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login", false)
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login", isLogin)
    }



    /**
     * 获取翻译语种
     */
    fun getLanguage(): String {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeString("language", "kr").toString()
    }

    /**
     * 设置翻译内容
     */
    fun setLanguage(language: String) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("language", language)
    }

    /**
     * 获取翻译内容
     */
    fun getTranslate(): String {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeString("translate", "").toString()
    }

    /**
     * 设置翻译内容
     */
    fun setTranslate(translate: String) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("translate", translate)
    }
}