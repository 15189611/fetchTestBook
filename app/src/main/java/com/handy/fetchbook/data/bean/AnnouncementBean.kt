package com.handy.fetchbook.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * - Author: Charles
 * - Date: 2023/8/23
 * - Description:
 */
@Parcelize
data class AnnouncementBean(
    val uuid: Int? = null,
    val title: String? = null,
    val content: String? = null,
    val read_status: Int? = null,
    val created_at: String? = null,
) : Parcelable