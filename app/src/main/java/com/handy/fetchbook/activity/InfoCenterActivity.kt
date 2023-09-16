package com.handy.fetchbook.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.databinding.HomeActivityInformationCenterBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.home_activity_information_center.*
import kotlinx.android.synthetic.main.me_activity_qr.back
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 消息中心列表
 */
class InfoCenterActivity : BaseActivity<HomeViewModel, HomeActivityInformationCenterBinding>() {
    override fun layoutId() = R.layout.home_activity_information_center
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        mViewModel.totalResult.observe(this) { resultState ->
            parseState(resultState, {
                if (it.announcement!! > 0) {
                    mDatabind.atvNotice.text = it.announcement.toString()
                } else {
                    mDatabind.atvNotice.visibility = View.GONE
                }

                if (it.system!! > 0) {
                    mDatabind.atvSystem.text = it.system.toString()
                } else {
                    mDatabind.atvSystem.visibility = View.GONE
                }
                if (it.message!! > 0) {
                    mDatabind.atvUser.text = it.message.toString()
                } else {
                    mDatabind.atvUser.visibility = View.GONE
                }
            })

        }
        crllSystem.setOnClickListener {
            startActivity(Intent(this, SystemInfoActivity::class.java))
        }
        crllNotice.setOnClickListener {
            startActivity(Intent(this, NoticeActivity::class.java))
        }

        crllUser.setOnClickListener {
            startActivity(Intent(this, UserMessageActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.total()
    }
}