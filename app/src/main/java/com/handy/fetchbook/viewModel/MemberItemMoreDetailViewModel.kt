package com.handy.fetchbook.viewModel

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.basic.ext.MyResultState
import com.handy.fetchbook.basic.ext.requestForFresh
import com.handy.fetchbook.data.bean.MemberItemDetailBean
import com.handy.fetchbook.data.bean.me.WalletBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class MemberItemMoreDetailViewModel : BaseViewModel() {

    val myBuyHistoryResult = MutableLiveData<MyResultState<MemberItemDetailBean>>()

    fun memberItemDetail(package_uid: String) {
        requestForFresh(
            { apiService.memberItemDetail(package_uid) },
            myBuyHistoryResult,
        )
    }

    var walletResult = MutableLiveData<ResultState<WalletBean>>()
    fun wallet() {
        request({ apiService.wallet() }, walletResult)
    }
}