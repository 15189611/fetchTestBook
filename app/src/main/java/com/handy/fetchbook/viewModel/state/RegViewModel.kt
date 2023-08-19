package com.handy.fetchbook.viewModel.state

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class RegViewModel : BaseViewModel() {

    val account = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val code = MutableLiveData<String>("666666")
    val invite_code = MutableLiveData<String>("3000001")


    /**
     * 注册成功
     */
    val isFinish = MutableLiveData<Boolean>()


    var regResult = MutableLiveData<ResultState<String>>()


    fun register() {
        request({
            apiService.register(
                account.value!!,
                password.value!!,
                code.value!!,
                invite_code.value!!
            )
        }, regResult)
    }

    var sendCodeResult = MutableLiveData<ResultState<Any>>()
    fun sendCode() {
        request({ apiService.sendCode(account.value!!, "register") }, sendCodeResult)
    }

    var changePasswordResult = MutableLiveData<ResultState<Any>>()
    fun changePassword() {
        request({ apiService.sendCode(code.value!!, password.value!!) }, changePasswordResult)
    }

}