package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.handy.fetchbook.R
import com.handy.fetchbook.app.App
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.databinding.ActivitySplashBinding
import com.handy.fetchbook.viewModel.state.MainViewModel

/**
 * 启动页
 *
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<MainViewModel, ActivitySplashBinding>() {
    override fun layoutId() = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {
        if (!isTaskRoot) {
            val intent = intent
            val action = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        mViewModel.getLanguageContent()
        mViewModel.languageContentResult.observe(this) {
            if (it == null) {
                startHomeActivity()
                return@observe
            }

            BooKLogger.d("获取语言接口成功")
            App.appViewModelInstance.setLanguageContent(it)
            startHomeActivity()
        }
    }

    private fun startHomeActivity() {
        val count = CacheUtil.getInt("first_page_finish", 0) ?: 0
        if (count >= 50) return
        CacheUtil.putInt("first_page_finish", count.plus(1))
        startActivity(Intent(this@SplashActivity, SplashActivity2::class.java))
        finish()
    }
}