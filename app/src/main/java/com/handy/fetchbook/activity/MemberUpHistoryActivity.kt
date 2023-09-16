package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.airsaid.pickerviewlibrary.TimePickerView
import com.blankj.utilcode.util.TimeUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.NormalModuleAdapter
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.view.MemberUpHistoryItemView
import com.handy.fetchbook.view.MyBuyWalletHistoryItemView
import com.handy.fetchbook.view.MyTimePickerView
import com.handy.fetchbook.viewModel.MemberUpHistoryViewModel
import com.handy.fetchbook.viewModel.MyBuyWalletHistoryViewModel
import kotlinx.android.synthetic.main.activity_my_buy_wallet_history_view.back
import kotlinx.android.synthetic.main.activity_my_buy_wallet_history_view.endContent
import kotlinx.android.synthetic.main.activity_my_buy_wallet_history_view.startDataContent
import kotlinx.android.synthetic.main.activity_my_buy_wallet_history_view.walletHistoryRv
import kotlinx.android.synthetic.main.activity_my_buy_wallet_history_view.walletQuery
import kotlinx.android.synthetic.main.fragment_home_v2.viewSmartLayout
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MemberUpHistoryActivity : BaseVmActivity<MemberUpHistoryViewModel>() {
    @SuppressLint("SimpleDateFormat")
    private val dft = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

    private val adapter = NormalModuleAdapter()
    private var startDate: String? = null
    private var endDate: String? = null
    private var currentPage = 1
    private var hasLoadMore = false

    override fun showLoading(message: String) {}

    override fun dismissLoading() {}

    override fun layoutId(): Int {
        return R.layout.member_up_history_activity
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewSmartLayout.isEnableRefresh = true
        viewSmartLayout.setDuRefreshListener {
            hasLoadMore = false
            currentPage = 1
            mViewModel.memberUpHistory(true, startDate.orEmpty(), endDate.orEmpty(), currentPage)
        }
        viewSmartLayout.setDuLoadMoreListener {
            mViewModel.memberUpHistory(false, startDate.orEmpty(), endDate.orEmpty(), currentPage)
        }
        startDate = getOldDate(-7)
        endDate = TimeUtils.getNowString(dft)
        BooKLogger.d("old = $startDate -> newDate = $endDate")
        startDataContent.text = startDate
        endContent.text = endDate
        back.setOnClickListener { finish() }
        mViewModel.memberUpHistory(true, startDate.orEmpty(), endDate.orEmpty(), currentPage)
        registerView()
        walletHistoryRv.itemAnimator = null
        walletHistoryRv.layoutManager = adapter.getGridLayoutManager(this)
        walletHistoryRv.adapter = adapter
        startDataContent.setOnClickListener {
            val timePick = MyTimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY)
            timePick.setCyclic(true)
            timePick.setRange(1949, 2040)
            timePick.setTime(startDate?.let { it1 -> dft.parse(it1) })
            timePick.show()
            timePick.setOnTimeSelectListener {
                startDate = dft.format(it)
                startDataContent.text = startDate
            }
        }
        endContent.setOnClickListener {
            val timePick = MyTimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY)
            timePick.setCyclic(true)
            timePick.setRange(1949, 2040)
            timePick.setTime(Date())
            timePick.show()
            timePick.setOnTimeSelectListener {
                endDate = dft.format(it)
                endContent.text = endDate
            }
        }
        walletQuery.setOnClickListener {
            hasLoadMore = false
            currentPage = 1
            mViewModel.memberUpHistory(true, startDate.orEmpty(), endDate.orEmpty(), currentPage)
        }
    }

    private fun registerView() {
        adapter.register { MemberUpHistoryItemView(it.context) }
    }

    override fun createObserver() {
        mViewModel.myBuyHistoryResult.observe(this) {
            parseMyState(it, onSuccess = { isRefresh, model ->
                val total = model.total
                if (isRefresh) walletHistoryRv.scrollToPosition(0)
                if (isRefresh) adapter.setItems(model.items ?: emptyList()) else adapter.appendItems(model.items ?: emptyList())
                BooKLogger.d("我的钱包购买记录接口成功-> total = $total -> size = ${model.items?.size}")
                hasLoadMore = adapter.getItems().size < model.total
                if (hasLoadMore) ++currentPage
                viewSmartLayout.isEnableLoadMore = hasLoadMore
                viewSmartLayout.onRefreshLoadMoreComplete(isRefresh, hasLoadMore)
            }, onError = { isRefresh, error ->
                BooKLogger.d("我的钱包购买记录失败-> ${error.errCode}")
                viewSmartLayout.onLoadMoreComplete(false)
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getOldDate(distanceDay: Int): String? {
        val beginDate = Date()
        val date = Calendar.getInstance()
        date.time = beginDate
        date[Calendar.DATE] = date[Calendar.DATE] + distanceDay
        var endDate: Date? = null
        try {
            endDate = dft.parse(dft.format(date.time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return endDate?.let { dft.format(it) }
    }

}