package com.handy.fetchbook.data.bean.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 描述　: 账户信息
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class TokenInfoModel(
    var token: String = "",
) : Parcelable
