package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.KeyboardUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.VLModuleAdapter
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.basic.vlayout.VLDelegateAdapter
import com.handy.fetchbook.basic.vlayout.VLVirtualLayoutManager
import com.handy.fetchbook.view.*
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.expo_activity.*
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.ext.parseState

/**
 * expoActivity
 *
 */
@SuppressLint("CustomSplashScreen")
class EXPOActivity : BaseVmActivity<HomeViewModel>() {
    override fun layoutId() = R.layout.expo_activity
    override fun showLoading(message: String) {}
    override fun dismissLoading() {
    }

    private var vtLayoutManager: VLVirtualLayoutManager? = null
    private var defaultAdapter: VLDelegateAdapter? = null
    private val headAdapter = VLModuleAdapter()
    private val adapter = VLModuleAdapter()
    private val headerList = mutableListOf<Any>()

    private var currentPage = 1
    private var hasLoadMore = false

    private var hasSearch = false
    private var lastSearchKey: String? = null
    private fun initAdapter() {
        vtLayoutManager = VLVirtualLayoutManager(this)
        defaultAdapter = VLDelegateAdapter(vtLayoutManager)
        defaultAdapter?.apply {
            addAdapter(headAdapter)
            addAdapter(adapter)
        }
        expoRv.layoutManager = vtLayoutManager
        expoRv.adapter = defaultAdapter
        expoRv.itemAnimator = null
    }

    override fun initView(savedInstanceState: Bundle?) {
        getExpoBanner()
        back.setOnClickListener { finish() }
        viewSmartLayout.isEnableRefresh = false
        viewSmartLayout.setDuLoadMoreListener {
            if (hasSearch) {
                getExpoSearchList(false, lastSearchKey.orEmpty())
            } else {
                getExpoList(false)
            }
        }
        registerView()
        initAdapter()
        getExpoList(true)
    }

    private fun registerView() {
        //注册banner
        headAdapter.register { ExpoBannerView(it.context) }
        //搜索view
        headAdapter.register {
            ExpoSearchView(it.context) { keyWord ->
                lastSearchKey = keyWord
                currentPage = 1
                getExpoSearchList(true, keyWord)
                KeyboardUtils.hideSoftInput(this)
                hasSearch = true
            }
        }
        adapter.register {
            ExpoItemView(it.context) { position, model ->
                val intent = Intent(this@EXPOActivity, EXPODetailActivity::class.java)
                intent.putExtra("id", model.id)
                startActivity(intent)
            }
        }
    }

    private fun getExpoSearchList(isRefresh: Boolean, keyWord: String) {
        mViewModel.expoSearchList(isRefresh, keyWord, currentPage)
    }

    private fun getExpoBanner() {
        mViewModel.getExpoBanner()
    }

    private fun getExpoList(isRefresh: Boolean) {
        mViewModel.getExpoList(isRefresh, "", currentPage)
    }

    override fun createObserver() {
        mViewModel.getExpoBannerResult.observe(this) {
            parseState(it, { model ->
                BooKLogger.d("expo列表获取banner成功")
                if (adapter.getItems().isEmpty()) {
                    headerList.clear()
                    headerList.add(ExpoBannerModel())
                    headerList.add(ExpoSearchModel())
                    headAdapter.setItems(headerList)
                }
            }, { error ->
                headerList.clear()
                headerList.add(ExpoBannerModel())
                headerList.add(ExpoSearchModel())
                BooKLogger.d("expo列表获取banner失败= ${error.message}")
            })
        }

        mViewModel.getExpoListResult.observe(this) { resultState ->
            parseMyState(resultState, { isRefresh, model ->
                val data = model.data ?: return@parseMyState
                val total = data.total ?: 0
                if (isRefresh) expoRv.scrollToPosition(0)
                if (isRefresh) {
                    adapter.setItems(data.items)
                } else {
                    adapter.appendItems(data.items)
                }
                BooKLogger.d("expo列表接口获取成功-> total = $total -> size = ${data.items.size} -> isRefresh = $isRefresh")
                hasLoadMore = adapter.getItems().size < total
                if (hasLoadMore) ++currentPage
                viewSmartLayout.isEnableLoadMore = hasLoadMore
                viewSmartLayout.onRefreshLoadMoreComplete(isRefresh, hasLoadMore)
            }, onError = { _, error ->
                BooKLogger.d("expo列表接口获取失败 = ${error.message}")
                viewSmartLayout.onLoadMoreComplete(false)
            })
        }

        //搜索成功的结果
        mViewModel.expoSearchListResult.observe(this) { resultState ->
            parseMyState(resultState, { isRefresh, model ->
                BooKLogger.d("expo搜索接口成功 = ${model.items}")
                val total = model.total ?: 0
                if (isRefresh) {
                    expoRv.scrollToPosition(0)
                    adapter.clearItems()
                }
                if (isRefresh) {
                    adapter.setItems(model.items)
                } else {
                    adapter.appendItems(model.items)
                }
                hasLoadMore = adapter.getItems().size < total
                if (hasLoadMore) ++currentPage
                viewSmartLayout.isEnableLoadMore = hasLoadMore
                viewSmartLayout.onRefreshLoadMoreComplete(isRefresh, hasLoadMore)
            }, onError = { isRefresh, error ->
                BooKLogger.d("expo搜索接口失败 = ${error.message}")
                viewSmartLayout.onLoadMoreComplete(false)
            })
        }
    }

}