package com.handy.fetchbook.app

import androidx.multidex.MultiDex
import com.blankj.utilcode.util.Utils
import com.drake.brv.utils.BRV
import com.handy.fetchbook.app.event.AppViewModel
import com.handy.fetchbook.basic.ext.DensityUtils
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.base.BaseApp
import java.util.*


//Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { com.handy.fetchbook.app.App.Companion.appViewModelInstance }

class App : BaseApp() {

    companion object {
        lateinit var instance: com.handy.fetchbook.app.App
        lateinit var appViewModelInstance: AppViewModel
    }

    override fun onCreate() {
        super.onCreate()

        MMKV.initialize(this)

        instance = this
        appViewModelInstance = getAppViewModelProvider()[AppViewModel::class.java]

        MultiDex.install(this)

        BRV.modelId = 2

        // 初始化 Toast 框架
        ToastUtils.init(this)
        DensityUtils.init(this)

        val appResources = Utils.getApp().resources
        val appConfig = appResources.configuration
        appConfig.setLocale(Locale("kr"))
        Utils.getApp().resources.updateConfiguration(appConfig, appResources.displayMetrics)
    }
}