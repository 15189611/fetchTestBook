package com.handy.fetchbook.basic

import android.content.Context
import android.util.AttributeSet
import android.view.View

open class AbsModuleMView<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IModuleView<T> {

    protected var data: T? = null

    override fun update(model: T) {
        val isChanged = data != model
        data = model
        if (isChanged) {
            onChanged(model)
        }
    }

    open fun onChanged(model: T) {}
}
