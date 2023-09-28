package com.handy.fetchbook.view

import android.content.Context
import android.util.AttributeSet
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.AbsModuleView
import com.handy.fetchbook.data.bean.MemberUpHistoryItemBean
import kotlinx.android.synthetic.main.member_up_history_activity_item.view.tv_bao
import kotlinx.android.synthetic.main.member_up_history_activity_item.view.tv_date
import kotlinx.android.synthetic.main.member_up_history_activity_item.view.tv_process
import kotlinx.android.synthetic.main.member_up_history_activity_item.view.tv_pttime
import kotlinx.android.synthetic.main.member_up_history_activity_item.view.tv_state
import kotlinx.android.synthetic.main.member_up_history_activity_item.view.tv_title

class MemberUpHistoryItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbsModuleView<MemberUpHistoryItemBean>(context, attrs) {

    override fun getLayoutId(): Int {
        return R.layout.member_up_history_activity_item
    }

    override fun onChanged(model: MemberUpHistoryItemBean) {
        super.onChanged(model)
//        dateContent.text = model.created_at.orEmpty()
//        balance.text = model.amount.orEmpty()
        tv_title.text = model.tour_packages.name.orEmpty();
        tv_date.text = model.created_at.orEmpty();
        tv_bao.text = model.tour_packages.price.toString();
        tv_process.text = model.tour_packages.progress.toString();
        tv_pttime.text = model.tour_packages.start_date.orEmpty() + "-" + model.tour_packages.end_date.orEmpty();
        tv_state.text = model.status_desc.orEmpty();
    }

}