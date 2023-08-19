package com.handy.fetchbook.basic

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.handy.fetchbook.basic.ext.pxToDp

class NoMoreTipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), IModuleView<ModuleNoMoreTipModel> {

    init {
        val padding = context.pxToDp(20)
        setPadding(padding, padding, padding, padding)
        gravity = Gravity.CENTER
        text = "没有更多了"
        setTextColor(ContextCompat.getColor(context, Color.parseColor("#14151A")))
    }

    override fun update(model: ModuleNoMoreTipModel) {
    }
}
