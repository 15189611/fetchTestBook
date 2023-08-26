package com.handy.fetchbook.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * - Author: Charles
 * - Date: 2023/8/25
 * - Description:
 */
@Parcelize
data class EditUserInfoBean(
    val country: String? = null,
    val type: Int = 0,
    val first_name: String? = null,
    val last_name: String? = null,
    val id_number: String? = null,
    val image: String? = null,
    val image2: String? = null,
    val status: Int = 0,
    val reason: String? = null
) : Parcelable