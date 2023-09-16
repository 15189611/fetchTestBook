package com.handy.fetchbook.view

import android.content.Context
import android.util.AttributeSet
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.AbsModuleView

/**
 * - Author: Charles
 * - Date: 2023/9/10
 * - Description:
 */
class ExpoBannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbsModuleView<ExpoBannerModel>(context, attrs) {

    override fun getLayoutId(): Int {
        return R.layout.item_view_expo_banner_view
    }

    override fun update(model: ExpoBannerModel) {
        super.update(model)
    }
}

data class ExpoBannerModel(
    val url: String? = null
)