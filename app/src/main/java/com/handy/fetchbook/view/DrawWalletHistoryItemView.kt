package com.handy.fetchbook.view

import android.content.Context
import android.util.AttributeSet
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.AbsModuleView
import com.handy.fetchbook.data.bean.MyBuyHistoryItemBean
import kotlinx.android.synthetic.main.item_draw_history_item_view.view.*

/**
 * - Author: Charles
 * - Date: 2023/9/2
 * - Description:
 */
class DrawWalletHistoryItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbsModuleView<MyBuyHistoryItemBean>(context, attrs) {

    override fun getLayoutId(): Int {
        return R.layout.item_draw_history_item_view
    }

    override fun onChanged(model: MyBuyHistoryItemBean) {
        super.onChanged(model)
        dateContent.text = model.created_at.orEmpty()
        balance.text = model.amount.orEmpty()
        drawMoneyContent.text = model.note.orEmpty()
    }

}