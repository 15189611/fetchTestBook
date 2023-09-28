package com.handy.fetchbook.data.bean.home

import com.google.gson.annotations.SerializedName

data class SocialMediaBean (
    @SerializedName("name"      ) var name      : String? = null,
    @SerializedName("en_name"   ) var enName    : String? = null,
    @SerializedName("url"       ) var url       : String? = null,
    @SerializedName("thumbnail" ) var thumbnail : String? = null,
    @SerializedName("is_show"   ) var isShow    : Int?    = null,
    @SerializedName("is_button" ) var isButton  : Int?    = null,
    @SerializedName("qr_code"   ) var qrCode    : String? = null
)