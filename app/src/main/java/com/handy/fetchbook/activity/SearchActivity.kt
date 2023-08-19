package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.SearchAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.data.bean.home.Items
import com.handy.fetchbook.data.bean.home.SearchBean
import com.handy.fetchbook.databinding.HomeActivitySearchBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.expo_activity.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_activity_search.*
import kotlinx.android.synthetic.main.home_activity_sociamedia.*
import kotlinx.android.synthetic.main.home_activity_sociamedia.rv
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
class SearchActivity : BaseActivity<HomeViewModel, HomeActivitySearchBinding>() {
    override fun layoutId() = R.layout.home_activity_search
    var page = 1

    private lateinit var searchAdapter: SearchAdapter
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        searchAdapter = SearchAdapter(R.layout.item_search, null)
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }

        searchAdapter.apply {
            setOnItemClickListener { adapter, view, position ->
                var intent = Intent(this@SearchActivity, HomeDetailActivity::class.java)
                intent.putExtra("id", (adapter.data[position] as SearchBean).uuid)
                startActivity(intent)

            }

        }
        search.setOnClickListener { getData() }

    }

    private fun getData() {
        mViewModel.searchScenic(etkey.text.toString(), page)
        mViewModel.searchScenicResult.observe(this) { resultState ->
            parseState(resultState, {
                searchAdapter.setNewData(it)
            })
        }
    }

}