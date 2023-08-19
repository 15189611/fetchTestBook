package com.handy.fetchbook.app.network

import android.util.Log
import com.handy.fetchbook.app.ext.getLanguage
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.constant.SpKey
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 自定义头部参数拦截器，传入heads
 */
class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (CacheUtil.isLogin()) {
            var token = SpUtils.getString(SpKey.TOKEN, "")
            builder
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", "Bearer $token")
                .addHeader("language", getLanguage())
        } else {
            builder
                .addHeader("Accept", "*/*")
                .addHeader("language", getLanguage())
        }
        return chain.proceed(builder.build())
    }

}