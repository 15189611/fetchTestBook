package com.handy.fetchbook.fragment

import android.os.Bundle
import com.handy.fetchbook.R
import com.handy.fetchbook.viewModel.state.HomeViewModel
import me.hgj.jetpackmvvm.base.fragment.BaseVmFragment

/**
 * - Author: Charles
 * - Date: 2023/9/27
 * - Description: AI旅游fragment
 */
class AiTravelFragment : BaseVmFragment<HomeViewModel>() {

    companion object {
        fun newInstance(): AiTravelFragment {
            return AiTravelFragment()
        }
    }

    override fun showLoading(message: String) {}

    override fun dismissLoading() {}

    override fun layoutId(): Int {
        return R.layout.fragment_ai_travel_view
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {
    }

}