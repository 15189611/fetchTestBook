package com.handy.fetchbook.data.bean.expo

import com.google.gson.annotations.SerializedName

data class ExpoDetailsBean(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("logo") var logo: String? = null,
    @SerializedName("region") var region: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("banner") var banner: List<String> = arrayListOf(),
    @SerializedName("rating") var rating: String? = null,
    @SerializedName("expo_comments") var expoComments: List<ExpoComments> = arrayListOf()

)

data class ExpoComments(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("rating") var rating: Int? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("expo_id") var expoId: Int? = null,
    @SerializedName("is_show") var isShow: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

)