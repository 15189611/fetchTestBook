package com.handy.fetchbook.app.network

import com.handy.fetchbook.data.bean.*
import com.handy.fetchbook.data.bean.expo.*
import com.handy.fetchbook.data.bean.model.ApiResponse
import com.handy.fetchbook.data.bean.group.GroupSharingBean
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
     * api/expo/search?keyword=123&page=1
     * expo列表头部搜索接口
     */
    @GET("api/expo/search")
    suspend fun expoSearchList(
        @Query("keyword") keyword: String?,
        @Query("page") page: Int?
    ): ApiResponse<Data>

    /**
     * 获取expo头部banner
     */
    @GET("api/expo/expoBanner")
    suspend fun getExpoBanner(): ApiResponse<Any>

    /**
     * 世博列表
     *
     */
    @GET("api/expo/details/{id}")
    suspend fun details(@Path("id") id: Int): ApiResponse<ExpoDetailsBean>

    /**
     * 反馈接口
     */
    @FormUrlEncoded
    @POST("api/expo/commentExpo")
    suspend fun commentExpo(
        @Field("expo_id") expo_id: String,
        @Field("comment") comment: String,
        @Field("rating") rating: Int
    ): ApiResponse<Any>

    /**
     *
     */
    @GET("api/home/socialMedia")
    suspend fun socialMedia(
    ): ApiResponse<List<SocialMediaBean>>

    /**
     * 获取系统消息(type=0),个人信息(type=1)
     */
    @GET("api/notice/message")
    suspend fun message(
        @Query("type") region: Int?,
        @Query("page") page: Int?
    ): ApiResponse<SystemInfoBean>

    /**
     * 消息点击读取状态
     */
    @GET("api/notice/messageRead")
    suspend fun messageRead(@Query("uuid") uuid: String): BaseApiModel

    /**
     * 拼团列表
     */
    @GET("api/tour/list")
    suspend fun grouplist(
        @Query("page") page: Int?
    ): ApiResponse<GroupSharingBean>

    /**
     * 公告消息
     */
    @GET("api/notice/announcements")
    suspend fun announcements(
        @Query("page") page: Int?
    ): ApiResponse<NoticeBean>

    /**
     * 公告消息阅读
     */
    @GET("api/notice/read")
    suspend fun noticeRead(@Query("uuid") uuid: String): BaseApiModel

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
     * 分享成功任务
     */
    @POST("api/task/share")
    suspend fun taskShare(
    ): BaseApiModel

    /**
     * 视频播放完成任务
     */
    @POST("api/task/secret")
    suspend fun taskVideo(
    ): BaseApiModel

    /**
     * 抽奖的winners
     */
    @GET("api/prize/winners")
    suspend fun getWinners(): ApiResponse<List<String>>

    /**
     * 抽奖的prize/list2列表
     */
    @GET("api/prize/list2")
    suspend fun getPrizeList(): ApiResponse<List<DrawPrizeItemBean>>

    /**
     * 抽奖打开红包
     */
    @POST("api/prize/openRedPacket")
    suspend fun openRedPacket(@Body body: PostJsonBody): ApiResponse<DrawOpenRedPacketBean>

    /**
     * 获取视频任务链接
     */
    @GET("api/videoList")
    suspend fun videoList(
    ): ApiResponse<String>

    //请求公告
    @GET("api/notice/announcement")
    suspend fun announcement(): ApiResponse<List<AnnouncementBean>>

    //编辑个人信息接口
    @GET("api/user/verified")
    suspend fun getUserVerified(): ApiResponse<EditUserInfoBean>

    //编辑资料保存接口
    @POST("api/user/updateProfile")
    suspend fun updateProfile(@Body body: PostJsonBody): ApiResponse<Any>

    //上传头像
    @POST("api/user/avatar")
    suspend fun updateAvatar(@Body body: PostJsonBody): ApiResponse<Any>

    //兑换旅游宝
    @POST("api/investment/invest")
    suspend fun invest(@Body body: PostJsonBody): ApiResponse<Any>

    //我的钱包兑换记录
    @GET("api/wallet/details")
    suspend fun myBuyHistory(
        @Query("start_date") start_date: String?,
        @Query("end_date") end_date: String?,
        @Query("balance_type") balance_type: String?,
        @Query("page") page: Int?,
        @Query("act_type") act_type: String? = null
    ): ApiResponse<MyBuyHistoryBean>
    @GET("api/tour/details")
    suspend fun memberItemDetail(
        @Query("package_uid") package_uid: String? = null
    ): ApiResponse<MemberItemDetailBean>
    @GET("api/tour/userList")
    suspend fun memberUpHistory(
        @Query("start_date") start_date: String?,
        @Query("end_date") end_date: String?,
        @Query("balance_type") balance_type: String?,
        @Query("page") page: Int?,
        @Query("act_type") act_type: String? = null
    ): ApiResponse<MemberUpHistoryBean>
}