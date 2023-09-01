package com.handy.fetchbook.data.bean.group

import com.google.gson.annotations.SerializedName

data  class GroupSharingBean (

    @SerializedName("items" ) var items : List<Items> = arrayListOf(),
        )
data class Items (

    @SerializedName("uid") var uid: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("start_date") var start_date: String? = null,
    @SerializedName("progress") var rating: Int? = null,
    @SerializedName("max_progress") var max_progress: Int? = null

)

