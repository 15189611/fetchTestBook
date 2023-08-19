package com.handy.fetchbook.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.model.LanguageModel
import com.lxj.xpopup.impl.FullScreenPopupView
import kotlinx.android.synthetic.main.popup_language.view.*

/**
 * Description: 语言设置弹窗
 * Create by Summer, at 2022/2/21
 */
class LanguageSetView(context: Context) : FullScreenPopupView(context) {

    override fun getImplLayoutId(): Int {
        return R.layout.popup_language
    }

    var pos = -1

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate() {
        super.onCreate()
        rv_language.linear().setup {
            addType<LanguageModel>(R.layout.item_language)
            onBind {
                findView<TextView>(R.id.tv_text).text = getModel<LanguageModel>().language
            }
            R.id.ll_temp.onClick {
                pos = layoutPosition
                dismiss()
            }

        }.models = getData()
        atvCancel.setOnClickListener {
            pos = -1
            dismiss()
        }
        atvOK.setOnClickListener {
            dismiss()
        }
    }

    private fun getData(): MutableList<LanguageModel> {
        return mutableListOf(
            LanguageModel("English"),
            LanguageModel("简体中文"),
            LanguageModel("繁體中文"),
            LanguageModel("한국어"),
        )
    }

    fun getLanguage(): String {
        return if (pos != -1) {
            getData()[pos].language
        } else {
            ""
        }
    }
}