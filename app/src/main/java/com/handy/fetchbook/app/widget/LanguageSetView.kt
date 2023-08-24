package com.handy.fetchbook.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.handy.fetchbook.R
import com.handy.fetchbook.app.ext.getLanguage
import com.handy.fetchbook.data.bean.model.LanguageModel
import com.lxj.xpopup.impl.FullScreenPopupView
import kotlinx.android.synthetic.main.popup_language.view.*

/**
 * Description: 语言设置弹窗
 * Create by Summer, at 2022/2/21
 */
class LanguageSetView(context: Context) : FullScreenPopupView(context) {
    var confirm: OnPopupViewConfirm? = null
    var cancel: OnPopupViewCancel? = null

    override fun getImplLayoutId(): Int {
        return R.layout.popup_language
    }

    private var currentList: MutableList<LanguageModel>? = null

    var pos = -1

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate() {
        super.onCreate()
        currentList = getData()
        rv_language.linear().setup {
            addType<LanguageModel>(R.layout.item_language)
            onBind {
                findView<TextView>(R.id.tv_text).text = getModel<LanguageModel>().language
                findView<View>(R.id.splitFirst).isInvisible = !getModel<LanguageModel>().isSelect
                findView<View>(R.id.splitSecond).isInvisible = !getModel<LanguageModel>().isSelect
            }
            R.id.ll_temp.onClick {
                currentList?.forEach { it.isSelect = false }
                getModel<LanguageModel>().isSelect = true
                pos = layoutPosition
                notifyDataSetChanged()
            }

        }.models = currentList
        atvCancel.setOnClickListener {
            pos = -1
            cancel?.invoke()
            dismiss()
        }
        atvOK.setOnClickListener {
            confirm?.invoke()
        }
    }

    private fun getData(): MutableList<LanguageModel> {
        val language = getLanguage()
        return mutableListOf(
            LanguageModel("English", language == "en"),
            LanguageModel("简体中文", language == "zh_CN"),
            LanguageModel("繁體中文", (language == "zhHK" || language == "zhTW")),
            LanguageModel("한국어", language == "ko"),
        )
    }

    fun getViewSelectLanguage(): String {
        return if (pos != -1) {
            getData()[pos].language
        } else {
            ""
        }
    }
}

typealias OnPopupViewConfirm = () -> Unit
typealias OnPopupViewCancel = () -> Unit