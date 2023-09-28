/*
 * Copyright (c) 2016.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.handy.fetchbook.basic.vlayout.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.handy.fetchbook.basic.vlayout.VLDelegateInnerAdapter
import kotlinx.android.extensions.LayoutContainer

abstract class DuViewHolder<Item> constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer{
    private var adapter: VLDelegateInnerAdapter<Item>? = null
    var enableCLick = true
    var isSelectMode: Boolean = false
    val context: Context
        get() = itemView.context

    constructor(itemView: View, enableCLick: Boolean) : this(itemView) {
        this.enableCLick = enableCLick
    }

    abstract fun onBind(item: Item, position: Int)

    open fun onPartBind(item: Item, position: Int, payloads: List<Any>) {
        onBind(item, position)
    }

    open fun onExposed(item: Item, position: Int): Boolean {
        return false
    }

    open fun onSensorExposed(item: Item, position: Int): Boolean {
        return false
    }

    fun attachAdapter(adapter: VLDelegateInnerAdapter<Item>) {
        this.adapter = adapter
    }

    fun getOffsetPosition(): Int {
        val adapter = adapter ?: return layoutPosition
        return adapter.getOffsetPosition(layoutPosition)
    }

    open fun attachedToWindow() = Unit

    open fun detachedFromWindow() = Unit

    open fun onViewRecycled() = Unit

    open fun onVisible(item: Item, position: Int) = Unit
}
