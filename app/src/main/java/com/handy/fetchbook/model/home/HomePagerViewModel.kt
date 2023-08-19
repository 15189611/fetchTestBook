package com.handy.fetchbook.model.home

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
data class HomePagerViewModel(val tabList: List<HomeTabPageItemModel>? = null)

data class HomeTabPageItemModel(
        val tabName: String? = null,
        val region: String? = null
)