package com.handy.fetchbook.data.bean.home

import com.google.gson.annotations.SerializedName

data class SearchBean(
    @SerializedName("uuid") var uuid: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null,
    @SerializedName("province") var province: String? = null
)
