package com.handy.fetchbook.viewModel.state

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.data.bean.me.UserInfoBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class RegViewModel : BaseViewModel() {

    //注册Activity
    val account = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val code = MutableLiveData<String>("666666")
    val invite_code = MutableLiveData<String>("3000055")

    val regResult = MutableLiveData<ResultState<Any>>()
    var userinfoResult = MutableLiveData<ResultState<UserInfoBean>>()
    var sendCodeResult = MutableLiveData<ResultState<Any>>()

    //修改密码Activity
    val changeForGetCodeResult = MutableLiveData<ResultState<Any>>()
    val changePasswordResult = MutableLiveData<ResultState<Any>>()
    val changeCode = MutableLiveData<String>()
    val passWordNew = MutableLiveData<String>()
    val passWordNewConfirm = MutableLiveData<String>()

    //忘记密码Activity
    val forgetResult = MutableLiveData<ResultState<Any>>()
    val forgetSendCodeResult = MutableLiveData<ResultState<Any>>()
    val forgetAccount = MutableLiveData<String>()
    val forgetSendCode = MutableLiveData<String>()
    val forgetPwdNew = MutableLiveData<String>()
    val forgetPwdNewConfirm = MutableLiveData<String>()

    //获取用户信息
    fun userinfo() {
        request({ apiService.userInfo() }, userinfoResult)
    }

    //注册
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

    //发送注册短信
    fun sendCode() {
        request({ apiService.sendCode(account.value!!, "register") }, sendCodeResult)
    }

    //发送修改密码的短信
    fun sendCodeForget() {
        request({ apiService.sendCodeForget( "forget") }, changeForGetCodeResult)
    }

    //修改密码
    fun changePassword() {
        request({ apiService.changePassword(changeCode.value!!, passWordNew.value!!, passWordNewConfirm.value!!) }, changePasswordResult)
    }

    //发送忘记密码短信
    fun sendForgetCode(){
        request({ apiService.sendCode(forgetAccount.value!!, "forget") }, forgetSendCodeResult)
    }

    //忘记密码
    fun forgotPassword() {
        request(
            { apiService.forgotPassword(forgetAccount.value!!, forgetSendCode.value!!, forgetPwdNew.value!!, forgetPwdNewConfirm.value!!) }, forgetResult
        )
    }

}