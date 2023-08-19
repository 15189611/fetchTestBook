package com.handy.fetchbook.app.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthInterceptor : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return null
//        return if (ActivityUtils.getTopActivity().javaClass.name == LoginActivity::class.java.name) {
//            CacheUtil.setToken(null)
//            response.request.newBuilder()
//                .header("Authorization", ApiService.Authorization)
//                .build()
//        } else {
//            showTips(LanUtil.getText("登录已失效，请重新登录"))
//            ActivityUtils.startActivity(LoginActivity::class.java)
//            null
//        }
    }
}