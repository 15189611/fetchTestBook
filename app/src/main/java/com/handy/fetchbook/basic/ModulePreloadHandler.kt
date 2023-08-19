package com.handy.fetchbook.basic

import android.content.Context
import android.os.Looper
import android.os.SystemClock
import android.util.AttributeSet
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pools
import androidx.core.view.LayoutInflaterCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class AsyncPreloadHandler(
    val delegate: ModuleAdapterDelegate,
    private val viewTypeGetter: (Any) -> Int,
    private val viewCreator: (ViewGroup, Int) -> View
) : IModulePreloadHandler {
    private val mockCacheList = mutableListOf<Pair<Any, Int>>()
    private val cacheViews = ConcurrentHashMap<Int, Pools.Pool<PreloadViewItem>>()
    private var isCleared: Boolean = false
    private val preloadLogItems = mutableListOf<PreloadLogItem>()

    private val inflaterPool = Pools.SynchronizedPool<LayoutInflater>(6)
    private val inflaterIndex = AtomicInteger(0)

    @Volatile
    private var recyclerView: RecyclerView? = null
    private fun getRecyclerView(context: Context): RecyclerView? {
        if (recyclerView == null) {
            synchronized(this) {
                if (recyclerView == null) {
                    recyclerView = RecyclerView(context)
                }
            }
        }
        return recyclerView
    }

    override fun addItem(item: Any, size: Int): IModulePreloadHandler {
        mockCacheList.add(item to size)
        return this
    }

    override fun preload(context: Context, lifecycleOwner: LifecycleOwner) {
        isCleared = false
        val list = ArrayList(mockCacheList)
        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val allJobs = mutableListOf<Job>()
            delegate.logd("preload start!!!!")
            list.flatMap { (item, size) -> List(size) { index ->
                Triple(item, size, index)
            } }.forEach { (item, size, index) ->
                val job = launch(Dispatchers.IO) {
                    preloadItem(context, item, index, size)
                }
                allJobs.add(job)
            }
            allJobs.joinAll()
            log("preload end!!!!")

            // 移除所有 inflater
            while (true) {
                val inflater = inflaterPool.acquire() ?: break
                log("remove inflater: $inflater")
            }
        }
        mockCacheList.clear()
    }

    private fun preloadItem(context: Context, item: Any, index: Int, size: Int) {
        if (isCleared) {
            log("preloadItem: item:${item.javaClass.simpleName}, $index, isCleared, stop it")
            return
        }
        val timeStart = SystemClock.elapsedRealtimeNanos()
        val recyclerView = getRecyclerView(context) ?: return
        val viewType = viewTypeGetter.invoke(item)
        val inflater = inflaterPool.acquire() ?: ModuleAsyncInflater(context, inflaterIndex.incrementAndGet())
        moduleAsyncInflater.set(inflater)
        val view: View
        try {
            view = viewCreator.invoke(recyclerView, viewType)
        } finally {
            moduleAsyncInflater.set(null)
            inflaterPool.release(inflater)
        }
        val timeSpent = TimeRecorder.nanoToMillis(SystemClock.elapsedRealtimeNanos() - timeStart)
        log("preloadItem: item:${item.javaClass.simpleName}, view:${view.javaClass.simpleName}, $index, timeSpent: $timeSpent")
        putViewToCachePool(viewType, view, item, timeSpent, size)
    }

    @Synchronized
    private fun putViewToCachePool(viewType: Int, view: View, item: Any, timeSpent: String, poolSize: Int) {
        if (isCleared) {
            return
        }
        val holder = cacheViews.getOrPut(viewType) { Pools.SynchronizedPool<PreloadViewItem>(poolSize) }
        holder.release(PreloadViewItem(view, item, timeSpent))
    }

    override fun acquireView(viewType: Int): View? {
        val item = cacheViews[viewType]?.acquire()
        if (item != null) {
            preloadLogItems.add(PreloadLogItem(item.view.javaClass.simpleName, item.timeSpent))
        }
        return item?.view
    }

    override fun cleanAll() {
        isCleared = true
        cacheViews.clear()
        recyclerView = null
    }

    override fun getPreloadLogItems(): List<PreloadLogItem> {
        return ArrayList(preloadLogItems)
    }

    private fun log(msg: String) {
        delegate.logd("AsyncPreloadHandler $msg")
    }
}

private val moduleAsyncInflater = ThreadLocal<LayoutInflater>()

@Deprecated("")
fun obtainModuleAsyncInflater(context: Context): LayoutInflater {
    return moduleAsyncInflater.get() ?: ModuleAsyncInflater(context, -1)
}

class PreloadViewItem(val view: View, val item: Any, val timeSpent: String)
class PreloadLogItem(val viewName: String, val timeSpent: String) {
    override fun toString(): String {
        return "($viewName, $timeSpent)"
    }
}

interface IModulePreloadHandler {
    // 添加需要进行预加载view对应的item
    fun addItem(item: Any, size: Int): IModulePreloadHandler

    // 开始预加载
    fun preload(context: Context, lifecycleOwner: LifecycleOwner)

    // 获取view
    fun acquireView(viewType: Int): View?

    // 清除并停止view预加载
    fun cleanAll()

    // 获取缓存命中的信息
    fun getPreloadLogItems(): List<PreloadLogItem>
}

fun isOnMainThread(): Boolean {
    return Looper.getMainLooper().thread === Thread.currentThread()
}

private class ModuleAsyncInflater(context: Context, val index: Int) : LayoutInflater(context) {

    companion object {
        private val sClassPrefixList = arrayOf(
            "android.widget.",
            "android.webkit.",
            "android.app."
        )
    }

    override fun toString(): String {
        return "ModuleAsyncInflater@${hashCode().toString(16)}#$index"
    }

    override fun cloneInContext(newContext: Context): LayoutInflater {
        return ModuleAsyncInflater(newContext, index)
    }

    override fun onCreateView(name: String, attrs: AttributeSet): View {
        for (prefix in sClassPrefixList) {
            try {
                val view = createView(name, prefix, attrs)
                if (view != null) {
                    return view
                }
            } catch (e: ClassNotFoundException) {
                // In this case we want to let the base class take a crack
                // at it.
            }
        }
        return super.onCreateView(name, attrs)
    }

    init {
        if (context is AppCompatActivity) {
            val appCompatDelegate = context.delegate
            if (appCompatDelegate is Factory2) {
                LayoutInflaterCompat.setFactory2(this, (appCompatDelegate as Factory2))
            }
        }
    }
}
