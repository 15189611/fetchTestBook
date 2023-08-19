package com.handy.fetchbook.viewModel.state

import androidx.fragment.app.Fragment
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.fragment.DrawFragment
import com.handy.fetchbook.fragment.HomeFragment
import com.handy.fetchbook.fragment.HomeFragmentV2
import com.handy.fetchbook.fragment.MeFragment
import com.handy.fetchbook.fragment.TaskFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class MainViewModel : BaseViewModel() {

    /**
     * 子页面集合
     */
    val fragments: List<Fragment> = listOf(
            HomeFragmentV2(),
        DrawFragment(),
        TaskFragment(),
        MeFragment(),

    )
}