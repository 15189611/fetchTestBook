package com.handy.fetchbook.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.handy.fetchbook.R
import com.handy.fetchbook.app.App
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.databinding.ActivitySplash2Binding
import com.handy.fetchbook.databinding.ActivitySplashBinding
import com.handy.fetchbook.viewModel.state.MainViewModel
import kotlinx.android.synthetic.main.activity_splash2.cd_view3
import kotlinx.android.synthetic.main.activity_splash2.video

class SplashActivity2 : BaseActivity<MainViewModel, ActivitySplash2Binding>() {
    override fun layoutId() = R.layout.activity_splash2

    override fun initView(savedInstanceState: Bundle?) {
        video.setVideoURI(Uri.parse("android.resource://"+packageName +"/"+ R.raw.video))
        video.start()
        cd_view3.start()
        cd_view3.setOnLoadingFinishListener {
            startHomeActivity()
        }

    }



    private fun startHomeActivity() {
        startActivity(Intent(this@SplashActivity2, MainActivity::class.java))
        finish()
    }
}