package com.handy.fetchbook.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MemberUpHistoryBean(
    val items: List<MemberUpHistoryItemBean>? = null,
    val total: Int = 0
) : Parcelable

@Parcelize
data class MemberUpHistoryItemBean(
    var status_desc: String? = null,
    var amount: Double = 0.0,
    var created_at: String ? =null,
    var package_id: Int = 0,
    var status: Int,
    var uid: String? = null,
    var tour_packages: MemberUpHistoryTourPackagesBean
) : Parcelable

@Parcelize
class MemberUpHistoryTourPackagesBean(
    var id: Int = 0,
    val uid: String? = null,
    var name: String? = "",
    var status: Int = 0,
    var image: String? = "",
    var price: Double = 0.0,
    var start_date: String?= null,
    var progress: Int = 0,
    var end_date: String?= null,
    var province: String?= null,
    var max_progress: Int = 0,
) : Parcelable


