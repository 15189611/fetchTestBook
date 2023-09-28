package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.handy.fetchbook.R
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.basic.NormalModuleAdapter
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.home.SystemInfoItems
import com.handy.fetchbook.view.SystemInfoItemView
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.hjq.toast.ToastUtils
import kotlinx.android.synthetic.main.fragment_home_v2.viewSmartLayout
import kotlinx.android.synthetic.main.home_activity_system_info.*
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.requestNoCheck

/**
 * 系统信息Activity
 */
@SuppressLint("CustomSplashScreen")
class SystemInfoActivity : BaseVmActivity<HomeViewModel>() {
    override fun layoutId() = R.layout.home_activity_system_info
    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {}

    private var currentPage = 1
    private var hasLoadMore = false
    private val adapter = NormalModuleAdapter()
    private var lastClickModel: SystemInfoItems? = null
    private var lastPosition = 0

    override fun initView(savedInstanceState: Bundle?) {
        getData(true)
        viewSmartLayout.isEnableRefresh = false
        viewSmartLayout.setDuLoadMoreListener {
            getData(false)
        }
        back.setOnClickListener { finish() }
        allCleanMessage.setOnClickListener {
            //清空所有传"system"
            mViewModel.requestNoCheck({ apiService.messageRead("system") }, success = {
                BooKLogger.d("清空所有 = $it")
            })
        }

        registerView()
        rv.itemAnimator = null
        rv.layoutManager = adapter.getGridLayoutManager(this)
        rv.adapter = adapter
    }

    private fun getData(isRefresh: Boolean) {
        mViewModel.message(isRefresh, 0, currentPage)
    }

    private fun registerView() {
        adapter.register {
            SystemInfoItemView(it.context) { position, model ->
                mViewModel.messageRead(model.id.toString() ?: "0")
                lastClickModel = model
                lastPosition = position
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserver() {
        mViewModel.messageReadResult.observe(this) { model ->
            parseState(model, {
                BooKLogger.d("点击读取接口成功 = $it")
                if (it.code == 0) {
                    lastClickModel?.status = 1
                    adapter.notifyItemChanged(lastPosition)
                } else {
                    ToastUtils.show(it.message)
                }
            }, onError = {
                BooKLogger.d("点击读取接口失败 = ${it.message}")
            })
        }

        mViewModel.messageResult.observe(this) { resultState ->
            parseMyState(resultState, { isRefresh, model ->
                val total = model.total ?: 0
                if (isRefresh) rv.scrollToPosition(0)
                if (isRefresh) adapter.setItems(model.items) else adapter.appendItems(model.items)
                BooKLogger.d("系统信息接口成功-> total = $total -> size = ${model.items.size} -> isRefresh = $isRefresh")
                hasLoadMore = adapter.getItems().size < total
                if (hasLoadMore) ++currentPage
                viewSmartLayout.isEnableLoadMore = hasLoadMore
                viewSmartLayout.onRefreshLoadMoreComplete(isRefresh, hasLoadMore)
            }, onError = { isRefresh, error ->
                BooKLogger.d("系统信息接口失败-> ${error.message}")
                viewSmartLayout.onLoadMoreComplete(false)
            })
        }
    }

}