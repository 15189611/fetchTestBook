package com.handy.fetchbook.basic.vlayout

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.*
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.handy.fetchbook.basic.vlayout.exeption.BaseAdapterDataException
import com.handy.fetchbook.basic.vlayout.holder.DuViewHolder

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
abstract class VLDelegateInnerAdapter<Item> : DelegateAdapter.Adapter<DuViewHolder<Item>>(), AdapterLoader<Item> {
    companion object {
        private val TAG = VLDelegateInnerAdapter::class.java.simpleName
    }

    var list: ArrayList<Item> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private var longClickListener: OnItemLongClickListener<Item>? = null
    private var clickListener: OnItemClickListener<Item>? = null
    private var parentAdapter: VLDelegateAdapter? = null
    private val clickChildViewIds = hashSetOf<Int>()
    private val longClickChildViewIds = hashSetOf<Int>()
    private var onItemChildClickListener: OnItemChildClickListener<Item>? = null
    private var onItemChildLongClickListener: OnItemChildLongClickListener<Item>? = null

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun setItems(items: List<Item>) {
        if (items.isEmpty()) {
            return
        }
        clearItems(false)
        appendItems(items)
    }

    override fun setItemsSafely(items: List<Item>?) {
        if (items == null || items.isEmpty()) {
            clearItems(true)
            return
        }
        setItems(items)
    }

    override fun appendItemsSafely(items: List<Item>?) {
        if (items == null || items.isEmpty()) {
            return
        }
        appendItems(items)
    }

    override fun insertItemsSafely(items: List<Item>?, startPos: Int) {
        if (items == null || items.isEmpty()) {
            return
        }
        insertItems(items, startPos)
    }

    override fun autoInsertItemsSafely(items: List<Item>?) {
        if (items == null || items.isEmpty()) {
            return
        }
        autoInsertItems(items)
    }

    override fun insertItems(items: List<Item>, startPos: Int) {
        if (startPos < 0 || startPos > list.size) {
            return
        }
        list.addAll(startPos, items)
        notifyItemRangeInserted(startPos, items.size)
    }

    override fun clearItems() {
        clearItems(true)
    }

    override fun clearItems(notify: Boolean) {
        list.clear()
        if (notify) {
            notifyDataSetChanged()
        }
    }

    override fun appendItems(items: List<Item>) {
        if (items.isEmpty()) {
            return
        }
        val positionStart = list.size
        list.addAll(items)
        val itemCount = list.size - positionStart

        if (positionStart == 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(positionStart, itemCount)
        }
    }

    override fun removeItem(position: Int): Item? {
        if (checkIllegalPosition(position)) {
            return null
        }
        val bean = list.removeAt(position)
        notifyItemRemoved(position)
        return bean
    }

    override fun removeItem(item: Item): Boolean {
        val position = list.indexOf(item)
        val removeItem = removeItem(position)
        return removeItem != null
    }

    override fun updateItem(item: Item, payload: Any?): Boolean {
        val index = list.indexOf(item)
        if (checkIllegalPosition(index)) {
            return false
        }
        notifyItemChanged(index, payload)
        return true
    }

    override fun updateItemWithEmptyPayload(item: Item): Boolean {
        return updateItem(item, "")
    }

    override fun getItem(position: Int): Item? {
        return if (checkIllegalPosition(position)) null else list[position]
    }

    override fun insertItem(position: Int, item: Item) {
        val p = when {
            position < 0 -> 0
            position > list.size -> list.size
            else -> position
        }
        list.add(p, item)
        notifyItemInserted(p)
    }

