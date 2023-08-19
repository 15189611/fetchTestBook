package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.databinding.MeActivityWalletBalanceBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.me_activity_wallet.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class WalletBalanceActivity : BaseActivity<HomeViewModel, MeActivityWalletBalanceBinding>() {
    override fun layoutId() = R.layout.me_activity_wallet_balance

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        mViewModel.wallet()
        mViewModel.walletResult.observe(this){ resultState ->
            parseState(resultState, {
                mDatabind.atvMoney.text = it.invest
            })
        }
    }

}