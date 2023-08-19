package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.NoticeAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.databinding.HomeActivityNoticeBinding
import com.handy.fetchbook.databinding.HomeActivitySystemInfoBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.expo_activity.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_activity_sociamedia.*
import kotlinx.android.synthetic.main.me_activity_wallet.*
import kotlinx.android.synthetic.main.me_activity_wallet.back
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class NoticeActivity : BaseActivity<HomeViewModel, HomeActivityNoticeBinding>() {
    override fun layoutId() = R.layout.home_activity_notice
    var page=1

    private lateinit var noticeAdapter: NoticeAdapter
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        noticeAdapter = NoticeAdapter(R.layout.item_notice, null)
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noticeAdapter
        }

        noticeAdapter.apply {
            setOnItemClickListener { adapter, view, position ->

            }

        }
        mViewModel.announcements(page)
        mViewModel.noticeResult.observe(this){ resultState ->
            parseState(resultState, {
                noticeAdapter.setNewData(it.items)
            })
        }

    }

}