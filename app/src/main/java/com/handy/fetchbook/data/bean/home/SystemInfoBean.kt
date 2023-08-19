package com.handy.fetchbook.data.bean.home

import com.google.gson.annotations.SerializedName

data class SystemInfoBean(
    @SerializedName("items") var items: List<SystemInfoItems> = arrayListOf(),
    @SerializedName("total") var total: Int? = null
)

data class SystemInfoItems(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("type") var type: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null

)