package com.handy.fetchbook.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MemberItemDetailBean(
    var uid: String? = null,
    var name: String? = null,
    var description: String? = null,
    var end_date: String? = null,
    var image: String? = null,
    var max_progress: Int = 0,
    var price: Int = 0,
    var progress: Int = 0,
    var province: String? = null,
    var start_date: String? = null,
) : Parcelable