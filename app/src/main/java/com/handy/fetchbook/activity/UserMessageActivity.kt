package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.SystemInfoAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.databinding.HomeActivitySociamediaBinding
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
class UserMessageActivity : BaseActivity<HomeViewModel, HomeActivitySystemInfoBinding>() {
    override fun layoutId() = R.layout.home_activity_user_message
    var page=1

    private lateinit var systemInfoAdapter: SystemInfoAdapter
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        systemInfoAdapter = SystemInfoAdapter(R.layout.item_system_info, null)
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = systemInfoAdapter
        }

        systemInfoAdapter.apply {
            setOnItemClickListener { adapter, view, position ->

            }

        }
        mViewModel.message(1,page)
        mViewModel.messageResult.observe(this){ resultState ->
            parseState(resultState, {
                systemInfoAdapter.setNewData(it.items)
            })
        }

    }

}