package com.handy.fetchbook.basic.ext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.base.fragment.BaseVmFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.network.AppException
import me.hgj.jetpackmvvm.network.BaseResponse
import me.hgj.jetpackmvvm.network.ExceptionHandle

/**
 * - Author: Charles
 * - Date: 2023/8/19
 * - Description:
 */
fun <T> BaseViewModel.requestForError(
    block: suspend () -> BaseResponse<T>,
    resultState: MutableLiveData<MyResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) resultState.value = MyResultState.onAppLoading(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            resultState.paresResult(result = it)
        }.onFailure {
            it.message?.loge()
            //打印错误栈信息
            it.printStackTrace()
            resultState.paresException(e = it)
        }
    }
}

fun <T> BaseViewModel.requestForFresh(
    block: suspend () -> BaseResponse<T>,
    resultState: MutableLiveData<MyResultState<T>>,
    isRefresh: Boolean,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) resultState.value = MyResultState.onAppLoading(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            resultState.paresResult(isRefresh, it)
        }.onFailure {
            it.message?.loge()
            //打印错误栈信息
            it.printStackTrace()
            resultState.paresException(isRefresh, it)
        }
    }
}

sealed class MyResultState<out T> {
    companion object {
        fun <T> onAppSuccess(isRefresh: Boolean, data: T): MyResultState<T> =
            Success(isRefresh, data)

        fun <T> onAppLoading(loadingMessage: String): MyResultState<T> = Loading(loadingMessage)
        fun <T> onAppError(isRefresh: Boolean, error: AppException): MyResultState<T> =
            Error(isRefresh, error)
    }

    data class Loading(val loadingMessage: String) : MyResultState<Nothing>()
    data class Success<out T>(val isRefresh: Boolean, val data: T) : MyResultState<T>()
    data class Error(val isRefresh: Boolean, val error: AppException) : MyResultState<Nothing>()
}

/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<MyResultState<T>>.paresResult(isRefresh: Boolean = true, result: BaseResponse<T>) {
    value = when {
        result.isSucces() -> {
            MyResultState.onAppSuccess(isRefresh, result.getResponseData())
        }

        else -> {
            MyResultState.onAppError(
                isRefresh,
                AppException(
                    result.getResponseCode(),
                    result.getResponseMsg()
                )
            )
        }
    }
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<MyResultState<T>>.paresException(isRefresh: Boolean = true, e: Throwable) {
    this.value = MyResultState.onAppError(isRefresh, ExceptionHandle.handleException(e))
}

fun <T> BaseVmActivity<*>.parseMyState(
    resultState: MyResultState<T>,
    onSuccess: (Boolean, T) -> Unit,
    onError: ((Boolean, AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is MyResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }

        is MyResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.isRefresh, resultState.data)
        }

        is MyResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.isRefresh, resultState.error) }
        }
    }
}

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmFragment<*>.parseMyState(
    resultState: MyResultState<T>,
    onSuccess: (Boolean, T) -> Unit,
    onError: ((Boolean, AppException) -> Unit)? = null,
    onLoading: ((message: String) -> Unit)? = null
) {
    when (resultState) {
        is MyResultState.Loading -> {
            if (onLoading == null) {
                showLoading(resultState.loadingMessage)
            } else {
                onLoading.invoke(resultState.loadingMessage)
            }
        }

        is MyResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.isRefresh, resultState.data)
        }

        is MyResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.isRefresh, resultState.error) }
        }
    }
}

