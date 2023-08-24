package com.handy.fetchbook.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.viewModels
import coil.load
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.network.ApiService
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.app.util.SpKey
import com.handy.fetchbook.data.bean.model.TokenInfoModel
import com.handy.fetchbook.databinding.MeActivityLoginBinding
import com.handy.fetchbook.net.Configuration
import com.handy.fetchbook.view.BlockPuzzleDialog
import com.handy.fetchbook.viewModel.state.LoginViewModel
import kotlinx.android.synthetic.main.home_activity_detail.back
import kotlinx.android.synthetic.main.me_activity_login.*
import me.hgj.jetpackmvvm.ext.parseState
import org.json.JSONObject

/**
 *
 * 登录activity
 * @author Handy
 * @since 2023/7/28 9:50 下午
 */
class LoginActivity : BaseActivity<LoginViewModel, MeActivityLoginBinding>() {

    var id: String = ""

    private val loginModel: LoginViewModel by viewModels()

    private var showPassword = true
    private val blockPuzzleDialog: BlockPuzzleDialog by lazy {
        BlockPuzzleDialog(this@LoginActivity)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.account.value = CacheUtil.getTokenInfo()?.userName.orEmpty()
        mViewModel.password.value = CacheUtil.getTokenInfo()?.userPassword.orEmpty()
        mDatabind.aetPwd.setText(mViewModel.password.value)
        mDatabind.aetAccount.setText(mViewModel.account.value)
    }

    override fun layoutId(): Int = R.layout.me_activity_login

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        //注册
        btn_to_reg.setOnClickListener {
            startActivity(Intent(this, RegActivity::class.java))
        }
        //登录
        btnLogin.setOnClickListener {
            mViewModel.account.value = mDatabind.aetAccount.text.toString()
            mViewModel.password.value = mDatabind.aetPwd.text.toString()
            try {
                blockPuzzleDialog.show()
            } catch (e: Exception) {
            }
        }
        //忘记登录密码
        login_forget.setOnClickListener {
            startActivity(Intent(this, ForgetPwdActivity::class.java))
            finish()
        }

        Configuration.server = Configuration.getServer(applicationContext, ApiService.SERVER_URL)
        blockPuzzleDialog.setOnResultsListener(object : BlockPuzzleDialog.OnResultsListener {
            override fun onResultsClick(result: String) {
                //todo 二次校验回调结果
                val s = result
                BooKLogger.d("LoginActivity pointJson result = $result -> token = ${Configuration.token}")
                mViewModel.token.value = Configuration.token
                mViewModel.pointJson.value = s
                mViewModel.login()
            }
        })

        mViewModel.loginResult.observe(this) { resultState ->
            parseState(resultState, { data ->
                val token = data.toString()
                BooKLogger.d("登录成功 = $data --> $data -> token = $token")
                CacheUtil.setIsLogin(true)
                CacheUtil.saveTokenInfo(TokenInfoModel(token, mViewModel.account.value, mViewModel.password.value))
                mViewModel.userinfo()
            }, {
                blockPuzzleDialog.dismiss()
                ToastUtils.showShort(it.message)
                BooKLogger.d("登录失败 = ${it.message.orEmpty()}")
            })
        }

        mViewModel.userinfoResult.observe(this) { resultState ->
            parseState(resultState, { user ->
                CacheUtil.saveUserInfo(user)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            })
        }

        aetPwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {

                } else {
                }
            }
        })

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
    }

    override fun onDestroy() {
        try {
            blockPuzzleDialog.dismiss()
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

}