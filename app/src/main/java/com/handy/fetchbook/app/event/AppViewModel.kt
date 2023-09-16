package com.handy.fetchbook.app.event

import com.handy.fetchbook.data.bean.LanguageBean
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class AppViewModel : BaseViewModel() {
    private var languageBean: LanguageBean? = null

    fun getLanguageContent(): LanguageBean? {
        return languageBean
    }

    fun setLanguageContent(content: LanguageBean?) {
        this.languageBean = content
    }
}