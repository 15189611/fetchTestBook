package com.handy.fetchbook.app.ext

import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LanguageUtils
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.app.widget.LanguageSetView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import java.util.Locale

/**
 * 设置语言选择弹窗
 */
fun languageSet(activity: FragmentActivity, layoutLanguage: View) {
    val languageSetView = LanguageSetView(ActivityUtils.getTopActivity())
    languageSetView.confirm = {
        when (it.language) {
            "English" -> {
                CacheUtil.setLanguage("enUs")
                LanguageUtils.applyLanguage(Locale.ENGLISH, true)
            }

            "简体中文" -> {
                CacheUtil.setLanguage("zhCn")
                LanguageUtils.applyLanguage(Locale.CHINESE, true)
            }

            "繁體中文" -> {
                CacheUtil.setLanguage("zhHK")
                LanguageUtils.applyLanguage(Locale.TRADITIONAL_CHINESE, true)
            }

            "한국어" -> {
                CacheUtil.setLanguage("koKR")
                LanguageUtils.applyLanguage(Locale.KOREA, true)
            }
        }
    }

    languageSetView.cancel = {}
    XPopup.Builder(ActivityUtils.getTopActivity())
        .atView(layoutLanguage)
        .hasShadowBg(false)
        .setPopupCallback(object : SimpleCallback() {
            override fun beforeShow(popupView: BasePopupView?) {
                super.beforeShow(popupView)
            }

            override fun beforeDismiss(popupView: BasePopupView) {
                super.onDismiss(popupView)
            }

            override fun onDismiss(popupView: BasePopupView?) {
                super.onDismiss(popupView)
            }
        })
        .asCustom(languageSetView)
        .show()
}

/**
 * 获取语言
 */
fun getLanguage(): String {
    return when (CacheUtil.getLanguage()) {
        "enUs" -> "en"
        "zhCn" -> "zh_CN"
        "zhTW" -> "zh_TW"
        "zhHK" -> "zh_TW"
        "koKR" -> "ko"
        else -> "zh_CN"
    }
}