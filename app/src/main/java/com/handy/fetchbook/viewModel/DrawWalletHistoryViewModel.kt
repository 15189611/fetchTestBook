package com.handy.fetchbook.viewModel

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.basic.ext.MyResultState
import com.handy.fetchbook.basic.ext.requestForFresh
import com.handy.fetchbook.data.bean.MyBuyHistoryBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * - Author: Charles
 * - Date: 2023/9/9
 * - Description:
 */
class DrawWalletHistoryViewModel : BaseViewModel() {

    val myDrawHistoryResult = MutableLiveData<MyResultState<MyBuyHistoryBean>>()

    fun myDrawHistory(isRefresh: Boolean, startData: String, endData: String, page: Int) {
        requestForFresh(
            { apiService.myBuyHistory(startData, endData, "invest", if (isRefresh) 1 else page, "static") },
            myDrawHistoryResult,
            isRefresh = isRefresh
        )
    }
}