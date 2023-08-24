package com.handy.fetchbook.app.network

import com.handy.fetchbook.data.bean.AnnouncementBean
import com.handy.fetchbook.data.bean.expo.ExpoListBean
import com.handy.fetchbook.data.bean.model.ApiResponse
import com.handy.fetchbook.data.bean.expo.ExpoDetailsBean
import com.handy.fetchbook.data.bean.home.*
import com.handy.fetchbook.data.bean.me.*
import com.handy.fetchbook.data.bean.model.BaseApiModel
import com.handy.fetchbook.data.bean.task.TaskStatusBean
import retrofit2.http.*

/**
 * 描述　: 网络API
 */
interface ApiService {

    companion object {
        const val SERVER_URL = "http://p28.bbq.bet/"
    }

    /**
     * 获取首页Banner数据
     *
     * @return ApiResponse<List<BannerBean>> Banner列表
     */
    @GET("api/home/banner")
    suspend fun banner(): ApiResponse<BannerBean>

    /**
     * 景点 scenics
     *
     */
    @GET("api/home/scenics")
    suspend fun scenics(
        @Query("page") page: Int?,
        @Query("region") region: String
    ): ApiResponse<ScenicsBean>

    /**
     * 景点详情 scenic
     *
     */
    @GET("api/home/scenic/{id}")
    suspend fun details(@Path("id") id: String): ApiResponse<ScenicsDetailsBean>


    @GET("api/notice/total")
    suspend fun total(): ApiResponse<InfoCenterTotalBean>


    /**
     * 登录 login
     */
    @FormUrlEncoded
    @POST("api/home/evaluate")
    suspend fun evaluate(
        @Field("id") id: String,
        @Field("type") type: Int
    ): ApiResponse<Any>


    /**
     * 注册 Register
     */
    @FormUrlEncoded
    @POST("api/register")
    suspend fun register(
        @Field("account") account: String,
        @Field("password") password: String,
        @Field("code") code: String,
        @Field("invite_code") invite_code: String
    ): ApiResponse<Any>


    /**
     * 忘记密码 Forgot Password
     */
    @FormUrlEncoded
    @POST("api/forgotPassword")
    suspend fun forgotPassword(
        @Field("account") account: String,
        @Field("code") code: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): ApiResponse<Any>

    /**
     * 登录 login
     */
    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("account") account: String,
        @Field("password") password: String,
        @Field("token") token: String,
        @Field("pointJson") pointJson: String
    ): ApiResponse<Any>

    /**
     * 发送短信 sendCode
     */
    @FormUrlEncoded
    @POST("api/sendCode")
    suspend fun sendCode(
        @Field("phone") phone: String,
        @Field("event") event: String
    ): ApiResponse<Any>


    /**
     * 发送短信 sendCode
     */
    @FormUrlEncoded
    @POST("api/user/sendCode")
    suspend fun sendCodeForget(
        @Field("event") event: String
    ): ApiResponse<Any>


    /**
     * config
     */
    @GET("api/config")
    suspend fun config(): ApiResponse<Any>

    /**
     * 获取验证图片 login
     */
    @GET("api/captchaImage")
    suspend fun captchaImage(): ApiResponse<Any>

    /**
     * 滑动验证码检查
     */
    @FormUrlEncoded
    @POST("api/captchaImage")
    suspend fun captchaImage(
        @Field("token") token: String,
        @Field("pointJson") pointJson: String
    ): ApiResponse<Any>

//--------用户中心-----------
    /**
     * 用户信息 UserInfo
     */
    @GET("api/user/info")
    suspend fun userInfo(): ApiResponse<UserInfoBean>

    /**
     * 登出 Logout
     */
    @GET("api/logout")
    suspend fun logout(): ApiResponse<Any>


    @GET("api/helpCenter")
    suspend fun helpCenter(): ApiResponse<List<HelpCenterBean>>


    @GET("api/wallet")
    suspend fun wallet(): ApiResponse<WalletBean>


    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST("api/user/changePassword")
    suspend fun changePassword(
        @Field("code") code: String,
        @Field("password_new") password_new: String,
        @Field("password_confirmation") password_confirmation: String
    ): ApiResponse<Any>

    /**
     * 滑动验证码检查
     */
    @FormUrlEncoded
    @POST("api/investment/buyMembership")
    suspend fun buyMembership(
        @Field("type") code: Int
    ): ApiResponse<Any>


    /**
     * 世博列表
     *
     */
    @GET("api/expo/list")
    suspend fun list(
        @Query("region") region: String?,
        @Query("page") page: Int?
    ): ApiResponse<ExpoListBean>

    /**
     * 世博列表
     *
     */
    @GET("api/expo/details/{id}")
    suspend fun details(@Path("id") id: Int): ApiResponse<ExpoDetailsBean>

    /**
     * 登录 login
     */
    @FormUrlEncoded
    @POST("api/expo/commentExpo")
    suspend fun commentExpo(
        @Field("expo_id") expo_id: String,
        @Field("comment") comment: String,
        @Field("rating") rating: Int
    ): ApiResponse<ArrayList<String>>

    /**
     *
     */
    @GET("api/home/socialMedia")
    suspend fun socialMedia(
    ): ApiResponse<List<SocialMediaBean>>


    /**
     *
     */
    @GET("api/notice/message")
    suspend fun message(
        @Query("type") region: Int?,
        @Query("page") page: Int?
    ): ApiResponse<SystemInfoBean>


    /**
     *
     */
    @GET("api/notice/message")
    suspend fun announcements(
        @Query("page") page: Int?
    ): ApiResponse<NoticeBean>


    /**
     *
     */
    @GET("api/home/searchScenic")
    suspend fun searchScenic(
        @Query("keyword") keyword: String?,
        @Query("page") page: Int?
    ): ApiResponse<List<SearchBean>>



    /**
     *
     */
    @POST("api/prize/draw")
    suspend fun draw(
    ): BaseApiModel

    /**
     *
     */
    @GET("api/task/status")
    suspend fun taskStatus(
    ): ApiResponse<TaskStatusBean>


    /**
     *
     */
    @POST("api/task/share")
    suspend fun taskShare(
    ): BaseApiModel


    /**
     *
     */
    @GET("api/videoList")
    suspend fun videoList(
    ): ApiResponse<String>

    //请求公告
    @GET("api/notice/announcement")
    suspend fun announcement():ApiResponse<List<AnnouncementBean>>

}