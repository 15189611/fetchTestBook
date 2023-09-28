package com.handy.fetchbook.data.bean.expo

import com.google.gson.annotations.SerializedName

data class ExpoListBean (

    @SerializedName("data"   ) var data   : Data?             = Data(),
    @SerializedName("region" ) var region : ArrayList<String> = arrayListOf()

)

data class Items (

    @SerializedName("id"     ) var id     : Int?    = null,
    @SerializedName("title"  ) var title  : String? = null,
    @SerializedName("logo"   ) var logo   : String? = null,
    @SerializedName("rating" ) var rating : String? = null,
    @SerializedName("region" ) var region : String? = null

)

data class Data (

    @SerializedName("items" ) var items : List<Items> = arrayListOf(),
    @SerializedName("total" ) var total : Int?             = null

)
