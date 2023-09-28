package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.HelpCenterAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.data.bean.me.HelpCenterBean
import com.handy.fetchbook.databinding.MeActivityHelpCenterBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.me_activity_help_center.*
import kotlinx.android.synthetic.main.me_activity_qr.back
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class HelpCenterActivity : BaseActivity<HomeViewModel, MeActivityHelpCenterBinding>() {
    override fun layoutId() = R.layout.me_activity_help_center
    private lateinit var helpCenterAdapter: HelpCenterAdapter
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        helpCenterAdapter = HelpCenterAdapter(R.layout.me_item_help_center, null)
        rvHelpCenter.apply {
            layoutManager = GridLayoutManager(this@HelpCenterActivity, 3)
            adapter = helpCenterAdapter
        }
        // 文章适配器
        helpCenterAdapter.apply {
            setOnItemClickListener { adapter, view, position ->
                var intent = Intent(this@HelpCenterActivity, VideoPlayActivity::class.java)
                intent.putExtra("thumbnail", (adapter.data[position] as HelpCenterBean).thumbnail)
                intent.putExtra("url", (adapter.data[position] as HelpCenterBean).video)
                intent.putExtra("title", (adapter.data[position] as HelpCenterBean).title)
                startActivity(intent)
            }
        }






        mViewModel.helpCenter()

        mViewModel.helpCenterResult.observe(this) { resultState ->
            parseState(resultState, {
                helpCenterAdapter.setNewData(it)
            })
        }
    }

}