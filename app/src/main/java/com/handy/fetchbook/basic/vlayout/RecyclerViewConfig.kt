package com.handy.fetchbook.basic.vlayout

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
object RecyclerViewConfig {
    private var errorConfig: ErrorConfig? = null
    fun setErrorConfig(config: ErrorConfig) {
        this.errorConfig = config
    }

    fun handleCatchError(throwable: Throwable) {
        this.errorConfig?.invoke(throwable)

    }
}
typealias ErrorConfig = (throwable: Throwable) -> Unit