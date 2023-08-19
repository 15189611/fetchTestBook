package com.handy.fetchbook.data.bean.home

import com.google.gson.annotations.SerializedName

data class NoticeBean(
    @SerializedName("items") var items: List<NoticeItems> = arrayListOf(),
    @SerializedName("total") var total: Int? = null
)

data class NoticeItems(
    @SerializedName("uuid"        ) var uuid       : Int?    = null,
    @SerializedName("title"       ) var title      : String? = null,
    @SerializedName("content"     ) var content    : String? = null,
    @SerializedName("read_status" ) var readStatus : Int?    = null,
    @SerializedName("created_at"  ) var createdAt  : String? = null
)