package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.lifecycle.lifecycleScope
import coil.load
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.constant.SpKey
import com.handy.fetchbook.databinding.ActivitySplashBinding
import com.handy.fetchbook.databinding.MeActivityChangePwdBinding
import com.handy.fetchbook.databinding.MeActivityQrBinding
import com.handy.fetchbook.utils.TimeCount
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.handy.fetchbook.viewModel.state.MainViewModel
import com.handy.fetchbook.viewModel.state.RegViewModel
import kotlinx.android.synthetic.main.me_activity_change_pwd.*
import kotlinx.android.synthetic.main.me_activity_qr.*
import kotlinx.android.synthetic.main.me_activity_qr.back
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class ChangePwdActivity : BaseActivity<RegViewModel, MeActivityChangePwdBinding>() {
    override fun layoutId() = R.layout.me_activity_change_pwd
    private var showPassword = true
    private var showPasswordNew = true
    private var showPasswordConfirm = true

    private var mTimeCount: TimeCount? = null
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        sendCode.setOnClickListener {
            mViewModel.sendCode()
            mTimeCount = TimeCount(60000, 1000,sendCode) //第一个参数表示总时间，第二个参数表示时间间隔
            mTimeCount!!.start()

        }

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

        aivNewEye.setOnClickListener {
            if (showPasswordNew) {// 显示密码
                aetNewPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                aetNewPwd.text?.let { it1 -> aetNewPwd.setSelection(it1.length) }
                showPasswordNew = !showPasswordNew
                aivNewEye.load(R.drawable.common_eye_n)
            } else {// 隐藏密码
                aetNewPwd.transformationMethod = PasswordTransformationMethod.getInstance();
                aetNewPwd.text?.let { it1 -> aetNewPwd.setSelection(it1.length) }
                showPasswordNew = !showPasswordNew
                aivNewEye.load(R.drawable.common_eye_y)
            }
        }
        aivConfirmEye.setOnClickListener {
            if (showPasswordConfirm) {// 显示密码
                aetConfirmPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                aetConfirmPwd.text?.let { it1 -> aetConfirmPwd.setSelection(it1.length) }
                showPasswordConfirm = !showPasswordConfirm
                aivConfirmEye.load(R.drawable.common_eye_n)
            } else {// 隐藏密码
                aetConfirmPwd.transformationMethod = PasswordTransformationMethod.getInstance();
                aetConfirmPwd.text?.let { it1 -> aetConfirmPwd.setSelection(it1.length) }
                showPasswordConfirm = !showPasswordConfirm
                aivConfirmEye.load(R.drawable.common_eye_y)
            }
        }



        btnChange.setOnClickListener {
            mViewModel.password.value = aetConfirmPwd.text.toString()
            var oldpwd = aetPwd.text.toString()
            var newpwd = aetNewPwd.text.toString()
            var confirmpwd = aetConfirmPwd.text.toString()
            if (TextUtils.isEmpty(oldpwd) || oldpwd.length <= 8 || oldpwd.length >= 20) {
                ToastUtils.showShort(getString(R.string.me_tip_pwd))
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(newpwd) || newpwd.length <= 8 || newpwd.length >= 20) {
                ToastUtils.showShort(getString(R.string.me_tip_pwd))
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(confirmpwd) || confirmpwd.length <= 8 || confirmpwd.length >= 20) {
                ToastUtils.showShort(getString(R.string.me_tip_pwd))
                return@setOnClickListener
            }

            if (newpwd == confirmpwd) {
                mViewModel.changePassword()
            }else{
                ToastUtils.showShort(getString(R.string.me_tip_inconsistent))
                return@setOnClickListener
            }
        }
    }

}