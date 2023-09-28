package com.handy.fetchbook.app.ext

import com.blankj.utilcode.util.StringUtils.getString
import com.handy.fetchbook.R
import com.hjq.toast.ToastUtils

/**
 * 打开tips框
 */
fun showTips(message: String = getString(R.string.loading_text)) {

    ToastUtils.setView(R.layout.toast_custom_view)
    ToastUtils.show(message)
}