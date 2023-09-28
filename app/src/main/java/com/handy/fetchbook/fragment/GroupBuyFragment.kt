package com.handy.fetchbook.fragment

import android.content.Intent
import android.os.Bundle
import androidx.core.view.updatePadding
import com.blankj.utilcode.util.BarUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.MemberUpHistoryActivity
import com.handy.fetchbook.basic.NormalModuleAdapter
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.view.GroupBuyItemView
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.fragment_group_buy_view.*
import kotlinx.android.synthetic.main.fragment_tab_expo_view.viewSmartLayout
import me.hgj.jetpackmvvm.base.fragment.BaseVmFragment
import me.hgj.jetpackmvvm.ext.parseState

/**
 * - Author: Charles
 * - Date: 2023/9/27
 * - Description: 拼团fragment
 */
class GroupBuyFragment : BaseVmFragment<HomeViewModel>() {

    companion object {
        fun newInstance(): GroupBuyFragment {
            return GroupBuyFragment()
        }
    }

    private var currentPage = 1
    private var hasLoadMore = false
    private val adapter = NormalModuleAdapter()

    override fun showLoading(message: String) {}

    override fun dismissLoading() {}

    override fun layoutId(): Int {
        return R.layout.fragment_group_buy_view
    }

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.updatePadding(top = BarUtils.getStatusBarHeight())
        viewSmartLayout.isEnableRefresh = false
        viewSmartLayout.setDuLoadMoreListener {
            getGroupListData(false)
        }
        registerView()
        groupBuyRv.adapter = adapter
        groupBuyRv.itemAnimator = null
        groupBuyRv.layoutManager = adapter.getGridLayoutManager(requireContext())
        memberHistoryClick.setOnClickListener {
            startActivity(Intent(requireContext(), MemberUpHistoryActivity::class.java))
        }
    }

    private fun registerView() {
        adapter.register { GroupBuyItemView(requireContext()) }
    }

    override fun lazyLoadData() {
        mViewModel.wallet()
        getGroupListData(true)
    }

    private fun getGroupListData(isRefresh: Boolean) {
        mViewModel.grouplist(isRefresh, currentPage)
    }

    override fun createObserver() {
        mViewModel.walletResult.observe(this) { resultState ->
            parseState(resultState, {
                atvMoney.text = it.invest
            })
        }

        mViewModel.groupSharingBean.observe(this) { resultState ->
            parseMyState(resultState, { isRefresh, model ->
                val items = model.items
                val total = model.total
                if (isRefresh) {
                    adapter.setItems(items)
                } else {
                    adapter.appendItems(items)
                }
                hasLoadMore = adapter.getItems().size < total
                if (hasLoadMore) ++currentPage
                viewSmartLayout.isEnableLoadMore = hasLoadMore
                viewSmartLayout.onRefreshLoadMoreComplete(isRefresh, hasLoadMore)
            }, onError = { isRefresh, error ->
                BooKLogger.d("拼团列表接口获取失败 = ${error.message}")
                viewSmartLayout.onLoadMoreComplete(false)
            })
        }
    }

}