package com.handy.fetchbook.basic.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
fun LifecycleOwner.postDelayed(delayInMillis: Long = 0L, block: () -> Unit): Job {
    return lifecycleScope.launch {
        if (delayInMillis > 0) {
            delay(delayInMillis)
        }
        block()
    }
}

/**
 * - Fragment获取ViewLifecycleOwner,可空，不crash
 */
fun Fragment.safeViewLifecycleOwner(): LifecycleOwner? = viewLifecycleOwnerLiveData.value

/**
 * 在onAttach后添加lifecycle，默认使用View自身来当LifecycleObserver，也可以传入Lifecycle
 * appcompat需要到1.3才能使用
 */
//fun View.addToViewTreeLifecycleOwner(observer: LifecycleObserver? = if (this is LifecycleObserver) this else null) {
//    if (observer == null) {
//        error("argument observer must not be null or View must be implement LifecycleObserver")
//    }
//    doOnAttach {
//        val lifecycleOwner = findViewTreeLifecycleOwner()
//        if (lifecycleOwner == null) {
//            DuLogger.bug("can not find lifecycleOwner from view:$this")
//        } else {
//            lifecycleOwner.lifecycle.addObserver(observer)
//        }
//    }
//}

fun View.findViewLifecycleOwner(): LifecycleOwner? {
    val lifecycleOwner = findViewTreeLifecycleOwner()
    return lifecycleOwner ?: safeAppCompatActivity()
}

val LifecycleOwner.isCreated: Boolean
    get() = lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)

val LifecycleOwner.isStarted: Boolean
    get() = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

val LifecycleOwner.isResumed: Boolean
    get() = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

val LifecycleOwner.isDestroyed: Boolean
    get() = lifecycle.currentState == Lifecycle.State.DESTROYED


typealias LifecycleEventCallback = (LifecycleOwner) -> Unit
typealias LifecycleAllEventCallback = (LifecycleOwner, Lifecycle.Event) -> Unit

/**
 * Lifecycle生命周期的监听
 */
fun Lifecycle.doOnLifecycle(
        onCreate: LifecycleEventCallback? = null,
        onStart: LifecycleEventCallback? = null,
        onResume: LifecycleEventCallback? = null,
        onPause: LifecycleEventCallback? = null,
        onStop: LifecycleEventCallback? = null,
        onDestroy: LifecycleEventCallback? = null,
        onEvent: LifecycleAllEventCallback? = null
) {
    addObserver(LifecycleEventObserver { source, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate?.invoke(source)
            Lifecycle.Event.ON_START -> onStart?.invoke(source)
            Lifecycle.Event.ON_RESUME -> onResume?.invoke(source)
            Lifecycle.Event.ON_PAUSE -> onPause?.invoke(source)
            Lifecycle.Event.ON_STOP -> onStop?.invoke(source)
            Lifecycle.Event.ON_DESTROY -> onDestroy?.invoke(source)
            else -> Unit
        }
        onEvent?.invoke(source, event)
    })
}

fun LifecycleOwner.doOnLifecycle(
        onCreate: LifecycleEventCallback? = null,
        onStart: LifecycleEventCallback? = null,
        onResume: LifecycleEventCallback? = null,
        onPause: LifecycleEventCallback? = null,
        onStop: LifecycleEventCallback? = null,
        onDestroy: LifecycleEventCallback? = null,
        onEvent: LifecycleAllEventCallback? = null
) {
    lifecycle.doOnLifecycle(onCreate, onStart, onResume, onPause, onStop, onDestroy, onEvent)
}

fun LifecycleOwner.doOnCreate(onCreate: LifecycleEventCallback) {
    doOnLifecycle(onCreate = onCreate)
}

fun LifecycleOwner.doOnStart(onStart: LifecycleEventCallback) {
    doOnLifecycle(onStart = onStart)
}

fun LifecycleOwner.doOnResume(onResume: LifecycleEventCallback) {
    doOnLifecycle(onResume = onResume)
}

fun LifecycleOwner.doOnPause(onPause: LifecycleEventCallback) {
    doOnLifecycle(onPause = onPause)
}

fun LifecycleOwner.doOnStop(onStop: LifecycleEventCallback) {
    doOnLifecycle(onStop = onStop)
}

fun LifecycleOwner.doOnDestroy(onDestroy: LifecycleEventCallback) {
    doOnLifecycle(onDestroy = onDestroy)
}

/**
 * 只会响应一次指定事件
 */
private fun Lifecycle.doOnEventOnce(
        onNextEvent: LifecycleEventCallback? = null,
        nextEvent: Lifecycle.Event
) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == nextEvent) {
                removeObserver(this)
                onNextEvent?.invoke(source)
            }
        }
    })
}

/**
 * 只会响应一次onResume事件
 */
fun LifecycleOwner.doOnResumeOnce(
        onResume: LifecycleEventCallback? = null,
) {
    lifecycle.doOnEventOnce(onResume, Lifecycle.Event.ON_RESUME)
}

