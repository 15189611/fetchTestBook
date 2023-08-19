package com.handy.fetchbook.basic

import android.content.Context
import android.os.Looper
import android.os.SystemClock
import android.util.*
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.os.TraceCompat
import androidx.core.util.forEach
import androidx.core.util.putAll
import androidx.recyclerview.widget.*
import com.handy.fetchbook.BuildConfig
import com.handy.fetchbook.R
import java.lang.reflect.Constructor
import kotlin.Pair

class ModuleAdapterDelegate(private val moduleAdapter: IModuleAdapter, private val dataAdapter: IDataAdapter) {
    internal val viewTypes = SparseArray<ViewType<*>>()
    internal val viewTypeMap = ArrayMap<Class<*>, IViewType<*>>()
    internal val groupTypes = ArrayMap<String, Set<ViewType<*>>>() // 分类
    internal val viewPoolSizes = mutableListOf<Pair<Int, Int>>()

    private val debugViewCount = SparseIntArray()
    private var isDebug: Boolean = BuildConfig.DEBUG
    private var debugTag: String = ""
    private var recyclerView: RecyclerView? = null
    private var spanLookupCache: Pair<Int, ModuleSpanSizeLookup>? = null
    internal var spanCacheEnable: Boolean = true

    // groupType的缓存
    private var groupPositionCache: SparseIntArray = SparseIntArray()
    private var isCacheGroupPosition: Boolean = true

    // viewType的缓存
    private var viewTypeCache = SparseArray<ViewType<*>>()
    private var isCacheViewType = true

    private var moduleCallback: IModuleCallback? = null

    private var modulePreloadHandler: IModulePreloadHandler? = null

    /**
     * 网格的Decoration
     */
    private var spaceDecoration: ModuleGridSpaceDelegateDecoration? = null

    private var viewTypeMax: Int = 0

    private val tag: String
        get() = "$TAG $debugTag "

    private val invalidReleaseType: ViewType<ModuleInvalidReleaseModel>
    private val invalidDebugType: ViewType<ModuleInvalidDebugModel>

    init {
        invalidReleaseType = registerGetType { ModuleInvalidReleaseView(it.context) }
        invalidDebugType = registerGetType { ModuleInvalidDebugView(it.context) }
        register { MallEmptyView(it.context) }
        register { MallSpaceView(it.context) }
        register { ModuleDividerView(it.context) }
        register { NoMoreTipView(it.context) }
        register { ModuleSeparatorBarView(it.context) }

        register { ModuleGroupSectionView(it.context) }

        (moduleAdapter as RecyclerView.Adapter<*>).registerAdapterDataObserver(ModuleCacheDataObserver(this, groupPositionCache, viewTypeCache))
    }

    /**
     * 同步另外一个Adapter注册的view
     */
    fun syncWith(delegate: ModuleAdapterDelegate) {
        assertMainThread()
        check(recyclerView == null) { // 必需在adapter attach RecyclerView之前注册
            "$tag syncWith must before to attach RecyclerView (请在设置给RecyclerView之前注册组件)"
        }

        this.viewTypes.clear()
        this.viewTypes.putAll(delegate.viewTypes)
        this.viewTypeMap.clear()
        this.viewTypeMap.putAll(delegate.viewTypeMap)
        this.groupTypes.clear()
        this.groupTypes.putAll(delegate.groupTypes)
        this.viewTypeMax = delegate.viewTypeMax
        this.viewPoolSizes.clear()
        this.viewPoolSizes.addAll(delegate.viewPoolSizes)
        this.spaceDecoration = delegate.spaceDecoration?.copy(moduleAdapter)
        this.spanCacheEnable = delegate.spanCacheEnable
    }

    fun generateViewTypeIndex(): Int = viewTypeMax++

    fun setModuleCallback(moduleCallback: IModuleCallback?) {
        this.moduleCallback = moduleCallback
    }

    fun setDebug(enable: Boolean) {
        this.isDebug = enable
    }

    fun setDebugTag(tag: String) {
        this.debugTag = tag
    }

    fun getDebugTag(): String {
        return this.debugTag
    }

    fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        val decoration = spaceDecoration
        if (decoration != null) {
            recyclerView.removeItemDecoration(decoration)
            recyclerView.addItemDecoration(decoration)
        }
        logd("attachToRecyclerView...")
    }

    fun onDetachedFromRecyclerView() {
        val decoration = spaceDecoration
        if (decoration != null) {
            recyclerView?.removeItemDecoration(decoration)
        }
        this.recyclerView = null
        logd("detachFromRecyclerView...")
    }

    /**
     * 注册类型
     */
    fun registerGroupType(groupType: String, viewType: ViewType<*>) {
        val types = groupTypes[groupType]
        if (types == null) {
            groupTypes[groupType] = setOf(viewType)
        } else {
            groupTypes[groupType] = types.plus(viewType)
        }
    }

    fun checkRegister(clazz: Class<*>) {
        if (!isDebug) return
        check(!checkClassType(clazz)) {
            "$tag register class $clazz not illegal, must not Primitive, Collection, Map (注册类型不合法，不允许为基本类型，String, Collection, Map)"
        }
        check(recyclerView == null) { // 必需在adapter attach RecyclerView之前注册
            "$tag register must before to attach RecyclerView (请在设置给RecyclerView之前注册组件)"
        }
    }

    private fun checkClassType(clazz: Class<*>): Boolean {
        return ILLEGAL_CLASS_TYPE.any { it?.isAssignableFrom(clazz) == true } || clazz.isArray
    }

    fun <T : Any> registerModelKeyGetter(clazz: Class<T>, getter: ModelKeyGetter<T>) {
        val viewType = viewTypeMap[clazz]
        check(viewType == null) {
            "please not repeat registerModelKeyGetter for clazz:$clazz (请不要重复注册)"
        }
        viewTypeMap[clazz] = ViewTypeGroup(
            modelKeyGetter = getter
        )
    }

    /**
     * 类型注册
     */
    inline fun <reified V, reified M : Any> register(viewClz: Class<V>) where V : IModuleView<M>, V : View {
        val clazzType = M::class.java
        checkRegister(clazzType)
        val group = clazzType.name
        val viewType = ViewType(clazzType, typeIndex = generateViewTypeIndex(), groupType = group) { parent ->
            createView(viewClz, parent)
        }
        registerGroupType(group, viewType)
        addViewType(viewType, null)
    }

    inline fun <reified V, reified M : Any> register(
        gridSize: Int = 1,
        groupType: String? = null,
        poolSize: Int = -1,
        enable: Boolean = true,
        modelKey: Any? = null, // 注册相同的class时需要以这个做区分
        itemSpace: ItemSpace? = null, // 设置Item之间的间距
        noinline creator: (ViewGroup) -> V
    ) where V : IModuleView<M>, V : View {
        val clazzType = M::class.java
        register(clazzType, gridSize, groupType, poolSize, enable, modelKey, itemSpace, creator)
    }

    fun <V, M : Any> register(
        clazzType: Class<M>,
        gridSize: Int = 1,
        groupType: String? = null,
        poolSize: Int = -1,
        enable: Boolean = true,
        modelKey: Any? = null, // 注册相同的class时需要以这个做区分
        itemSpace: ItemSpace? = null, // 设置Item之间的间距
        creator: (ViewGroup) -> V
    ) where V : IModuleView<M>, V : View {
        if (!enable) return
        checkRegister(clazzType)
        val realGroupType = groupType ?: clazzType.name + (if (modelKey != null) "_$modelKey" else "")
        val viewType = ViewType(
            clazzType, typeIndex = generateViewTypeIndex(),
            groupType = realGroupType,
            gridSize = gridSize,
            poolSize = poolSize
        ) { parent -> creator(parent) }
        registerGroupType(realGroupType, viewType)
        addViewType(viewType, modelKey)
        if (itemSpace != null) {
            registerItemSpace(realGroupType, gridSize, itemSpace)
        }
    }

    private inline fun <reified V, reified M : Any> registerGetType(
        crossinline creator: (ViewGroup) -> V
    ): ViewType<M> where V : IModuleView<M>, V : View {
        val clazzType = M::class.java
        val realGroupType = clazzType.name
        val viewType = ViewType(
            clazzType, typeIndex = generateViewTypeIndex(),
            groupType = realGroupType
        ) { parent -> creator(parent) }
        registerGroupType(realGroupType, viewType)
        addViewType(viewType)
        return viewType
    }

    fun addViewType(viewType: ViewType<*>, modelKey: Any? = null) {
        if (modelKey != null) {
            val typeGroup = viewTypeMap[viewType.type]
            check(typeGroup is ViewTypeGroup<*>) {
                "$tag must registerModelKeyGetter before(请先注册modelKey的生成器，以便查找具体类型), modeKey:$modelKey"
            }
            typeGroup.viewTypes.put(viewType.typeIndex, viewType)
            typeGroup.modelKeyTypeMap.put(modelKey, viewType.typeIndex)
        } else {
            if (isDebug) {
                check(viewTypeMap[viewType.type] == null) {
                    "$tag please not register ${viewType.type} repeat(请不要重复注册)"
                }
            }
            viewTypeMap[viewType.type] = viewType
        }
        viewTypes.put(viewType.typeIndex, viewType)
        if (viewType.poolSize > 0) {
            viewPoolSizes.add(viewType.typeIndex to viewType.poolSize)
        }
    }

    /**
     * 设置间距
     */
    private fun registerItemSpace(groupType: String, gridSize: Int, itemSpace: ItemSpace) {
        val decoration = spaceDecoration ?: ModuleGridSpaceDelegateDecoration(moduleAdapter).also {
            spaceDecoration = it
        }
        decoration.registerSpace(groupType, gridSize, itemSpace, isDebug)
    }

    fun <V : View> createView(clazz: Class<V>, parent: ViewGroup):
            View {
        return try {
            val constructor = getConstructor(clazz)
            constructor.newInstance(parent.context)
        } catch (e: Exception) {
            if (isDebug) {
                throw IllegalStateException("$tag createView Error can not create View clazz:$clazz")
            } else {
                loge("$tag createView Error", e)
                View(parent.context)
            }
        }
    }

    /**
     * 获取类型
     */
    fun getViewTypeIndex(position: Int): Int {
        return getViewTypeByPosition(position).typeIndex
    }

    /**
     * 通过adapter的viewType获取
     */
    private fun getViewTypeByIndex(viewTypeIndex: Int): ViewType<*>? {
        val type: ViewType<*>? = viewTypes.get(viewTypeIndex)
        if (type == null && isDebug) {
            throw IllegalStateException("$tag can not found viewType: viewType:$viewTypeIndex")
        } else if (type == null) {
            bmBug("$tag getViewTypeByIndex is null, viewTypeIndex:$viewTypeIndex")
        }
        return type
    }

    /**
     * 获取adapter中的position的viewType
     */
    fun getViewTypeByPosition(position: Int): ViewType<*> {
        if (isDebug) {
            assertMainThread()
        }
        if (isCacheViewType) {
            val viewType = viewTypeCache.get(position)
            if (viewType != null) {
                return viewType
            }
        }
        val model = dataAdapter.getItem(position)
        if (model == null && isDebug) {
            bmBug("$tag getViewTypeByPosition getItem is null for position: $position, adapter count: ${dataAdapter.getCount()}")
            return invalidDebugType
        } else if (model == null) {
            bmBug("$tag getViewTypeByPosition getItem is null for position: $position, adapter count: ${dataAdapter.getCount()}")
            return invalidReleaseType
        }
        val viewType = getViewTypeByModel(model)
        if (isCacheViewType) {
            viewTypeCache.put(position, viewType)
        }

        return viewType
    }

    /**
     * 获取ViewType
     */
    private fun getViewTypeByModel(model: Any): ViewType<*> {
        val modelClazz = model.javaClass
        val type = viewTypeMap[modelClazz]
        val viewType = if (type is ViewType<*>) {
            return type
        } else if (type is ViewTypeGroup<*>) {
            val key = (type as ViewTypeGroup<Any>).modelKeyGetter(model)
            val index = type.modelKeyTypeMap[key] ?: return if (isDebug) invalidDebugType else invalidReleaseType
            type.viewTypes[index]
        } else null
        //val viewType = getViewTypeIndex(model.javaClass)
        if (viewType == null && isDebug) {
            bmBug(
                ("$tag getItemViewType can not found view type " +
                        "for ${model.javaClass.name} model: $model," +
                        " please check you register the Model")
            )
            return invalidDebugType
        } else if (viewType == null) {
            bmBug(
                "$tag getItemViewType can not found view type " +
                        "for ${model.javaClass.name} model: $model," +
                        " please check you register the Model"
            )
            return invalidReleaseType
        }
        return viewType
    }

    /**
     * 创建view
     */
    fun createView(parent: ViewGroup, viewType: Int): View {
        val view = modulePreloadHandler?.acquireView(viewType)
        if (view != null) {
            logd("createView from async PreloadHandler, ${view.javaClass.simpleName}")
        }
        return view ?: createViewReal(parent, viewType)
    }

    /**
     * 创建view
     */
    private fun createViewReal(parent: ViewGroup, viewType: Int): View {
        TraceCompat.beginSection("$tag#createView#viewType#$viewType")
        val startTime: Long = if (isDebug) {
            SystemClock.elapsedRealtimeNanos()
        } else 0L
        val type: ViewType<*> = getViewTypeByIndex(viewType) ?: return View(parent.context)
        val view: View = type.viewCreator.invoke(parent)

        val lp = view.layoutParams
        if (lp != null) { // 有LayoutParams时不需要重新沿用
            view.layoutParams = when (lp) {
                is RecyclerView.LayoutParams -> lp
                is ViewGroup.MarginLayoutParams -> RecyclerView.LayoutParams(lp)
                else -> RecyclerView.LayoutParams(lp)
            }
        } else { // 默认LayoutParams
            view.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        moduleCallback?.onViewCreated(parent, view, viewType)

        if (isDebug) {
            val timeSpent = SystemClock.elapsedRealtimeNanos() - startTime
            debugViewCount.put(viewType, debugViewCount[viewType] + 1)
            logd(
                "createView viewType:$viewType, " +
                        "view:${view.javaClass.simpleName}, viewCount:${debugViewCount[viewType]}," +
                        " timeSpent: ${TimeRecorder.nanoToMillis(timeSpent)}ms"
            )
        }
        TraceCompat.endSection()

        return view
    }

    fun bindHolder(
        view: View,
        viewHolder: RecyclerView.ViewHolder,
        viewType: Int,
        rvAdapter: IModuleAdapter
    ) {
        view.setTag(MALL_ITEM_HOLDER_TAG, object : IRvItemHolder {

            override fun getStartPosition(): Int = rvAdapter.getStartPosition()

            override fun getViewLayoutPosition(): Int = viewHolder.layoutPosition

            override fun getLayoutPosition(): Int {
                val adapterPosition = viewHolder.adapterPosition
                val resultPosition = if (adapterPosition >= 0) {
                    adapterPosition - rvAdapter.getStartPosition()
                } else {
                    //next version remove it
                    val layoutPosition = viewHolder.layoutPosition
                    layoutPosition - rvAdapter.getStartPosition()
                }
                if (resultPosition < 0) {
                    val tagPosition = view.getTag(MALL_ITEM_HOLDER_LAYOUT_POSITION)
                    if (tagPosition is Int) {
                        logw("getLayoutPosition tagPosition $tagPosition")
                        return tagPosition
                    }
                    bmBug(
                        "$tag IRvItemHolder getLayoutPosition invalid, adapterPosition:$adapterPosition," +
                                "layoutPosition:${viewHolder.layoutPosition}, tagPosition:$tagPosition," +
                                " startPosition:${getStartPosition()}"
                    )
                }
                return resultPosition
            }

            override fun getGroupPosition(): Int {
                val type = getViewTypeByIndex(viewType) ?: return getLayoutPosition()
                val layoutPosition = getLayoutPosition()
                if (layoutPosition < 0) {
                    return -1
                }
                val groupPosition = findGroupPosition(type.groupType, layoutPosition)
                if (groupPosition < 0) {
                    val tagGroupPosition = view.getTag(MALL_ITEM_HOLDER_GROUP_POSITION)
                    if (tagGroupPosition is Int) {
                        logw("getGroupPosition tagPosition $tagGroupPosition")
                        return tagGroupPosition
                    }

                    val item = dataAdapter.getItem(layoutPosition)
                    bmBug(
                        "$tag IRvItemHolder getGroupPosition is invalid, adapterPosition:${viewHolder.adapterPosition}, " +
                                "layoutPosition:${viewHolder.layoutPosition}, startPosition:${getStartPosition()}, resultPosition:$layoutPosition, " +
                                "groupType:${type.groupType}, clazz:${type.type}, item:$item, itemCount:${rvAdapter.getItemCount()}"
                    )
                }
                return groupPosition
            }

            override fun getGroupCount(): Int {
                val type = getViewTypeByIndex(viewType) ?: return getItemCount()
                val groupType = type.groupType
                return rvAdapter.getGroupCount(groupType)
            }

            override fun getItemCount(): Int = rvAdapter.getItemCount()
        })
    }

    /**
     * 解绑
     */
    fun onViewRecycled(view: View) {
        if (view is IModuleLifecycle) {
            view.onViewRecycled()
        }
    }

    /**
     * 绑定数据
     */
    fun bindView(view: View, item: Any, position: Int) {
        if (view !is IModuleView<*>) {
            return
        }
        TraceCompat.beginSection("$tag#bindView#${view.javaClass.simpleName}")
        val startTime = if (isDebug) SystemClock.elapsedRealtimeNanos() else 0L
        view.setTag(MALL_ITEM_HOLDER_LAYOUT_POSITION, position)
        val type = getViewTypeByPosition(position)
        val groupPosition = findGroupPosition(type.groupType, position)
        view.setTag(MALL_ITEM_HOLDER_GROUP_POSITION, groupPosition)
        if (view is IModuleLifecycle) {
            view.onBind()
        }
        moduleCallback?.onBind(view, item, position)
        if (view !is IModuleInvalidView) {
            (view as IModuleView<Any>).update(item)
        }
        moduleCallback?.onBindAfter(view, item, position)
        if (isDebug) {
            val timeSpent = SystemClock.elapsedRealtimeNanos() - startTime
            logd(
                "bindView position:$position groupPosition:${view.groupPosition}" +
                        ", view:${view.javaClass.simpleName} timeSpent: ${TimeRecorder.nanoToMillis(timeSpent)}ms"
            )
        }
        TraceCompat.endSection()
    }

    fun onViewAttachedToWindow(itemView: View, layoutPosition: Int) {
        val lp = itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams
            && getViewTypeByPosition(layoutPosition).gridSize == 1) {
            lp.isFullSpan = true
        }
    }

    fun findGroupTypeByPosition(position: Int): String {
        val viewType = getViewTypeByPosition(position)
        return viewType.groupType
    }

    /**
     * 检查是否越界
     */
    private fun checkPosition(position: Int) = position >= 0 && position < dataAdapter.getCount()

    /**
     * 获取groupType
     */
    fun findGroupPosition(groupType: String, position: Int): Int {
        val types = groupTypes[groupType] ?: return -1
        return findCachedGroupPosition(types, position)
    }

    private fun findCachedGroupPosition(types: Set<ViewType<*>>, position: Int): Int {
        if (!checkPosition(position)) return -1
        if (!(getViewTypeByPosition(position) in types)) {
            // 在这里将类型不匹配的拦掉
            return -1
        }
        if (!isCacheGroupPosition) {
            return findGroupPosition(types, position)
        }
        val existing = groupPositionCache.get(position, -1)
        if (existing >= 0) {
            return existing
        }
        val groupPosition = findGroupPosition(types, position)
        groupPositionCache.put(position, groupPosition)
        return groupPosition
    }

    /**
     * position一定是对应types的类型
     */
    private fun findGroupPosition(types: Set<ViewType<*>>, position: Int): Int {
        if (position == 0) return 0
        var typePos = 0
        for (index in position - 1 downTo 0) {
            val item1 = dataAdapter.getItem(index)
            if (item1 is ModuleGroupSectionModel) {
                break
            }
            if (!(getViewTypeByPosition(index) in types)) {
                continue
            }
            // 数据必须是对应的类型
            typePos++
            if (isCacheGroupPosition) { // 找到之前的缓存
                val p = groupPositionCache.get(index, -1)
                if (p >= 0) {
                    typePos += p
                    break
                }
            }
        }
        return typePos
    }

    /**
     * 获取分组类型
     */
    fun getGroupTypes(groupType: String): List<Class<*>> {
        return groupTypes[groupType]?.map { it.type }.orEmpty()
    }

    /**
     * 分组数量
     */
    fun getGroupCount(groupType: String): Int {
        val types = groupTypes[groupType].orEmpty()
        if (types.isEmpty()) return 0
        return (0 until dataAdapter.getCount()).count {
            val type = getViewTypeByPosition(it)
            type != null && type in types
        }
    }

    /**
     * @return <Type, Size>
     */
    fun allRecyclerPoolSize(): List<Pair<Int, Int>> {
        return viewPoolSizes
    }

    // log
    fun logd(msg: String) {
        Log.d(tag, msg)
    }

    fun logw(msg: String, e: Throwable? = null) {
        Log.w(tag, msg)
    }

    fun loge(msg: String, e: Throwable? = null) {
        Log.e(tag, msg)
    }

    companion object {
        const val TAG = "ModuleAdapter"
        const val TYPE_NONE = 0
        private val CONSTRUCTOR_CACHE = ArrayMap<CKey, Constructor<*>>()

        private val ILLEGAL_CLASS_TYPE = arrayOf(
            Int::class.javaPrimitiveType, Long::class.javaPrimitiveType,
            Short::class.javaPrimitiveType, Float::class.javaPrimitiveType, Double::class.javaPrimitiveType,
            Byte::class.javaPrimitiveType, Boolean::class.javaPrimitiveType, Char::class.javaPrimitiveType,
            Int::class.java, Long::class.java, Short::class.java, Float::class.java, Double::class.java,
            Byte::class.java, Boolean::class.java, Char::class.java,
            CharSequence::class.java, Collection::class.java, Map::class.java
        )

        fun <V> getConstructor(clazz: Class<V>, dataClz: Class<*>? = null): Constructor<V> {
            val cKey = CKey(
                clazz,
                dataClz
            )
            val constructor = CONSTRUCTOR_CACHE[cKey]
            if (constructor != null) {
                return constructor as Constructor<V>
            }
            val newConstructor = if (dataClz == null) clazz.getConstructor(Context::class.java)
            else clazz.getConstructor(Context::class.java, dataClz)
            CONSTRUCTOR_CACHE[cKey] = newConstructor
            return newConstructor
        }
    }

    fun getGridLayoutManager(context: Context): GridLayoutManager {
        val pair = getGridSpanLookup()
        val spanCount = pair.first
        val glm = GridLayoutManager(context, spanCount)
        glm.orientation = RecyclerView.VERTICAL
        glm.spanSizeLookup = pair.second
        return glm
    }

    internal fun getSpanLookup() = spanLookupCache?.second

    fun getGridSpanLookup(): Pair<Int, ModuleSpanSizeLookup> {
        val cache = spanLookupCache
        if (cache != null) {
            return cache
        }
        var spanCount = 1
        viewTypes.forEach { _, viewType ->
            val size = viewType.gridSize
            if (size > 0 && spanCount % size != 0) {
                spanCount *= size
            }
        }

        val spanLockup = object : ModuleSpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                if (spanCount == 1) return 1
                val type = getViewTypeByPosition(position) ?: return 1
                return spanCount / type.gridSize
            }
        }
        spanLockup.isSpanGroupIndexCacheEnabled = spanCacheEnable
        spanLockup.isSpanIndexCacheEnabled = spanCacheEnable
        val pair = spanCount to spanLockup
        spanLookupCache = pair
        return pair
    }

    private fun assertMainThread() {
        if (!isOnUiThread()) {
            throw java.lang.IllegalStateException("Expected to run on UI thread!")
        }
    }

    private fun isOnUiThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }

    fun getPreloadHandler(): IModulePreloadHandler {
        return modulePreloadHandler ?: AsyncPreloadHandler(this,
            { getViewTypeByModel(it)?.typeIndex ?: -1 },
            { parent, viewType ->
                createViewReal(parent, viewType)
            }).also { modulePreloadHandler = it }
    }
}

