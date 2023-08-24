package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.databinding.MeActivityQrBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.me_activity_qr.*

/**
 * 推荐QR ->activity
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class QrActivity : BaseActivity<HomeViewModel, MeActivityQrBinding>() {
    override fun layoutId() = R.layout.me_activity_qr

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
    }

}