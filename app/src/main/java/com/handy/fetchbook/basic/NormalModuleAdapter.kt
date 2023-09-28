package com.handy.fetchbook.basic

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*


class NormalModuleAdapter(private val calDiff: Boolean = false) :
    RecyclerView.Adapter<NormalModuleAdapter.MCommonViewHolder>(), IModuleImplAdapter {
    companion object {
        const val TAG = "NormalModuleAdapter"
    }
    internal lateinit var recyclerView: RecyclerView
    private val list = mutableListOf<Any>()

    override val delegate: ModuleAdapterDelegate = ModuleAdapterDelegate(
        this,
        object : IDataAdapter {
            override fun getItem(position: Int): Any? = list.getOrNull(position)
            override fun getCount(): Int = list.size
            override fun remove(position: Int) {
                list.removeAt(position)
            }

            override fun addAll(index: Int, listData: List<Any>, clear: Boolean) {
                if (clear) list.clear()
                list.addAll(index, listData)
            }

            override fun notifyDataSetChange() {
                notifyDataSetChanged()
            }
        })

    private fun checkIllegalPosition(position: Int): Boolean {
        return list.isEmpty() || position < 0 || position >= list.size
    }

    override fun setDebug(debug: Boolean) {
        delegate.setDebug(debug)
    }

    override fun setDebugTag(debugTag: String) {
        delegate.setDebugTag(debugTag)
    }

    override fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    override fun clearItems() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun appendItems(items: List<Any>) {
        if (isEmpty()) {
            setItems(items)
        } else if (items.isNotEmpty()) {
            val startPosition = list.size
            list.addAll(items)
            notifyItemRangeInserted(startPosition, items.size)
        }

    }

    override fun insertItems(position: Int, items: List<Any>) {
        if (items.isEmpty()) {
            return
        }
        val p = when {
            position < 0 -> 0
            position > list.size -> list.size
            else -> position
        }
        list.addAll(p, items)
        notifyItemRangeInserted(position, items.size)
    }

    override fun insertItem(position: Int, item: Any) {
        val p = when {
            position < 0 -> 0
            position > list.size -> list.size
            else -> position
        }
        list.add(p, item)
        notifyItemInserted(position)
    }

    override fun removeItem(position: Int): Any? {
        if (checkIllegalPosition(position)) {
            return null
        }
        val bean = list.removeAt(position)
        notifyItemRemoved(position)
        return bean
    }

    override fun removeItem(item: Any): Boolean {
        val position = list.indexOf(item)
        val removeItem = removeItem(position)
        return removeItem != null
    }

    override fun updateItem(position: Int, item: Any?) {
        if (checkIllegalPosition(position)) {
            return
        }
        if (item != null) {
            list.removeAt(position)
            list.add(position, item)
        }
        notifyItemChanged(position)
    }

    override fun updateItem(position: Int, item: Any?, payload: Any?) {
        if (checkIllegalPosition(position)) {
            return
        }
        if (item != null) {
            list.removeAt(position)
            list.add(position, item)
        }
        notifyItemChanged(position, payload)
    }

    override fun indexOf(item: Any): Int {
        return list.indexOf(item)
    }

    override fun indexOf(predicate: (Any) -> Boolean): Int {
        return list.indexOfFirst(predicate)
    }

    override fun getGroupPosition(groupType: String, position: Int): Int {
        return delegate.findGroupPosition(groupType, position)
    }

    override fun getGroupTypeByPosition(position: Int): String {
        return delegate.findGroupTypeByPosition(position)
    }

    override fun getGroupTypes(groupType: String): List<Class<*>> {
        return delegate.getGroupTypes(groupType)
    }

    override fun getGroupCount(groupType: String): Int {
        return delegate.getGroupCount(groupType)
    }

    override fun getStartPosition(): Int = 0

    override fun getSpanCount(): Int {
        return delegate.getGridSpanLookup().first
    }

    override fun getSpanSize(position: Int): Int {
        val spanLookup = delegate.getGridSpanLookup().second
        return spanLookup.getSpanSize(position)
    }

    override fun getSpanIndex(position: Int): Int {
        val (spanCount, spanLookup) = delegate.getGridSpanLookup()
        return spanLookup.getCachedSpanIndex(position, spanCount)
    }

    override fun getSpanGroupIndex(position: Int): Int {
        val (spanCount, spanLookup) = delegate.getGridSpanLookup()
        return spanLookup.getCachedSpanGroupIndex(position, spanCount)
    }

    override fun setItems(items: List<Any>) {
        if (calDiff && list.size > 0) {
            val result = DiffUtil.calculateDiff(
                RvDiffCallback(
                    list,
                    items
                )
            )
            list.clear()
            list.addAll(items)
            result.dispatchUpdatesTo(this)
        } else if (items != list) {
            list.clear()
            list.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun setItemsWithDiff(items: List<Any>, callback: DiffUtil.Callback?, updateCallback: ListUpdateCallback?) {
        if (list.size > 0) {
            val result = DiffUtil.calculateDiff(callback ?: RvDiffCallback(list, items))
            list.clear()
            list.addAll(items)
            if (updateCallback != null) {
                result.dispatchUpdatesTo(updateCallback)
            } else {
                result.dispatchUpdatesTo(this)
            }
        } else if (items != list) {
            list.clear()
            list.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun getItems(): List<Any> {
        return list
    }

    override fun getItem(position: Int): Any? = list.getOrNull(position)

    fun setData(data: List<Any>) {
        if (data != this.list) {
            this.list.clear()
            this.list.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return delegate.getViewTypeIndex(position)
    }

    override fun getGridLayoutManager(context: Context): GridLayoutManager {
        return delegate.getGridLayoutManager(context)
    }

    override fun setSpanCacheEnable(isEnable: Boolean) {
        delegate.spanCacheEnable = isEnable
    }

    override fun refresh(oldItem: Any, newItem: Any?) {
        val index = list.indexOf(oldItem)
        if (index < 0) {
            return
        }
        if (newItem != null) {
            if (oldItem != newItem) {
                list.removeAt(index)
                list.add(index, newItem)
            }
            notifyItemChanged(index)
        } else {
            notifyItemRemoved(index)
        }
    }

    override fun setModuleCallback(moduleCallback: IModuleCallback?) {
        delegate.setModuleCallback(moduleCallback)
    }

    override fun syncWith(adapter: IModuleAdapter) {
        if (adapter is IModuleImplAdapter) {
            delegate.syncWith(adapter.delegate)
        }
    }

    override fun getPreloadHandler(): IModulePreloadHandler {
        return delegate.getPreloadHandler()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MCommonViewHolder {
        return MCommonViewHolder(
            delegate.createView(parent, viewType)
        ).also {
            delegate.bindHolder(it.itemView, it, viewType, this)
        }
    }

    override fun onBindViewHolder(holder: MCommonViewHolder, position: Int) {
        val model = list.getOrNull(position) ?: return
        delegate.bindView(holder.itemView, model, position)
    }

    override fun onViewRecycled(holder: MCommonViewHolder) {
        super.onViewRecycled(holder)
        delegate.onViewRecycled(holder.itemView)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        delegate.onAttachedToRecyclerView(recyclerView)
        for ((type, maxSize) in delegate.allRecyclerPoolSize()) {
            delegate.logd("setMaxRecycledViews type:$type, maxSize:$maxSize")
            recyclerView.recycledViewPool.setMaxRecycledViews(type, maxSize)
        }
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        delegate.onDetachedFromRecyclerView()
    }

    override fun onViewAttachedToWindow(holder: MCommonViewHolder) {
        super.onViewAttachedToWindow(holder)
        delegate.onViewAttachedToWindow(holder.itemView, holder.layoutPosition)
    }

    /**
     * 如果需要一个类对应多个View需要先注册这个生成器类
     */
    inline fun <reified T : Any> registerModelKeyGetter(noinline getter: ModelKeyGetter<T>) {
        val clazz = T::class.java
        delegate.registerModelKeyGetter(clazz, getter)
    }

    override fun <T : Any> registerModelKeyGetter(clazz: Class<T>, getter: ModelKeyGetter<T>) {
        delegate.registerModelKeyGetter(clazz, getter)
    }

    /**
     * 注册类型
     * 注: 1. 不能重复注册相同的model类, 在Fragment里面使用报错的，将register提前到view创建之前，或者使用FragmentStateAdapter
     * 2. 必需在add到RecyclerView之前调用
     * @param gridSize 每行所占的网格数
     * @param groupType 类型归类，获取position需要
     */
    @Deprecated("Please use register{} instead")
    inline fun <reified V, reified M : Any> register(viewClz: Class<V>) where V : IModuleView<M>, V : View {
        delegate.register(viewClz)
    }

    /**
     * 注册类型
     * 注: 1. 不能重复注册相同的model类, 在Fragment里面使用报错的，将register提前到view创建之前，或者使用FragmentStateAdapter
     * 2. 必需在add到RecyclerView之前调用
     * @param gridSize 每行所占的网格数
     * @param groupType 类型归类，获取position需要, 在View中通过groupPosition获取对应位置，通过ModuleGroupSectionModel可以用来分割
     * @param poolSize recyclerview的缓存大小
     * @param itemSpace 设置Item之间的间距
     * @param enable 该注册是否生效，方便书写，防止大堆if else
     */
    inline fun <reified V, reified M : Any> register(
        gridSize: Int = 1,
        groupType: String? = null,
        poolSize: Int = -1,
        enable: Boolean = true,
        modelKey: Any? = null, // 注册相同的class时需要以这个做区分
        itemSpace: ItemSpace? = null, // 设置Item之间的间距
        noinline creator: (ViewGroup) -> V
    )
        where V : IModuleView<M>, V : View {
        delegate.register(
            gridSize = gridSize,
            groupType = groupType,
            poolSize = poolSize,
            enable = enable,
            modelKey = modelKey,
            itemSpace = itemSpace,
            creator = creator
        )
    }

    override fun <V, M : Any> register(
        clazzType: Class<M>,
        gridSize: Int,
        groupType: String?,
        poolSize: Int,
        enable: Boolean,
        modelKey: Any?, // 注册相同的class时需要以这个做区分
        itemSpace: ItemSpace?, // 设置Item之间的间距
        creator: (ViewGroup) -> V
    ) where V : IModuleView<M>, V : View {
        delegate.register(
            clazzType = clazzType,
            gridSize = gridSize,
            groupType = groupType,
            poolSize = poolSize,
            enable = enable,
            modelKey = modelKey,
            itemSpace = itemSpace,
            creator = creator
        )
    }

    class MCommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
