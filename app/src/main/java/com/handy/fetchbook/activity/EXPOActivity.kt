package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.RegionAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.data.bean.expo.Items
import com.handy.fetchbook.databinding.ExpoActivityBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.expo_activity.*
import kotlinx.android.synthetic.main.fragment_region.*
import kotlinx.android.synthetic.main.me_activity_qr.*
import kotlinx.android.synthetic.main.me_activity_qr.back
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class EXPOActivity : BaseActivity<HomeViewModel, ExpoActivityBinding>() {
    override fun layoutId() = R.layout.expo_activity
    private lateinit var regionAdapter: RegionAdapter

    /**
     * 当前已获取的文章页码
     */
    var mCurrPage = 1
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        regionAdapter = RegionAdapter(R.layout.expo_item, null)
        expoRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = regionAdapter
        }
        regionAdapter.apply {
            setOnItemClickListener { adapter, view, position ->

                var intent = Intent(this@EXPOActivity, EXPODetailActivity::class.java)
                intent.putExtra("id", (adapter.data[position] as Items).id)
                startActivity(intent)
            }

        }





        atvSearch.setOnClickListener {
            mViewModel.getExpoList(aetK.text.toString(), mCurrPage)
        }

        mViewModel.getExpoList(aetK.text.toString(), mCurrPage)

        mViewModel.getExpoListResult.observe(this) { resultState ->
            parseState(resultState, {
                regionAdapter.setNewData(it.data!!.items)
            })

        }
    }

}