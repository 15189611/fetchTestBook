package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.airsaid.pickerviewlibrary.TimePickerView
import com.blankj.utilcode.util.TimeUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.NormalModuleAdapter
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.view.MyBuyWalletHistoryItemView
import com.handy.fetchbook.view.MyTimePickerView
import com.handy.fetchbook.viewModel.MyBuyWalletHistoryViewModel
import kotlinx.android.synthetic.main.activity_my_buy_wallet_history_view.*
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * - Author: Charles
 * - Date: 2023/9/2
 * - Description:
 */
class MyBuyWalletHistoryActivity : BaseVmActivity<MyBuyWalletHistoryViewModel>() {
    @SuppressLint("SimpleDateFormat")
    private val dft = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

    private val adapter = NormalModuleAdapter()
    private var startDate: String? = null
    private var endDate: String? = null

    override fun showLoading(message: String) {}

    override fun dismissLoading() {}

    override fun layoutId(): Int {
        return R.layout.activity_my_buy_wallet_history_view
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewSmartLayout.isEnableRefresh = false

        startDate = getOldDate(-5)
        endDate = TimeUtils.getNowString(dft)
        BooKLogger.d("old = $startDate -> newDate = $endDate")
        startDataContent.text = startDate
        endContent.text = endDate

        mViewModel.myBuyHistory(true, startDate.orEmpty(), endDate.orEmpty(), 1)
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
            mViewModel.myBuyHistory(true, startDate.orEmpty(), endDate.orEmpty(), 1)
        }
    }

    private fun registerView() {
        adapter.register { MyBuyWalletHistoryItemView(it.context) }
    }

    override fun createObserver() {
        mViewModel.myBuyHistoryResult.observe(this) {
            parseMyState(it, onSuccess = { isRefresh, model ->
                adapter.setItems(model.items ?: emptyList())
                BooKLogger.d("我的钱包购买记录接口成功-> ${model.items?.size}")
            }, onError = { isRefresh, error ->
                BooKLogger.d("我的钱包购买记录失败-> ${error.errCode}")
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