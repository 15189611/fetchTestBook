package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.databinding.ActivitySplashBinding
import com.handy.fetchbook.viewModel.state.MainViewModel
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
class SplashActivity : BaseActivity<MainViewModel, ActivitySplashBinding>() {
    override fun layoutId() = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.Default) {
            delay(1500)
            ActivityUtils.startActivity(MainActivity::class.java)
            delay(200)
            finish()
        }
    }

}