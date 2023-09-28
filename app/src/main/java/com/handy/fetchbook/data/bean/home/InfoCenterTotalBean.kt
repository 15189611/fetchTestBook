package com.handy.fetchbook.data.bean.home

import com.google.gson.annotations.SerializedName

data class InfoCenterTotalBean (

    @SerializedName("message"        ) var message        : Int?    = null,
    @SerializedName("system"        ) var system        : Int?    = null,
    @SerializedName("announcement"     ) var announcement      : Int?    = null

)

