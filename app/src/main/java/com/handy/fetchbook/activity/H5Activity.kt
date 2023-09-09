package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.handy.fetchbook.R
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.expo_activity_h5.back
import kotlinx.android.synthetic.main.expo_activity_h5.webview
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class H5Activity : BaseVmActivity<HomeViewModel>() {
    override fun layoutId() = R.layout.expo_activity_h5
    override fun showLoading(message: String) {}
    override fun dismissLoading() {}

    var url: String? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        url = intent.getStringExtra("url")


        webview.settings.javaScriptEnabled = true
        url?.let { webview.loadUrl(it) }
        webview.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    override fun createObserver() {
    }

}