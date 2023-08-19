package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.SocialMediaAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.databinding.HomeActivitySociamediaBinding
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
class SocialMediaActivity : BaseActivity<HomeViewModel, HomeActivitySociamediaBinding>() {
    override fun layoutId() = R.layout.home_activity_sociamedia

    private lateinit var socialMediaAdapter: SocialMediaAdapter
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        socialMediaAdapter = SocialMediaAdapter(R.layout.expo_item, null)
        rv.apply {
            layoutManager = GridLayoutManager(context,3)
            adapter = socialMediaAdapter
        }

        socialMediaAdapter.apply {
            setOnItemClickListener { adapter, view, position ->

            }

        }
        mViewModel.socialMedia()
        mViewModel.socialMediaResult.observe(this){ resultState ->
            parseState(resultState, {
                socialMediaAdapter.setNewData(it)
            })
        }

    }

}