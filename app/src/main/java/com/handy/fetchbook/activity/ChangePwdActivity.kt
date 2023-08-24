package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import coil.load
import com.blankj.utilcode.util.ToastUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.databinding.MeActivityChangePwdBinding
import com.handy.fetchbook.utils.TimeCount
import com.handy.fetchbook.viewModel.state.RegViewModel
import kotlinx.android.synthetic.main.me_activity_change_pwd.*
import kotlinx.android.synthetic.main.me_activity_qr.back
import me.hgj.jetpackmvvm.ext.parseState

/**
 *   修改密码
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
@SuppressLint("CustomSplashScreen")
class ChangePwdActivity : BaseActivity<RegViewModel, MeActivityChangePwdBinding>() {
    override fun layoutId() = R.layout.me_activity_change_pwd
    private var showPassword = true
    private var showPasswordNew = true
    private var showPasswordConfirm = true
    private var oldPassWord: String = ""
    private var newPassWord: String = ""
    private var confirmPassWord: String = ""
    private var mTimeCount: TimeCount? = null

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        sendCode.setOnClickListener {
            if (!checkPassWord()) return@setOnClickListener
            mViewModel.sendCodeForget()
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
            if (!checkPassWord()) return@setOnClickListener
            mViewModel.changeCode.value = aetCode.text.toString()
            if (newPassWord == confirmPassWord) {
                mViewModel.passWordNew.value = aetNewPwd.text.toString()
                mViewModel.passWordNewConfirm.value = aetConfirmPwd.text.toString()
                mViewModel.changePassword()
            } else {
                ToastUtils.showShort(getString(R.string.me_tip_inconsistent))
                return@setOnClickListener
            }
        }

        mViewModel.changePasswordResult.observe(this) {
            parseState(it, {
                BooKLogger.d("修改密码->成功")
                //成功之后，清空登录,重新去登录界面
                CacheUtil.clearUserAll()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, { errorData ->
                BooKLogger.d("修改密码->失败")
                ToastUtils.showShort(errorData.message)
            })
        }

        mViewModel.changeForGetCodeResult.observe(this) { it ->
            parseState(it, onSuccess = {
                BooKLogger.d("修改密码->发送验证码成功")
                ToastUtils.showShort(it.toString())
                mTimeCount = TimeCount(60000, 1000, sendCode) //第一个参数表示总时间，第二个参数表示时间间隔
                mTimeCount?.start()
            }, onError = { errorData ->
                if (errorData.errCode != 0) {
                    BooKLogger.d("修改密码->发送验证码失败 = ${errorData.message}")
                    ToastUtils.showShort(errorData.message)
                }
            })
        }
    }

    private fun checkPassWord(): Boolean {
        oldPassWord = aetPwd.text.toString()
        newPassWord = aetNewPwd.text.toString()
        confirmPassWord = aetConfirmPwd.text.toString()
        if (TextUtils.isEmpty(oldPassWord) || oldPassWord.length < 8 || oldPassWord.length > 20) {
            ToastUtils.showShort(getString(R.string.me_tip_pwd))
            return false
        }

        if (TextUtils.isEmpty(newPassWord) || newPassWord.length < 8 || newPassWord.length > 20) {
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