package com.handy.fetchbook.basic

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*

interface IModuleAdapter {
    /**
     * 设置log开关
     */
    fun setDebug(debug: Boolean)

    /**
     * 调试用，输出log, 方便在报错或者打印log时无法确认哪个adapter打印的，建议设置
     */
    fun setDebugTag(debugTag: String)

    /**
     * 是否为空
     */
    fun isEmpty(): Boolean

    /** 设置Item*/
    fun setItems(items: List<Any>)

    // 设置对比
    fun setItemsWithDiff(items: List<Any>, callback: DiffUtil.Callback? = null, updateCallback: ListUpdateCallback? = null)

    /**
     * - 获取Items
     * - 一定要注意，不要通过as强转成特定类型
     * - 需要特定类型请使用 List.filterInstance
     */
    fun getItems(): List<Any>

    /**
     * 添加item
     */
    fun appendItems(items: List<Any>)

    /**插入数据*/
    fun insertItems(position: Int, items: List<Any>)

    /**插入数据*/
    fun insertItem(position: Int, item: Any)

    /**
     * 删除数据
     * 返回
     */
    fun removeItem(position: Int): Any?

    /**删除*/
    fun removeItem(item: Any): Boolean

    /**
     * 更新Item
     * @param item 如果为null，则表示只通过notifyItemChange更新
     *
     */
    fun updateItem(position: Int, item: Any? = null)

    /**
     * 更新Item
     * @param item 如果为null，则表示只通过notifyItemChange更新
     * @param payload 局部刷新
     */
    fun updateItem(position: Int, item: Any? = null, payload: Any? = null)

    // 获取元素个数
    fun getItemCount(): Int

    /**
     * 查找元素位置
     */
    fun indexOf(item: Any): Int

    /** 获取元素位置 */
    fun indexOf(predicate: (Any) -> Boolean): Int

    /** 清除元素 */
    fun clearItems()

    // 获取指定位置的元素
    fun getItem(position: Int): Any?

    /**
     * 获取分组中的相对位置
     * @param groupType 分组类型
     * @param position 绝对位置
     */
    fun getGroupPosition(groupType: String, position: Int): Int

    /**
     * 根据位置获取分组类型
     */
    fun getGroupTypeByPosition(position: Int): String

    /**
     * 获致分组的所有类型
     */
    fun getGroupTypes(groupType: String): List<Class<*>>

    /**
     * 获取分组的数据个数
     */
    fun getGroupCount(groupType: String): Int

    /**
     * 获取Adapter第一个的数组
     */
    fun getStartPosition(): Int

    /**
     * 获取GridLayoutManager，获取注册的gridSize计算
     */
    fun getGridLayoutManager(context: Context): GridLayoutManager

    /**
     * 设置setSpanGroupIndexCacheEnabled，isSpanIndexCacheEnabled，请在recyclerView.layoutManger设置之前
     */
    fun setSpanCacheEnable(isEnable: Boolean)

    /**
     * 共有几格
     */
    fun getSpanCount(): Int

    /**
     * 占几格
     */
    fun getSpanSize(position: Int): Int

    /**
     * 在第几格
     */
    fun getSpanIndex(position: Int): Int

    /**
     * 在第几行
     */
    fun getSpanGroupIndex(position: Int): Int

    /**
     * 刷新指定的元素
     */
    fun refresh(oldItem: Any, newItem: Any? = oldItem)

    /**
     * 设置createView和bindView时的拦截，可以通过AbsModuleCallback实现需要的方法
     */
    fun setModuleCallback(moduleCallback: IModuleCallback?)

    /**
     * 同步另外一个view注册的view， 必需在attach到RecyclerView之前
     */
    fun syncWith(adapter: IModuleAdapter)

    /**
     * 获取预加载的handler, context最好传入Activity的context
     */
    @Deprecated("用处不大，废弃")
    fun getPreloadHandler(): IModulePreloadHandler

    /**
     * 用来区分同一个类对应不同View, 获取同一个类中区分类型的字段，如
     * registerModelKeyGetter(ModelA::class::java) { it.type }
     * register(clazzType=ModelA::class::java, modelKey="type1") {  ViewA(it.context) }
     * register(clazzType=ModelA::class::java, modelKey="type2") {  ViewA(it.context) }
     */
    fun <T : Any> registerModelKeyGetter(clazz: Class<T>, getter: ModelKeyGetter<T>)

    fun <V, M : Any> register(
        clazzType: Class<M>,
        gridSize: Int = 1,
        groupType: String? = null,
        poolSize: Int = -1,
        enable: Boolean = true,
        modelKey: Any? = null, // 注册相同的class时需要以这个做区分
        itemSpace: ItemSpace? = null, // 设置Item之间的间距
        creator: (ViewGroup) -> V
    ) where V : IModuleView<M>, V : View
}

internal interface IModuleImplAdapter : IModuleAdapter {
    val delegate: ModuleAdapterDelegate
}
