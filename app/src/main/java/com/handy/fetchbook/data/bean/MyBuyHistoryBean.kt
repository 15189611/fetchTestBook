package com.handy.fetchbook.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * - Author: Charles
 * - Date: 2023/9/2
 * - Description:
 */
@Parcelize
data class MyBuyHistoryBean(
    val items: List<MyBuyHistoryItemBean>? = null,
    val total: Int = 0
) : Parcelable

@Parcelize
data class MyBuyHistoryItemBean(
    val user_id: String? = null,
    val balance_type: String? = null,
    val balance_desc: String? = null,
    val act_type: String? = null,
    val note: String? = null,
    val act_desc: String? = null,
    val amount: String? = null,
    val process_at: String? = null,
    val created_at: String? = null
) : Parcelable