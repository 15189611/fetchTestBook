package com.handy.fetchbook.viewModel.state

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.app.util.LanUtil
import com.handy.fetchbook.data.bean.me.UserInfoBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class LoginViewModel : BaseViewModel() {


    val account = MutableLiveData("test001@qq.com")
    val password = MutableLiveData("aa123456")
    val token = MutableLiveData("123")
    val pointJson = MutableLiveData("123123")


    var loginResult = MutableLiveData<ResultState<String>>()


    fun login() {
        request({ apiService.login(account.value!!,password.value!!,token.value!!,pointJson.value!!) }, loginResult,
            true, LanUtil.getText("正在登录中…")
        )
    }

    var userinfoResult = MutableLiveData<ResultState<UserInfoBean>>()
    fun userinfo() {
        request({ apiService.userInfo() }, userinfoResult)
    }

}