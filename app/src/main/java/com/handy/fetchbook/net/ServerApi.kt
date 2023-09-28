package com.handy.fetchbook.net

import com.handy.fetchbook.model.CaptchaCheckOt
import com.handy.fetchbook.model.CaptchaGetIt
import com.handy.fetchbook.model.Input
import com.handy.fetchbook.model.WordCaptchaGetIt
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Date:2020/4/29
 * author:wuyan
 */
interface ServerApi {

    //获取验证码
    @GET("api/captchaImage")
    fun getAsync(): Deferred<Response<Input<CaptchaGetIt>>>

    //获取文字的验证码
    @GET("api/captchaImage")
    fun getWordCaptchaAsync(): Deferred<Response<Input<WordCaptchaGetIt>>>

    //核对验证码
    @POST("api/captchaImage")
    fun checkAsync(@Body body: CaptchaCheckOt): Deferred<Response<Input<Any>>>

}