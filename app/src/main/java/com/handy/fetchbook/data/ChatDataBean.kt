package com.handy.fetchbook.data

import com.google.gson.annotations.SerializedName
import com.handy.fetchbook.data.bean.expo.ExpoComments

data class ChatDataBean(var data: String,var isLoading: Boolean,var isMySelf: Boolean)
//data class ChatDataBean(var data: String,var code: Int,var msg: String,var timeStamp: Long,var isMySelf: Boolean)
