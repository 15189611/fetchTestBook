package com.handy.fetchbook.viewModel

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.basic.ext.MyResultState
import com.handy.fetchbook.basic.ext.requestForFresh
import com.handy.fetchbook.data.bean.MyBuyHistoryBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * - Author: Charles
 * - Date: 2023/9/2
 * - Description:
 */
class MyBuyWalletHistoryViewModel : BaseViewModel() {

    val myBuyHistoryResult = MutableLiveData<MyResultState<MyBuyHistoryBean>>()

    fun myBuyHistory(isRefresh: Boolean, startData: String, endData: String, page: Int) {
        requestForFresh(
            { apiService.myBuyHistory(startData, endData, "invest", if (isRefresh) 1 else page) },
            myBuyHistoryResult,
            isRefresh = isRefresh
        )
    }
}