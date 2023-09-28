package com.handy.fetchbook.data.bean.home

import com.google.gson.annotations.SerializedName

data class ScenicsBean (

    @SerializedName("items" ) var items : List<Items> = arrayListOf(),
    @SerializedName("total" ) var total : Int?             = null

)

data class Items(

    @SerializedName("uuid") var uuid: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("province") var province: String? = null,
    @SerializedName("must") var must: Int? = null,
    @SerializedName("plan") var plan: Int? = null

)


