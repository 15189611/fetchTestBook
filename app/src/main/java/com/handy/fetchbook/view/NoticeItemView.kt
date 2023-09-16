package com.handy.fetchbook.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.handy.fetchbook.R
import com.handy.fetchbook.app.App
import com.handy.fetchbook.basic.AbsModuleView
import com.handy.fetchbook.basic.viewLayoutPosition
import com.handy.fetchbook.data.bean.home.NoticeItems
import com.handy.fetchbook.data.bean.home.SystemInfoItems
import kotlinx.android.synthetic.main.item_notice.view.*

/**
 * - Author: Charles
 * - Date: 2023/9/10
 * - Description:
 */
class NoticeItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    var click: ((Int, NoticeItems) -> Unit)? = null
) : AbsModuleView<NoticeItems>(context, attrs) {

    override fun getLayoutId(): Int {
        return R.layout.item_notice
    }

    @SuppressLint("SetTextI18n")
    override fun update(model: NoticeItems) {
        super.update(model)
        val timeTip = App.appViewModelInstance.getLanguageContent()?.`_$PageCoinBagHistoryCreatedTime4` ?: "日期"
        val readContent = App.appViewModelInstance.getLanguageContent()?.`_$AppMessageRead80` ?: "已读"
        val notRead = App.appViewModelInstance.getLanguageContent()?.`_$AppMessageNotRead214` ?: "未读"
        atvTitle.text = model.title.orEmpty()
        atvTime.text = timeTip + " : " + model.createdAt.orEmpty()
        atvUnread.text = if (model.readStatus == 0) notRead else readContent
        noticeViewParent.setOnClickListener {
            if (model.readStatus == 1) return@setOnClickListener
            click?.invoke(viewLayoutPosition, model)
        }
    }
}