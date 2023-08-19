package com.handy.fetchbook.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.HomeDetailActivity
import com.handy.fetchbook.adapter.ScenicsAdapter
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.data.bean.home.Items
import com.handy.fetchbook.databinding.FragmentRegionBinding
import com.handy.fetchbook.viewModel.request.RequestRegionModel
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_region.*
import me.hgj.jetpackmvvm.ext.parseState


/**
 *
 * @author Handy
 * @since 2023/8/1 11:46 下午
 */
class RegionFragment : BaseFragment<HomeViewModel, FragmentRegionBinding>() {

    override fun layoutId(): Int = R.layout.fragment_region
    lateinit var region: String


    private var scenicsAdapter: ScenicsAdapter? = null
    private val requestHomeViewModel: RequestRegionModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdpter() {
        if (scenicsAdapter == null) {
            scenicsAdapter = ScenicsAdapter(R.layout.home_item_senics, null)
            vArticleRv.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = scenicsAdapter
            }

        } else {
            scenicsAdapter!!.notifyDataSetChanged()
        }
        scenicsAdapter!!.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->

                var intent = Intent(context, HomeDetailActivity::class.java)
                intent.putExtra("id", (adapter.data[position] as Items).uuid)
                startActivity(intent)
            }
    }

    override fun initView(savedInstanceState: Bundle?) {
        region = if (arguments != null) requireArguments().getString("text")
            .toString() else null.toString()
        initAdpter()
        requestHomeViewModel.scenics(1, region)
        requestHomeViewModel.secenicsResult.observe(this) { resultState ->
            parseState(resultState, {

                scenicsAdapter!!.setNewData(it.items)
            })
        }
    }

    companion object {
        fun newInstance(s: String): Fragment {
            val args = Bundle()
            args.putString("text", s)
            val fragment = RegionFragment()
            fragment.arguments = args
            return fragment
        }
    }

}