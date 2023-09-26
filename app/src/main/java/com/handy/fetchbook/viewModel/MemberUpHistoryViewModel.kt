package com.handy.fetchbook.viewModel

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.basic.ext.MyResultState
import com.handy.fetchbook.basic.ext.requestForFresh
import com.handy.fetchbook.data.bean.MemberUpHistoryBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class MemberUpHistoryViewModel : BaseViewModel() {

    val myBuyHistoryResult = MutableLiveData<MyResultState<MemberUpHistoryBean>>()

    fun memberUpHistory(isRefresh: Boolean, startData: String, endData: String, page: Int) {
        requestForFresh(
            { apiService.memberUpHistory(startData, endData, "invest", if (isRefresh) 1 else page) },
            myBuyHistoryResult,
            isRefresh = isRefresh
        )
    }
}