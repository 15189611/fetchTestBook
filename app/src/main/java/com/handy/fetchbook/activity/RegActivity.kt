package com.handy.fetchbook.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.viewModels
import coil.load
import com.blankj.utilcode.util.ToastUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.network.ApiService
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.constant.SpKey
import com.handy.fetchbook.databinding.MeActivityLoginBinding
import com.handy.fetchbook.databinding.MeActivityRegBinding
import com.handy.fetchbook.net.Configuration
import com.handy.fetchbook.utils.TimeCount
import com.handy.fetchbook.view.BlockPuzzleDialog
import com.handy.fetchbook.viewModel.state.LoginViewModel
import com.handy.fetchbook.viewModel.state.RegViewModel
import kotlinx.android.synthetic.main.home_activity_detail.back
import kotlinx.android.synthetic.main.me_activity_login.*
import kotlinx.android.synthetic.main.me_activity_login.aetAccount
import kotlinx.android.synthetic.main.me_activity_login.aetPwd
import kotlinx.android.synthetic.main.me_activity_login.aivEye
import kotlinx.android.synthetic.main.me_activity_reg.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 首页
 *
 * @author Handy
 * @since 2023/7/28 9:50 下午
 */
class RegActivity : BaseActivity<RegViewModel, MeActivityRegBinding>() {

    private var showPassword = true
    private var showPassword2 = true
    private var mTimeCount: TimeCount? = null


    override fun onResume() {
        super.onResume()

        mViewModel.account.value = SpUtils.getString(SpKey.LOGINNAME, "")
        mViewModel.password.value = SpUtils.getString(SpKey.LOGINPWD, "")

        mDatabind.aetPwd.setText(mViewModel.password.value)
        mDatabind.aetAccount.setText(mViewModel.account.value)
    }

    override fun layoutId(): Int = R.layout.me_activity_login

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

        back.setOnClickListener { finish() }
        sendCode.setOnClickListener {
            if (TextUtils.isEmpty(aetAccount.text.toString())) {
                ToastUtils.showShort(getString(R.string.me_reg_phone_hint))
                return@setOnClickListener
            }
            mViewModel.account.value = aetAccount.text.toString()
            mViewModel.sendCode()
        }

        btnReg.setOnClickListener {
            mViewModel.account.value = aetAccount.text.toString()
            mViewModel.code.value = aetCode.text.toString()
            mViewModel.password.value = aetPwd.text.toString()
            mViewModel.invite_code.value = aetrRecommendCode.text.toString()
            if (TextUtils.isEmpty(aetAccount.text.toString())) {
                ToastUtils.showShort(getString(R.string.me_reg_phone_hint))
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(aetCode.text.toString())) {
                ToastUtils.showShort(getString(R.string.me_reg_code_hint))
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(aetPwd.text.toString()) || TextUtils.isEmpty(aetPwd2.text.toString())) {
                ToastUtils.showShort(getString(R.string.me_tip_pwd))
                return@setOnClickListener
            }
            if (aetPwd.text.toString() != aetPwd2.text.toString()) {
                ToastUtils.showShort(getString(R.string.me_tip_inconsistent))
                return@setOnClickListener
            }

            mViewModel.register()
        }

        sendCode.setOnClickListener {
            mTimeCount = TimeCount(60000, 1000,sendCode) //第一个参数表示总时间，第二个参数表示时间间隔
            mTimeCount!!.start()

        }
    }


}