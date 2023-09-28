package com.handy.fetchbook.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.ImageView
import coil.load
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.MemberItemMoreDetailActivity
import com.handy.fetchbook.basic.AbsModuleView
import com.handy.fetchbook.data.bean.group.Items
import kotlinx.android.synthetic.main.item_group_buy_view.view.*

/**
 * - Author: Charles
 * - Date: 2023/9/27
 * - Description:
 */
class GroupBuyItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbsModuleView<Items>(context, attrs) {

    override fun getLayoutId(): Int {
        return R.layout.item_group_buy_view
    }

    override fun onChanged(model: Items) {
        super.onChanged(model)
        (groupBuyImage as ImageView).load(model.image)
        name.text = model.name
        name1.text = model.price + " 旅游宝"
        memberUpItemMore.setOnClickListener {
            val intent = Intent(context, MemberItemMoreDetailActivity::class.java);
            intent.putExtra("id", model.uid)
            intent.putExtra("price", model.price)
            context.startActivity(intent)
        }
    }
}