package com.handy.fetchbook.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.viewModels
import coil.load
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.network.ApiService
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.constant.SpKey
import com.handy.fetchbook.databinding.MeActivityLoginBinding
import com.handy.fetchbook.net.Configuration
import com.handy.fetchbook.view.BlockPuzzleDialog
import com.handy.fetchbook.viewModel.state.LoginViewModel
import kotlinx.android.synthetic.main.home_activity_detail.back
import kotlinx.android.synthetic.main.me_activity_login.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 首页
 *
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

        mViewModel.account.value = SpUtils.getString(SpKey.LOGINNAME, "")
        mViewModel.password.value = SpUtils.getString(SpKey.LOGINPWD, "")

        mDatabind.aetPwd.setText(mViewModel.password.value)
        mDatabind.aetAccount.setText(mViewModel.account.value)
    }

    override fun layoutId(): Int = R.layout.me_activity_login

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        Configuration.server = Configuration.getServer(applicationContext, ApiService.SERVER_URL)
        btn_to_reg.setOnClickListener {
        }
        blockPuzzleDialog.setOnResultsListener(object : BlockPuzzleDialog.OnResultsListener {
            override fun onResultsClick(result: String) {
                //todo 二次校验回调结果
                val s = result
                BooKLogger.d("pointJson result = $result -> token = ${Configuration.token}")
                mViewModel.token.value = Configuration.token
                mViewModel.pointJson.value = s
                mViewModel.login()
            }
        })
        btnLogin.setOnClickListener {
            mViewModel.account.value = mDatabind.aetAccount.text.toString()
            mViewModel.password.value = mDatabind.aetPwd.text.toString()
            try {
                blockPuzzleDialog.show()
            } catch (e: Exception) {
            }

        }

        mViewModel.loginResult.observe(this) { resultState ->
            parseState(resultState, { it ->
                CacheUtil.setIsLogin(true)
                SpUtils.put(SpKey.TOKEN, it)
                SpUtils.put(SpKey.LOGINNAME, mViewModel.account.value!!)
                SpUtils.put(SpKey.LOGINPWD, mViewModel.password.value!!)
                mViewModel.userinfo()
            })

        }

        mViewModel.userinfoResult.observe(this) { resultState ->
            parseState(resultState, { user ->
                SpUtils.put(SpKey.USER_TYPE, user.type!!)
                SpUtils.put(SpKey.USER_LEVEL, user.level!!)
                SpUtils.put(SpKey.USER_LUCKY_TICKET, user.luckyTicket!!)
                SpUtils.put(SpKey.USER_LUCKY_BAG, user.luckyBag!!)
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