    override fun autoInsertItems(items: List<Item>) {
        if (list.isEmpty()) {
            setItems(items)
        } else {
            appendItems(items)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuViewHolder<Item> {
        return onViewHolderCreate(parent, viewType).apply {
            this.attachAdapter(this@VLDelegateInnerAdapter)
            bindItemChildClickListener(this)
        }
    }

    abstract override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): DuViewHolder<Item>
    override fun onBindViewHolder(holder: DuViewHolder<Item>, position: Int) {
        bindViewHolder(holder, position, null)
    }

    override fun onBindViewHolder(
        holder: DuViewHolder<Item>,
        position: Int,
        payloads: List<Any>
    ) {
        bindViewHolder(holder, position, payloads)
    }

    private fun bindViewHolder(holder: DuViewHolder<Item>, position: Int, o: List<Any>?) {
        bindDefaultHolder(holder, position, o)
    }

    private fun bindDefaultHolder(
        holder: DuViewHolder<Item>,
        position: Int,
        payloads: List<Any>?
    ) {
        if (checkIllegalPosition(position)) {
            return
        }
        handleHolderClick(holder, position)
        if (payloads == null || payloads.isEmpty()) {
            onViewHolderBind(holder, position)
        } else {
            onViewHolderBind(holder, position, payloads)
        }
    }

    private fun handleHolderClick(holder: DuViewHolder<Item>, position: Int) {
        if (clickListener != null && holder.enableCLick) {
            holder.itemView.setOnClickListener {
                val layoutPosition = getParentOffsetPosition(holder.layoutPosition, position)
                if (!checkIllegalPosition(layoutPosition)) {
                    val item = getItem(layoutPosition)
                    if (item != null) {
                        performClick(holder, layoutPosition, item)
                    }
                }
            }
        }
        if (longClickListener != null) {
            holder.itemView.setOnLongClickListener {
                val layoutPosition = getParentOffsetPosition(holder.layoutPosition, position)
                val item = getItem(layoutPosition)
                item != null && !checkIllegalPosition(layoutPosition) && performLongClick(
                    holder, layoutPosition, item
                )
            }
        }
    }

    override fun performClick(holder: DuViewHolder<Item>, position: Int, item: Item) {
        clickListener?.invoke(holder, position, item)
    }

    override fun performLongClick(holder: DuViewHolder<Item>, position: Int, item: Item): Boolean {
        return longClickListener?.invoke(holder, position, item) ?: false
    }

    override fun onViewHolderBind(holder: DuViewHolder<Item>, position: Int) {
        try {
            holder.containerView.isGone = false
            holder.onBind(list[position], position)
        } catch (e: Exception) {
            holder.containerView.isGone = true
            RecyclerViewConfig.handleCatchError(BaseAdapterDataException(e, list[position]))
        }
    }

    override fun onViewHolderBind(holder: DuViewHolder<Item>, position: Int, payloads: List<Any>) {
        try {
            holder.containerView.isGone = false
            holder.onPartBind(list[position], position, payloads)
        } catch (e: Exception) {
            holder.containerView.isGone = true
            RecyclerViewConfig.handleCatchError(BaseAdapterDataException(e, list[position]))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (!checkIllegalPosition(position)) {
            getItemViewTypes(position)
        } else {
            super.getItemViewType(position)
        }
    }

    override fun findFirstPositionOfType(viewType: Int): Int {
        return findFirstPositionOfType(viewType, 0)
    }

    override fun findFirstPositionOfType(viewType: Int, offsetPosition: Int): Int {
        if (checkIllegalPosition(offsetPosition)) {
            return RecyclerView.NO_POSITION
        }
        for (i in offsetPosition until list.size) {
            if (getItemViewType(i) == viewType) {
                return i
            }
        }
        return RecyclerView.NO_POSITION
    }

    override fun findLastPositionOfType(viewType: Int): Int {
        return findLastPositionOfType(viewType, list.size - 1)
    }

    override fun findLastPositionOfType(viewType: Int, offsetPosition: Int): Int {
        if (checkIllegalPosition(offsetPosition)) {
            return RecyclerView.NO_POSITION
        }
        for (i in offsetPosition downTo 0) {
            if (getItemViewType(i) == viewType) {
                return i
            }
        }
        return RecyclerView.NO_POSITION
    }

    override fun getItemViewTypes(position: Int): Int {
        return 0
    }

    // 重复调用的话，影响不大，helper 内部 有做 recyclerview 判断
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        val manager = recyclerView.layoutManager
            ?: throw NullPointerException("Did you forget call setLayoutManager() at first?")
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return initSpanSize(position)
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
    }

    private fun initSpanSize(position: Int): Int {
        return getSpanSize(position)
    }

    override fun getSpanSize(position: Int): Int {
        return 1
    }

    override fun setOnItemClickListener(listener: OnItemClickListener<Item>?) {
        this.clickListener = listener
    }

    override fun setOnItemLongClickListener(listener: OnItemLongClickListener<Item>?) {
        this.longClickListener = listener
    }

    override fun checkIllegalPosition(position: Int): Boolean {
        return list.isEmpty() || position < 0 || position >= list.size
    }

    /**
     * 将 RecyclerView 的绝对 position 转换为 Adapter 的相对 position
     */
    fun getOffsetPositions(positionSet: List<Int>): List<Int> {
        return parentAdapter?.getOffsetPositions(positionSet, this) ?: positionSet
    }

    fun findVisiblePosition(offsetPosition: Int, position: Int) {
        @Suppress("UNCHECKED_CAST")
        val duViewHolder =
            recyclerView.findViewHolderForAdapterPosition(position) as? DuViewHolder<Item>
        duViewHolder?.onVisible(list[offsetPosition], offsetPosition)
    }

    override fun onViewAttachedToWindow(holder: DuViewHolder<Item>) {
        super.onViewAttachedToWindow(holder)
        holder.attachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: DuViewHolder<Item>) {
        super.onViewDetachedFromWindow(holder)
        holder.detachedFromWindow()
    }

    override fun onViewRecycled(holder: DuViewHolder<Item>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun attachParentAdapter(parentAdapter: VLDelegateAdapter) {
        this.parentAdapter = parentAdapter
        if (!::recyclerView.isInitialized) {
            val aRecyclerView = parentAdapter.getAttachRecyclerView() ?: return
            onAttachedToRecyclerView(aRecyclerView)
        }
    }

    fun detachParentAdapter(parentAdapter: VLDelegateAdapter) {
        if (parentAdapter === this.parentAdapter) {
            this.parentAdapter = null
        }
    }

    fun getOffsetPosition(position: Int): Int {
        return if (parentAdapter == null) {
            position
        } else {
            parentAdapter?.findOffsetPosition(position) ?: position
        }
    }

    private fun getParentOffsetPosition(layoutPosition: Int, position: Int): Int {
        return if (parentAdapter == null) {
            position
        } else {
            parentAdapter?.findOffsetPosition(layoutPosition) ?: position
        }
    }

    fun setMaxRecycledViews(adapterIndex: Int, viewType: Int, max: Int) {
        val realType = Cantor.getCantor(viewType.toLong(), adapterIndex.toLong())
        recyclerView.recycledViewPool.setMaxRecycledViews(realType.toInt(), max)
    }

    private fun bindItemChildClickListener(holder: DuViewHolder<Item>) {
        onItemChildClickListener?.let { clickListener ->
            for (id in getItemChildClickViewIds()) {
                val childView = holder.itemView.findViewById<View>(id) ?: continue
                if (!childView.isClickable) {
                    childView.isClickable = true
                }
                childView.setOnClickListener {
                    val position = holder.adapterPosition
                    if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                    clickListener.invoke(holder, position, childView)
                }
            }

            for (id in getItemChildLongClickViewIds()) {
                val childView = holder.itemView.findViewById<View>(id) ?: continue

                if (!childView.isClickable) {
                    childView.isClickable = true
                }
                childView.setOnLongClickListener {
                    val position = holder.adapterPosition
                    if (position == RecyclerView.NO_POSITION) {
                        return@setOnLongClickListener false
                    }
                    onItemChildClickListener?.invoke(holder, position, childView)
                    true
                }
            }
        }
    }

    fun addItemChildClickViewIds(@IdRes vararg ids: Int) {
        for (id in ids) {
            clickChildViewIds.add(id)
        }
    }

    fun setOnItemChildClickListener(onItemChildClickListener: OnItemChildClickListener<Item>) {
        this.onItemChildClickListener = onItemChildClickListener
    }

    fun addItemChildLongClickViewIds(@IdRes vararg ids: Int) {
        for (id in ids) {
            longClickChildViewIds.add(id)
        }
    }

    fun setOnItemChildLongClickListener(onItemChildLongClickListener: OnItemChildLongClickListener<Item>) {
        this.onItemChildLongClickListener = onItemChildLongClickListener
    }

    fun getItemChildClickViewIds(): HashSet<Int> {
        return clickChildViewIds
    }

    fun getItemChildLongClickViewIds(): HashSet<Int> {
        return longClickChildViewIds
    }
}
