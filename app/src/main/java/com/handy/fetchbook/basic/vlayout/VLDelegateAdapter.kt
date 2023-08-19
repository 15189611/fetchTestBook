package com.handy.fetchbook.basic.vlayout

import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.handy.fetchbook.basic.vlayout.holder.DuViewHolder

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
@Suppress("MemberVisibilityCanBePrivate")
class VLDelegateAdapter(layoutManager: VirtualLayoutManager?) : DelegateAdapter(layoutManager) {
    private val tag = "VLDelegateAdapter"
    private var recyclerView: RecyclerView? = null

    fun getAttachRecyclerView(): RecyclerView? = recyclerView

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        if (position >= 0) {
            val pair = findAdapterByPosition(position)
            val second = pair?.second ?: return
            if (second is VLDelegateInnerAdapter<*> && holder is DuViewHolder<*>) {
                try {
                    second.onViewAttachedToWindow(holder)
                } catch (e: Exception) {
                    RecyclerViewConfig.handleCatchError(e)
                }
            } else if (second is AdapterViewState<*>) {
                try {
                    second.onViewAttachedToWindow(holder)
                } catch (e: Exception) {
                    RecyclerViewConfig.handleCatchError(e)
                }
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        // replace  holder.getPosition() to holder.adapterPosition ;
        val position = holder.adapterPosition
        if (position >= 0) {
            val pair = findAdapterByPosition(position)
            val second = pair?.second ?: return
            if (second is VLDelegateInnerAdapter<*> && holder is DuViewHolder<*>) {
                try {
                    second.onViewDetachedFromWindow(holder)
                } catch (e: Exception) {
                    RecyclerViewConfig.handleCatchError(e)
                }
            } else if (second is AdapterViewState<*>) {
                try {
                    second.onViewDetachedFromWindow(holder)
                } catch (e: Exception) {
                    RecyclerViewConfig.handleCatchError(e)
                }
            }
        }
    }

    private fun checkIllegalPosition(position: Int): Boolean {
        return position < 0 || position > itemCount - 1
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        val adaptersCount = adaptersCount
        for (i in 0 until adaptersCount) {
            findAdapterByIndex(i).onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        val adaptersCount = adaptersCount
        for (i in 0 until adaptersCount) {
            findAdapterByIndex(i).onDetachedFromRecyclerView(recyclerView)
        }
    }

    override fun addAdapter(adapter: Adapter<*>?) {
        super.addAdapter(adapter)
        bindAdapter(adapter)
    }

    override fun addAdapters(adapters: MutableList<Adapter<RecyclerView.ViewHolder>>?) {
        super.addAdapters(adapters)
        if (adapters?.isNotEmpty() == true) {
            adapters.forEach {
                bindAdapter(it)
            }
        }
    }

    override fun addAdapters(position: Int, adapters: MutableList<Adapter<RecyclerView.ViewHolder>>?) {
        super.addAdapters(position, adapters)
        if (adapters?.isNotEmpty() == true) {
            adapters.forEach {
                bindAdapter(it)
            }
        }
    }

    override fun addAdapter(position: Int, adapter: Adapter<*>?) {
        super.addAdapter(position, adapter)
        bindAdapter(adapter)
    }

    private fun bindAdapter(adapter: Adapter<*>?) {
        if (adapter is VLDelegateInnerAdapter<*>) {
            adapter.attachParentAdapter(this)
        }
    }

    fun <Item> getOffsetPositions(positionSet: List<Int>, adapter: VLDelegateInnerAdapter<Item>): List<Int> {
        if (adaptersCount == 1) {
            return positionSet
        } else {
            val size = positionSet.size
            var index = size - 1
            // 倒序查找
            while (index >= 0) {
                val position = positionSet[index]
                // AdapterDataObserver 包含 startPosition 等信息
                val first = findAdapterByPosition(position)?.first ?: continue
                val findAdapter = findAdapterByPosition(position)?.second ?: continue
                // 如果是当前的 adapter
                if (findAdapter === adapter) {
                    // 获取 adapter 开始的第一个 position
                    val startPosition = first.startPosition
                    // 获取返回的可见角标的 开始的 position
                    val firstVisiblePosition = positionSet.first()
                    // 取相对大的 position
                    val maxPosition = startPosition.coerceAtLeast(firstVisiblePosition)
                    val startIndex = positionSet.indexOf(maxPosition)
                    val endIndex = index + 1
                    val subList = positionSet.subList(startIndex, endIndex)
                    val resultList = subList.map {
                        // index 便宜转换为 adapter 中的值
                        findOffsetPosition(it)
                    }
                    return resultList
                } else {
                    // 如果不是当前 adapter ，向前继续查找上一个 adapter, 这里使用 first.startPosition 来实现跳级，而不是一个接一个遍历
                    index = positionSet.indexOf(first.startPosition - 1)
                }
            }
        }
        // 没有命中返回 空数组
        return emptyList()
    }
}