data class CKey(val viewClz: Class<*>, val dataClz: Class<*>? = null)

/**
 * 数据存储器
 */
interface IDataAdapter {
    fun getItem(position: Int): Any?
    fun getCount(): Int
    fun remove(position: Int)
    fun addAll(index: Int, listData: List<Any>, clear: Boolean)
    fun notifyDataSetChange()
}

interface IViewType<T : Any>

/**
 * 组件类型
 */
class ViewType<T : Any>(
    val type: Class<T>,
    val typeIndex: Int,
    val groupType: String,
    val gridSize: Int = 1,
    val poolSize: Int = -1,
    val viewCreator: IViewCreator
) : IViewType<T>

class ViewTypeGroup<T : Any>(
    val viewTypes: SparseArray<ViewType<*>> = SparseArray(),
    val modelKeyTypeMap: MutableMap<Any, Int> = ArrayMap(),
    val modelKeyGetter: ModelKeyGetter<T>
) : IViewType<T>

val sUnableCreator: IViewCreator = { FrameLayout(it.context) }

data class ItemSpace(
    /**
     * item之间的间距
     */
    val spaceH: Int = 0,
    /**
     * item 上下之间的间距，不包括第一个
     */
    val spaceV: Int = 0,
    /**
     * 两边的间距
     */
    val edgeH: Int = 0
)

