package com.handy.fetchbook.basic.ext

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */

inline fun Int.dp(): Int {
    return DensityUtils.dip2px(this.toFloat())
}

inline fun Float.dp(): Int {
    return DensityUtils.dip2px(this)
}