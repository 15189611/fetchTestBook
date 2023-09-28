package com.handy.fetchbook.data.bean.me

import com.google.gson.annotations.SerializedName

data class UserInfoBean(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("nickname") var nickname: String? = null,
    @SerializedName("account") var account: String? = null,
    @SerializedName("level_desc") var levelDesc: String? = null,
    @SerializedName("verify") var verify: Int? = null,
    @SerializedName("notification") var notification: Notification? = Notification(),
    @SerializedName("birthday") var birthday: String? = null,
    @SerializedName("membership_at") var membershipAt: String? = null,
    @SerializedName("redeemable_amount") var redeemableAmount: String? = null,
    @SerializedName("lucky_ticket") var luckyTicket: String? = null,
    @SerializedName("lucky_bag") var luckyBag: Int? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("invest") var invest: String? = null,
    @SerializedName("type") var type: Int? = null,
    @SerializedName("type_desc") var typeDesc: String? = null,
    @SerializedName("level") var level: Int? = null,
    @SerializedName("wheel_level") var wheel_level: Int? = null,
    @SerializedName("last_at") var lastAt: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("referral") var referral: String? = null,

)

data class Notification(
    @SerializedName("share") var share: Int? = 0,
    @SerializedName("secret") var secret: Int? = null,
    @SerializedName("shareTotal") var shareTotal: Int? = null,
    @SerializedName("secretTotal") var secretTotal: Int? = null

)