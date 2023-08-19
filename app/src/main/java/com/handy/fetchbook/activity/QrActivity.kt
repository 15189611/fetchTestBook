package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.constant.SpKey
import com.handy.fetchbook.databinding.ActivitySplashBinding
import com.handy.fetchbook.databinding.MeActivityQrBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.handy.fetchbook.viewModel.state.MainViewModel
import kotlinx.android.synthetic.main.me_activity_qr.*
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
class QrActivity : BaseActivity<HomeViewModel, MeActivityQrBinding>() {
    override fun layoutId() = R.layout.me_activity_qr

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
    }

}