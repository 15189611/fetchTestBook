package com.handy.fetchbook.app.ext

import android.annotation.SuppressLint
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.StringUtils.getString
import com.handy.fetchbook.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView

/**
 * loading框
 */
@SuppressLint("StaticFieldLeak")
private var loadingDialog: LoadingPopupView? = null

/**
 * 打开等待框
 */
fun showLoadingExt(message: String = getString(R.string.loading_text)) {
    if (loadingDialog == null) {
        loadingDialog = XPopup.Builder(ActivityUtils.getTopActivity())
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(false)
            .isLightNavigationBar(true)
            .isViewMode(true)
            .asLoading(message) as LoadingPopupView
    }
    loadingDialog?.show()
}

/**
 * 关闭等待框
 */
fun dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}
