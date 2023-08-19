package com.handy.fetchbook.basic

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePaddingRelative

class ModuleDividerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsModuleView<ModuleDividerModel>(context, attrs, defStyleAttr) {

    private val dividerView = View(context)

    init {
        dividerView.setBackgroundColor(ContextCompat.getColor(context, Color.parseColor("#f5f5f9")))
        addView(dividerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun onChanged(model: ModuleDividerModel) {
        super.onChanged(model)
        updateLayoutParams<ViewGroup.LayoutParams> {
            height = model.height
        }
        updatePaddingRelative(start = model.start, end = model.end)
    }
}

/**
 * 灰色分割块
 */
class MallSpaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsModuleView<ModuleSpaceModel>(context, attrs, defStyleAttr) {

    private val dividerView = View(context)

    init {
        dividerView.setBackgroundColor(ContextCompat.getColor(context, Color.parseColor("f5f5f9")))
        addView(dividerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun onChanged(model: ModuleSpaceModel) {
        super.onChanged(model)
        updateLayoutParams<ViewGroup.LayoutParams> {
            height = model.height
        }
        updatePaddingRelative(start = model.start, end = model.end)
    }
}

/**
 * 分割块，自定义高度和颜色
 */
class ModuleSeparatorBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsModuleMView<ModuleSeparatorBarModel>(context, attrs, defStyleAttr) {

    override fun onChanged(model: ModuleSeparatorBarModel) {
        super.onChanged(model)
        updateLayoutParams<ViewGroup.LayoutParams> {
            height = model.height
        }
        setBackgroundColor(model.color)
    }
}

/**
 * 空白分割块
 */
class MallEmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsModuleMView<ModuleEmptyModel>(context, attrs, defStyleAttr) {

    override fun onChanged(model: ModuleEmptyModel) {
        super.onChanged(model)
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            height = model.height
        }
    }
}

class ModuleGroupSectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsModuleMView<ModuleGroupSectionModel>(context, attrs, defStyleAttr)