typealias ModelKeyGetter<T> = (t: T) -> Any?

typealias IViewCreator = (parent: ViewGroup) -> View

interface IRvItemHolder {
    fun getStartPosition(): Int
    fun getViewLayoutPosition(): Int
    fun getLayoutPosition(): Int
    fun getGroupPosition(): Int
    fun getGroupCount(): Int
    fun getItemCount(): Int
}

val MALL_ITEM_HOLDER_TAG = R.id.rv_item_holder_tag
val MALL_ITEM_HOLDER_LAYOUT_POSITION = R.id.rv_item_holder_layout_position
val MALL_ITEM_HOLDER_GROUP_POSITION = R.id.rv_item_holder_group_position
val IModuleView<*>.rvItemHolder: IRvItemHolder?
    get() = if (this is ISubModuleView) {
        this.parent.rvItemHolder
    } else if (this is View) {
        val tag = getTag(MALL_ITEM_HOLDER_TAG)
        if (tag is IRvItemHolder) {
            tag
        } else {
            if (BuildConfig.DEBUG) {
                Toast.makeText(context, "ModuleAdapter使用有误, 具体查看log", Toast.LENGTH_SHORT).show()
            }
            bmBug("can not find rvItemHolder，$this, 使用layoutPosition, groupPosition时请确保不要在init里面调用，对应的View要在ModuleAdapter中注册")
            null
        }
    } else {
        bmBug(" can not find IRvItemHolder, $this， 使用layoutPosition, groupPosition时请确保不要在init里面调用，对应的View要在ModuleAdapter中注册")
        null
    }

