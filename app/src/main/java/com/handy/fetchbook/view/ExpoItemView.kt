package com.handy.fetchbook.view

import android.content.Context
import android.util.AttributeSet
import coil.load
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.AbsModuleView
import com.handy.fetchbook.basic.viewLayoutPosition
import com.handy.fetchbook.data.bean.expo.Items
import kotlinx.android.synthetic.main.expo_item.view.*

/**
 * - Author: Charles
 * - Date: 2023/9/10
 * - Description:
 */
class ExpoItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    var click: ((Int, Items) -> Unit)? = null
) : AbsModuleView<Items>(context, attrs) {

    override fun getLayoutId(): Int {
        return R.layout.expo_item
    }

    override fun update(model: Items) {
        super.update(model)
        aivImg.load(model.logo.orEmpty())
        name.text = model.title.orEmpty()
        atvRating.text = model.rating.orEmpty()
        arbRating.rating = model.rating?.toFloat() ?: 0.0F
        expoItemViewParent.setOnClickListener {
            click?.invoke(viewLayoutPosition, model)
        }
    }
}