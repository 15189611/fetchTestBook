package com.handy.fetchbook.data.bean.me

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class HelpCenterBean(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("locale") var locale: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null,
    @SerializedName("video") var video: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("sort") var sort: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

) : Serializable