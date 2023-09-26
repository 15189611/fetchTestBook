package com.handy.fetchbook.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView
import coil.load
import com.blankj.utilcode.util.ToastUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.utils.TimeCount
import com.handy.fetchbook.viewModel.state.RegViewModel
import kotlinx.android.synthetic.main.activity_forget_pwd_view.*
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.ext.parseState

/**
 *  忘记密码activity
 * - Author: Charles
 * - Date: 2023/8/24
 * - Description:
 */
class ForgetPwdActivity : BaseVmActivity<RegViewModel>() {
    private var account: String = ""
    private var passWord: String = ""
    private var confirmPassWord: String = ""
    private var showPassword = true
    private var showPassword2 = true
    private var mTimeCount: TimeCount? = null

    override fun showLoading(message: String) {}
    override fun dismissLoading() {}

    override fun layoutId(): Int {
        return R.layout.activity_forget_pwd_view
    }

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        aivEye.setOnClickListener {
            if (showPassword) {// 显示密码
                aetPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                aetPwd.text?.let { it1 -> aetPwd.setSelection(it1.length) }
                showPassword = !showPassword
                aivEye.load(R.drawable.common_eye_n)
            } else {// 隐藏密码
                aetPwd.transformationMethod = PasswordTransformationMethod.getInstance();
                aetPwd.text?.let { it1 -> aetPwd.setSelection(it1.length) }
                showPassword = !showPassword
                aivEye.load(R.drawable.common_eye_y)
            }
        }

        aivEye2.setOnClickListener {
            if (showPassword2) {// 显示密码
                aetPwd2.transformationMethod = HideReturnsTransformationMethod.getInstance()
                aetPwd2.text?.let { it1 -> aetPwd2.setSelection(it1.length) }
                showPassword2 = !showPassword2
                aivEye2.load(R.drawable.common_eye_n)
            } else {// 隐藏密码
                aetPwd2.transformationMethod = PasswordTransformationMethod.getInstance();
                aetPwd2.text?.let { it1 -> aetPwd2.setSelection(it1.length) }
                showPassword2 = !showPassword2
                aivEye2.load(R.drawable.common_eye_y)
            }
        }

        sendCode.setOnClickListener {
            val account = aetAccount.text.toString()
            if (account.isEmpty()) {
                ToastUtils.showShort(getString(R.string.forget_activity_input_account))
                return@setOnClickListener
            }
            mViewModel.forgetAccount.value = aetAccount.text.toString()
            mViewModel.sendForgetCode()
        }
        btnChange.setOnClickListener {
            if (!checkPassWord()) return@setOnClickListener
            mViewModel.forgetAccount.value = aetAccount.text.toString()
            mViewModel.forgetSendCode.value = aetCode.text.toString()
            mViewModel.forgetPwdNew.value = aetPwd.text.toString()
            mViewModel.forgetPwdNewConfirm.value = aetPwd2.text.toString()
            mViewModel.forgotPassword()
        }
    }

    override fun createObserver() {
        mViewModel.forgetSendCodeResult.observe(this) { it ->
            parseState(it, onSuccess = {
                BooKLogger.d("忘记密码->发送验证码成功")
                ToastUtils.showShort(it.toString())
                mTimeCount = TimeCount(60000, 1000, sendCode) //第一个参数表示总时间，第二个参数表示时间间隔
                mTimeCount?.start()
            }, onError = { errorData ->
                if (errorData.errCode != 0) {
                    BooKLogger.d("忘记密码->发送验证码失败 = ${errorData.message}")
                    ToastUtils.showShort(errorData.message)
                }
            })
        }
        mViewModel.forgetResult.observe(this) {
            parseState(it, {
                //showDialog
                BooKLogger.d("忘记密码重置成功")
                showSuccessDialog()
            }, { errorData ->
                BooKLogger.d("忘记密码重置失败 = ${errorData.message}")
                ToastUtils.showShort(errorData.message)
            })
        }
    }

    private var activityPop: PopupWindow? = null
    private fun showSuccessDialog() {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.home_dialog_forget_success, null)
        dialogView.findViewById<AppCompatTextView>(R.id.startLogin).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        activityPop = PopupWindow(dialogView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true)
        activityPop?.setBackgroundDrawable(null)
        activityPop?.isFocusable = true
        activityPop?.isOutsideTouchable = true
        activityPop?.animationStyle = R.style.common_CustomDialog
        activityPop?.showAsDropDown(forgetPwdParent)
    }

    private fun checkPassWord(): Boolean {
        account = aetAccount.text.toString()
        passWord = aetPwd.text.toString()
        confirmPassWord = aetPwd2.text.toString()
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShort(getString(R.string.forget_activity_input_account))
            return false
        }

        if (TextUtils.isEmpty(passWord) || passWord.length < 8 || passWord.length > 20) {
            ToastUtils.showShort(getString(R.string.me_tip_pwd))
            return false
        }

        if (TextUtils.isEmpty(confirmPassWord) || confirmPassWord.length < 8 || confirmPassWord.length > 20) {
            ToastUtils.showShort(getString(R.string.me_tip_pwd))
            return false
        }
        return true
    }

}