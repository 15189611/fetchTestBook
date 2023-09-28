package com.handy.fetchbook.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * ViewPager适配器
 *
 * @author Handy
 * @since 2023/8/2 12:41 上午
 */
class ViewPagerAdapter(fa: FragmentActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}