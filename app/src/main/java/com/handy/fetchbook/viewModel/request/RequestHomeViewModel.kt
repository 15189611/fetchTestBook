package com.handy.fetchbook.viewModel.request

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.basic.ext.MyResultState
import com.handy.fetchbook.basic.ext.requestForFresh
import com.handy.fetchbook.data.bean.home.BannerBean
import com.handy.fetchbook.data.bean.home.ScenicsBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 首页Fragment的->ViewModel
 */
class RequestHomeViewModel : BaseViewModel() {

    var bannerResult = MutableLiveData<ResultState<BannerBean>>()

    var secenicsResult = MutableLiveData<MyResultState<ScenicsBean>>()

    fun banner() {
        request({ apiService.banner() }, bannerResult)
    }

    //瀑布流数据
    fun scenics(isRefresh: Boolean, page: Int, region: String) {
        requestForFresh({ apiService.scenics(page, region) }, secenicsResult, isRefresh = isRefresh)
    }
}