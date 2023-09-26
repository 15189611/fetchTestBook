package com.handy.fetchbook.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.ViewPagerAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.databinding.ActivityMainBinding
import com.handy.fetchbook.viewModel.state.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 首页
 *
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun layoutId(): Int = R.layout.activity_main

    /**
     * ViewPager适配器
     */
    private val mVPAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewPagerAdapter(this, mViewModel.fragments)
    }

    companion object {
        const val INDEX_HOME = 0
        const val INDEX_DRAW = 1
        const val INDEX_TASK = 2
        const val INDEX_ME = 3
    }

    override fun initView(savedInstanceState: Bundle?) {
        // 底部导航栏设置
        vBottomNavigationView.run {
            itemIconTintList = null
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.vNavHome -> vViewPager.setCurrentItem(INDEX_HOME, false)
                    R.id.vNavTask -> vViewPager.setCurrentItem(INDEX_TASK, false)
                    R.id.vNavDraw -> vViewPager.setCurrentItem(INDEX_DRAW, false)
                    R.id.vNavMe -> vViewPager.setCurrentItem(INDEX_ME, false)
                }
                true
            }
        }
        // ViewPager2设置
        (vViewPager.getChildAt(0) as? RecyclerView)?.itemAnimator = null
        vViewPager.setPageTransformer(null)
        vViewPager.isUserInputEnabled = false
        vViewPager.run {
            adapter = mVPAdapter
            offscreenPageLimit = 4
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val menu = vBottomNavigationView.menu
                    when (position) {
                        INDEX_HOME -> menu.getItem(INDEX_HOME).isChecked = true
                        INDEX_TASK -> menu.getItem(INDEX_TASK).isChecked = true
                        INDEX_DRAW -> menu.getItem(INDEX_DRAW).isChecked = true
                        INDEX_ME -> menu.getItem(INDEX_ME).isChecked = true
                    }
                }
            })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

}