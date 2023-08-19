package com.handy.fetchbook.data.bean.home

import com.google.gson.annotations.SerializedName

data class BannerBean(

    @SerializedName("banner") var banner: List<Banner> = arrayListOf(),
    @SerializedName("announcement") var announcement: List<Announcement> = arrayListOf()

)


data class Banner(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("locale") var locale: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("type") var type: Int? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("sort") var sort: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

)

data class Announcement(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("locale") var locale: String? = null,
    @SerializedName("type") var type: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

)