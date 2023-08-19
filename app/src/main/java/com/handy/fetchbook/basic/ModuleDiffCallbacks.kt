package com.handy.fetchbook.basic

import android.util.Log
import androidx.recyclerview.widget.*
import com.handy.fetchbook.BuildConfig

/**
 * 根据position来判断是否是同一个元素，比较适用于只有加载更多的场景
 */
class RvPositionDiffCallback(private val oldList: List<Any>, private val newList: List<Any>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItemPosition == newItemPosition

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)
}

/**
 * 完全对比内容是否是同一个元素
 */
class RvDiffCallback(private val oldList: List<Any>, private val newList: List<Any>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)
}

/**
 * 对比类型是否是同一个元素
 */
class RvTypeDiffCallback(private val oldList: List<Any>, private val newList: List<Any>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList.getOrNull(oldItemPosition)?.javaClass == newList.getOrNull(newItemPosition)?.javaClass

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)
}

class UpdateCallback(val adapter: RecyclerView.Adapter<*>, val tag: String, val isDebug: Boolean = BuildConfig.DEBUG) : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {
        if (isDebug) {
            Log.d(tag, " UpdateCallback notifyItemRangeChanged position:$position, count:$count")
        }
        adapter.notifyItemRangeChanged(position, count, "")
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        if (isDebug) {
            Log.d(tag, "UpdateCallback notifyItemMoved fromPosition:$fromPosition, toPosition:$toPosition")
        }
        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onInserted(position: Int, count: Int) {
        if (isDebug) {
            Log.d(tag, "UpdateCallback notifyItemRangeInserted position:$position, count:$count")
        }
        adapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        if (isDebug) {
            Log.d(tag, "UpdateCallback notifyItemRangeRemoved position:$position, count:$count")
        }
        adapter.notifyItemRangeRemoved(position, count)
    }
}
