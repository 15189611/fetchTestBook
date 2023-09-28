package com.handy.fetchbook.data.bean.home

import com.google.gson.annotations.SerializedName

data class ScenicsDetailsBean (

    @SerializedName("id"          ) var id          : Int?    = null,
    @SerializedName("locale"      ) var locale      : String? = null,
    @SerializedName("uuid"        ) var uuid        : String? = null,
    @SerializedName("title"       ) var title       : String? = null,
    @SerializedName("thumbnail"   ) var thumbnail   : String? = null,
    @SerializedName("background"  ) var background  : String? = null,
    @SerializedName("content"     ) var content     : String? = null,
    @SerializedName("must"        ) var must        : Int?    = null,
    @SerializedName("plan"        ) var plan        : Int?    = null,
    @SerializedName("is_show"     ) var isShow      : Int?    = null,
    @SerializedName("created_at"  ) var createdAt   : String? = null,
    @SerializedName("updated_at"  ) var updatedAt   : String? = null,
    @SerializedName("province"    ) var province    : String? = null,
    @SerializedName("is_china"    ) var isChina     : Int?    = null,
    @SerializedName("description" ) var description : String? = null

)

