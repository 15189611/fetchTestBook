package com.handy.fetchbook.viewModel.request

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.data.bean.home.ScenicsBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 描述　:
 */
class RequestRegionModel : BaseViewModel() {

    var secenicsResult = MutableLiveData<ResultState<ScenicsBean>>()

    fun scenics(page: Int, region: String) {
        request({ apiService.scenics(page, region) }, secenicsResult)
    }



}