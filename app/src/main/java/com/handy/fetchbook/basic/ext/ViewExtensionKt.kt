package com.handy.fetchbook.basic.ext

import android.content.ContextWrapper
import android.graphics.Outline
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
typealias AfterTextChange = (s: Editable?) -> Unit

typealias BeforeTextChanged = (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit
typealias OnTextChanged = (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit

fun View.getString(@StringRes messageRes: Int): String {
    return context.getString(messageRes)
}

fun View.formatString(@StringRes messageRes: Int, vararg args: Any?): String {
    return context.formatString(messageRes, *args)
}

fun View.appCompatActivity(): AppCompatActivity {
    return checkNotNull(safeAppCompatActivity())
}

fun View.safeAppCompatActivity(): AppCompatActivity? {
    var context = context
    if (context is AppCompatActivity) {
        return context
    } else {
        while (context is ContextWrapper) {
            if (context is AppCompatActivity) {
                return context
            }
            context = context.baseContext
        }
    }
    return null
}

fun EditText.getContent(): String {
    return this.text.toString()
}

fun EditText.getContentWithTrim(): String {
    return getContent().trim()
}

fun EditText.addListener(
    afterTextChanged: AfterTextChange = {},
    beforeTextChanged: BeforeTextChanged = { _, _, _, _ -> },
    onTextChanged: OnTextChanged = { _, _, _, _ -> }
): TextWatcher {
    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }
    }
    addTextChangedListener(listener)
    return listener
}

fun EditText.doOnTextChanged(
    onTextChanged: OnTextChanged
) {
    addListener(onTextChanged = onTextChanged)
}

fun EditText.doBeforeTextChanged(
    beforeTextChanged: BeforeTextChanged
) {
    addListener(beforeTextChanged = beforeTextChanged)
}

fun EditText.doAfterTextChanged(
    afterTextChanged: AfterTextChange
) {
    addListener(afterTextChanged = afterTextChanged)
}

fun ViewGroup.find(action: (View) -> Boolean): View? {
    val childCount = this.childCount
    if (childCount == 0) {
        return null
    }
    for (index in 0 until childCount) {
        val child = this.getChildAt(index)
        if (action(child)) {
            return child
        }
    }
    return null
}

fun ViewGroup.find(action: (View, index: Int) -> Boolean): View? {
    val childCount = this.childCount
    if (childCount == 0) {
        return null
    }
    for (index in 0 until childCount) {
        val child = this.getChildAt(index)
        if (action(child, index)) {
            return child
        }
    }
    return null
}

fun View.disableOutlineProvider() {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline?.setOval(0, 0, 0, 0)
        }
    }
}

fun Toolbar.tintNavigationIcon(@ColorRes colorResource: Int) {
    navigationIcon?.mutate()?.setTint(context.color(colorResource))
}

fun ViewGroup.inflate(@LayoutRes res: Int, attachRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(res, this, attachRoot)
}

typealias PageScrollStateChanged = (state: Int) -> Unit
typealias PageScrolled = (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit
typealias PageSelected = (position: Int) -> Unit

fun ViewPager.addOnPageChangeListener(
    pageScrollStateChanged: PageScrollStateChanged = {},
    pageScrolled: PageScrolled = { _, _, _ -> },
    pageSelected: PageSelected = {}
) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            pageScrollStateChanged.invoke(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            pageScrolled.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            pageSelected.invoke(position)
        }
    })
}

fun ViewPager.doOnPageScrollStateChanged(pageScrollStateChanged: PageScrollStateChanged) {
    this.addOnPageChangeListener(pageScrollStateChanged = pageScrollStateChanged)
}

fun ViewPager.doOnPageScrolled(pageScrolled: PageScrolled) {
    this.addOnPageChangeListener(pageScrolled = pageScrolled)
}

fun ViewPager.doOnPageSelected(pageSelected: PageSelected) {
    this.addOnPageChangeListener(pageSelected = pageSelected)
}