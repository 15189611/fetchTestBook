package com.handy.fetchbook.app.network

/**
 * - Author: Charles
 * - Date: 2023/9/1
 * - Description:
 */

fun postBodyOf(vararg pairs: Pair<String, Any?>): PostJsonBody {
    val params = ParamsBuilder.newParams()
    for ((key, value) in pairs) {
        if (value != null) {
            params.addParams(key, value)
        }
    }
    return PostJsonBody.create(params)
}

fun postBodyOf(map: Map<String, Any>): PostJsonBody {
    val params = ParamsBuilder.newParams()
    params.addParams(map)
    return PostJsonBody.create(params)
}