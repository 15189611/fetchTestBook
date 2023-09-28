package com.handy.fetchbook.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import coil.load
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.model.TokenInfoModel
import com.handy.fetchbook.databinding.MeActivityRegBinding
import com.handy.fetchbook.utils.TimeCount
import com.handy.fetchbook.viewModel.state.RegViewModel
import kotlinx.android.synthetic.main.me_activity_login.aetAccount
import kotlinx.android.synthetic.main.me_activity_login.aetPwd
import kotlinx.android.synthetic.main.me_activity_login.aivEye
import kotlinx.android.synthetic.main.me_activity_reg.*
import me.hgj.jetpackmvvm.ext.parseState
import org.json.JSONObject

/**
 * 注册activity
 *
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
class RegActivity : BaseActivity<RegViewModel, MeActivityRegBinding>() {

    private var showPassword = true
    private var showPassword2 = true
    private var mTimeCount: TimeCount? = null

    override fun onResume() {
        super.onResume()
    }

    override fun layoutId(): Int = R.layout.me_activity_reg

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.back.setOnClickListener { finish() }
        mDatabind.btnToReg.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        mDatabind.aivEye.setOnClickListener {
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
        mDatabind.aivEye2.setOnClickListener {
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
        mDatabind.sendCode.setOnClickListener {
            if (TextUtils.isEmpty(aetAccount.text.toString())) {
                ToastUtils.showShort(getString(R.string.me_reg_phone_hint))
                return@setOnClickListener
            }
            mViewModel.account.value = aetAccount.text.toString()
            mViewModel.sendCode()
        }

        mDatabind.btnReg.setOnClickListener {
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
            if (aetrRecommendCode.text.toString().isEmpty()) {
                ToastUtils.showShort(getString(R.string.me_reg_recommend_code_hint))
                return@setOnClickListener
            }

            mViewModel.account.value = aetAccount.text.toString()
            mViewModel.code.value = aetCode.text.toString()
            mViewModel.password.value = aetPwd.text.toString()
            mViewModel.invite_code.value = aetrRecommendCode.text.toString()
            mViewModel.register()
        }

        mViewModel.regResult.observe(this) {
            parseState(it, onSuccess = { data ->
                val jsonObject = JSONObject(GsonUtils.toJson(data))
                val token = jsonObject.optString("token") ?: return@parseState
                BooKLogger.d("注册成功 = $data --> $data -> token = $token")
                CacheUtil.setIsLogin(true)
                CacheUtil.saveTokenInfo(TokenInfoModel(token, mViewModel.account.value, mViewModel.password.value))
                mViewModel.userinfo()
            }, onError = { errorData ->
                if (errorData.errCode != 0) {
                    BooKLogger.d("注册失败 = ${errorData.message}")
                    ToastUtils.showShort(errorData.message ?: "邀请码不存在")
                }
            })
        }

        mViewModel.userinfoResult.observe(this) { resultState ->
            parseState(resultState, { user ->
                CacheUtil.saveUserInfo(user)
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            })
        }

        mViewModel.sendCodeResult.observe(this) { it ->
            parseState(it, onSuccess = {
                BooKLogger.d("发送验证码成功")
                ToastUtils.showShort(it.toString())
                mTimeCount = TimeCount(60000, 1000, sendCode) //第一个参数表示总时间，第二个参数表示时间间隔
                mTimeCount?.start()
            }, onError = { errorData ->
                if (errorData.errCode != 0) {
                    BooKLogger.d("发送验证码失败 = ${errorData.message}")
                    ToastUtils.showShort(errorData.message)
                }
            })
        }
    }

}