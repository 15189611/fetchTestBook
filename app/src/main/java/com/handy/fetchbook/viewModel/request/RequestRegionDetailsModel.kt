package com.handy.fetchbook.viewModel.request

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.data.bean.home.ScenicsDetailsBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 瀑布流详情的viewModel
 */
class RequestRegionDetailsModel : BaseViewModel() {

    var detailsResult = MutableLiveData<ResultState<ScenicsDetailsBean>>()
    var evaluateResult = MutableLiveData<ResultState<Any>>()

    fun details(id:String) {
        request({ apiService.details(id) }, detailsResult)
    }

    fun evaluate(id: String,type:Int) {
        request({ apiService.evaluate(id,type) }, evaluateResult)
    }


}