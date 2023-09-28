package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.NormalModuleAdapter
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.home.NoticeItems
import com.handy.fetchbook.view.NoticeItemView
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.hjq.toast.ToastUtils
import kotlinx.android.synthetic.main.home_activity_notice.*
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 公告信息Activity
 */
@SuppressLint("CustomSplashScreen")
class NoticeActivity : BaseVmActivity<HomeViewModel>() {
    override fun layoutId() = R.layout.home_activity_notice
    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {
    }

    private var currentPage = 1
    private var hasLoadMore = false
    private val adapter = NormalModuleAdapter()
    private var lastPosition = 0
    private var lastClickModel: NoticeItems? = null

    override fun initView(savedInstanceState: Bundle?) {
        getData(true)
        back.setOnClickListener { finish() }
        viewSmartLayout.isEnableRefresh = false
        viewSmartLayout.setDuLoadMoreListener {
            getData(false)
        }
        registerView()
        rv.itemAnimator = null
        rv.layoutManager = adapter.getGridLayoutManager(this)
        rv.adapter = adapter
    }

    private fun registerView() {
        adapter.register {
            NoticeItemView(it.context) { position, model ->
                mViewModel.noticeRead(model.uuid.toString() ?: "0")
                lastPosition = position
                lastClickModel = model
            }
        }
    }

    private fun getData(isRefresh: Boolean) {
        mViewModel.announcements(isRefresh, currentPage)
    }

    override fun createObserver() {
        mViewModel.noticeReadResult.observe(this) { model ->
            parseState(model, {
                BooKLogger.d("公告更新状态接口成功 = $it")
                if (it.code == 0) {
                    lastClickModel?.readStatus = 1
                    adapter.notifyItemChanged(lastPosition)
                } else {
                    ToastUtils.show(it.message)
                }
            }, onError = {
                BooKLogger.d("公告更新状态接口失败 = ${it.message}")
            })
        }

        mViewModel.noticeResult.observe(this) { resultState ->
            parseMyState(resultState, { isRefresh, model ->
                val total = model.total ?: 0
                if (isRefresh) rv.scrollToPosition(0)
                if (isRefresh) adapter.setItems(model.items) else adapter.appendItems(model.items)
                BooKLogger.d("公告接口成功-> total = $total -> size = ${model.items.size} -> isRefresh = $isRefresh")
                hasLoadMore = adapter.getItems().size < total
                if (hasLoadMore) ++currentPage
                viewSmartLayout.isEnableLoadMore = hasLoadMore
                viewSmartLayout.onRefreshLoadMoreComplete(isRefresh, hasLoadMore)
            }, onError = { isRefresh, error ->
                BooKLogger.d("公告接口失败-> ${error.message}")
                viewSmartLayout.onLoadMoreComplete(false)
            })
        }
    }

}