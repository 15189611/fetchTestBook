package com.handy.fetchbook.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.drake.statusbar.immersive
import com.handy.fetchbook.app.ext.dismissLoadingExt
import com.handy.fetchbook.app.ext.showLoadingExt
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.eventBus.BaseEvent
import com.handy.fetchbook.eventBus.EventVideoPlayOver

import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import org.greenrobot.eventbus.*

/**
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    abstract override fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        immersive()
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun baseEvent(event: BaseEvent) {
    }
}