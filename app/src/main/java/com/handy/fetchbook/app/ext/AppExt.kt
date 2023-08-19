package com.handy.fetchbook.app.ext

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LanguageUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.app.widget.LanguageSetView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import java.util.*


/**
 * 设置语言选择弹窗
 */
fun languageSet(layoutLanguage: View) {
    val languageSetView = LanguageSetView(ActivityUtils.getTopActivity())
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
                when (languageSetView.getLanguage()) {
                    "English" -> {
                        CacheUtil.setLanguage("enUs")
                        LanguageUtils.applyLanguage(Locale.ENGLISH)
                    }
                    "简体中文" -> {
                        CacheUtil.setLanguage("zhCn")
                        LanguageUtils.applyLanguage(Locale.CHINESE)
                    }
                    "繁體中文" -> {
                        CacheUtil.setLanguage("zhHK")
                        LanguageUtils.applyLanguage(Locale.TRADITIONAL_CHINESE)
                    }

                    "한국어" -> {
                        CacheUtil.setLanguage("koKR")
                        LanguageUtils.applyLanguage(Locale.KOREA)
                    }
                }
                for (activity in ActivityUtils.getActivityList()) {
                    activity.recreate()
                }
            }
        })
        .asCustom(languageSetView)
        .show()
}

/**
 * 设置语言选择弹窗
 */
fun lanSet(tv_lan: TextView) {
    when (CacheUtil.getLanguage()) {
        "enUs" -> {
            tv_lan.text = "English"
        }
        "zhHK", "zhTW" -> {
            tv_lan.text = "繁體中文"
        }
        "zhCn" -> {
            tv_lan.text = "简体中文"
        }

        "koKR" -> {
            tv_lan.text = "한국어"
        }
    }
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