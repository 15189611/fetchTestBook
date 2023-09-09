package com.handy.fetchbook.data.bean

/**
 * - Author: Charles
 * - Date: 2023/9/9
 * - Description:
 */
data class DrawPrizeListBean(
    val list: List<DrawPrizeItemBean>? = null
)

data class DrawPrizeItemBean(
    val uuid: String? = null,
    val name: String? = null,
    val image: String? = null,
    val position: String? = null,
    val category: String? = null
)