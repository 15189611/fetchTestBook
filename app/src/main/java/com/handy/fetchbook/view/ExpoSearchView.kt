package com.handy.fetchbook.view

import android.content.Context
import android.util.AttributeSet
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.AbsModuleView
import kotlinx.android.synthetic.main.item_view_expo_search_view.view.aetK
import kotlinx.android.synthetic.main.item_view_expo_search_view.view.atvSearch

/**
 * - Author: Charles
 * - Date: 2023/9/10
 * - Description:
 */
class ExpoSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    var click: ((String) -> Unit)? = null
) : AbsModuleView<ExpoSearchModel>(context, attrs) {

    override fun getLayoutId(): Int {
        return R.layout.item_view_expo_search_view
    }

    override fun update(model: ExpoSearchModel) {
        super.update(model)
        atvSearch.setOnClickListener {
            click?.invoke(aetK.text.toString())
        }
    }
}

data class ExpoSearchModel(
    val title: String? = null
)