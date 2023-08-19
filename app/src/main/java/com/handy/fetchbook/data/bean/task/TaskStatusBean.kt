package com.handy.fetchbook.data.bean.task

import com.google.gson.annotations.SerializedName

data class TaskStatusBean(
    @SerializedName("share") var share: Int? = null,
    @SerializedName("secret") var secret: Int? = null,
    @SerializedName("shareTotal") var shareTotal: Int? = null,
    @SerializedName("secretTotal") var secretTotal: Int? = null,
    @SerializedName("day") var day: Int? = null
)