/**
 * 对于VLayout的多Adapter，表示该Adapter的开始的position
 */
val IModuleView<*>.startPosition: Int
    get() = rvItemHolder?.getStartPosition() ?: 0

/**
 * ViewHolder的layoutPosition, 不考虑startPosition
 */
val IModuleView<*>.viewLayoutPosition: Int
    get() = rvItemHolder?.getViewLayoutPosition() ?: -1

/**
 * ViewHolder的adapterPosition - startPosition
 */
val IModuleView<*>.layoutPosition: Int
    get() = rvItemHolder?.getLayoutPosition() ?: -1

/**
 * - 分组的position，若在register时设置了groupType，那么就会根据相同groupType的View计算位置，否则根据注册的view计算
 * - 不能在View init阶段使用
 */
val IModuleView<*>.groupPosition: Int
    get() = rvItemHolder?.getGroupPosition() ?: -1

/**
 * 该分组的数量
 */
@Deprecated("Please not use this, 对于长列表来说性能很差，不建议使用")
val IModuleView<*>.groupCount: Int
    get() = rvItemHolder?.getGroupCount() ?: -1

/**
 * 该Adapter的itemCount
 */
val IModuleView<*>.itemCount: Int
    get() = rvItemHolder?.getItemCount() ?: 0

fun bmBug(msg: String, e: Throwable? = null, isDebug: Boolean = false) {
    Log.e("ModuleAdapter bmBug", msg)
}
