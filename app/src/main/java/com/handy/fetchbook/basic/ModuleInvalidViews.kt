package com.handy.fetchbook.basic

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import androidx.appcompat.widget.AppCompatTextView
import com.handy.fetchbook.basic.ext.dp

class ModuleInvalidReleaseModel

class ModuleInvalidDebugModel

interface IModuleInvalidView


class ModuleInvalidReleaseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs), IModuleView<ModuleInvalidReleaseModel>, IModuleInvalidView {

    override fun update(model: ModuleInvalidReleaseModel) {

    }
}

class ModuleInvalidDebugView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbsModuleView<ModuleInvalidDebugModel>(context, attrs), IModuleInvalidView {

    val textView = AppCompatTextView(context)
    init {
        setBackgroundColor(Color.RED)
        textView.text = "adapter的数据有问题，adapter中有null，或者没有notifyDataSetChange，或者对应数据为注册，log可查看具体原因"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
        textView.setTextColor(Color.WHITE)
        addView(textView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER))
        minimumHeight = 40.dp()
        minimumWidth = 40.dp()

    }

}
