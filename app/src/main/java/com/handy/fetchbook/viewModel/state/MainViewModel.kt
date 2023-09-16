package com.handy.fetchbook.viewModel.state

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.otherApiService
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.LanguageBean
import com.handy.fetchbook.fragment.DrawFragment
import com.handy.fetchbook.fragment.HomeFragmentV2
import com.handy.fetchbook.fragment.MeFragment
import com.handy.fetchbook.fragment.TaskFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.requestNoCheck

class MainViewModel : BaseViewModel() {

    /**
     * 子页面集合
     */
    val fragments: List<Fragment> = listOf(
        HomeFragmentV2(),
        DrawFragment(),
        TaskFragment(),
        MeFragment(),
    )

    val languageContentResult = MutableLiveData<LanguageBean?>()

    fun getLanguageContent() {
        val language = getLanguage()
        val url = "_latest/${language}"
        BooKLogger.d("语言名称 = $language -> url = $url")
        requestNoCheck({ otherApiService.getLanguageContent(path = url) }, success = {
            languageContentResult.value = it
        }, error = {
            languageContentResult.value = null
        })
    }
    //获取语言要的参数
    private fun getLanguage(): String {
        return when (CacheUtil.getLanguage()) {
            "enUs" -> "en"
            "zhCn" -> "zh-cn"
            "zhTW" -> "zh-tw"
            "koKR" -> "kr"
            else -> "zh-cn"
        }
    }
}