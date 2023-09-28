package com.handy.fetchbook.basic.util

import android.util.Log

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
object BooKLogger {
    const val TAG = "Charles"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    fun w(msg: String, e: Throwable? = null) {
        Log.w(TAG, msg)
    }

    fun e(msg: String, e: Throwable? = null) {
        Log.e(TAG, msg)
    }

}