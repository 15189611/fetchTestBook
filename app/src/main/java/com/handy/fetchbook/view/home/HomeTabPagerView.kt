package com.handy.fetchbook.view.home

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.AbsModuleView
import com.handy.fetchbook.model.home.HomePagerViewModel
import com.handy.fetchbook.model.home.HomeTabPageItemModel
import kotlinx.android.synthetic.main.home_item_tab_pager_view.view.tabLayout

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
class HomeTabPagerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    var listener: ((Int, HomeTabPageItemModel) -> Unit)? = null
) : AbsModuleView<HomePagerViewModel>(context, attrs) {

    private var currentTabList: List<HomeTabPageItemModel>? = null
    private val activeColor: Int = Color.parseColor("#333333")
    private val normalColor: Int = Color.parseColor("#666666")
    private val activeSize = 14f
    private val normalSize = 14f

    override fun getLayoutId(): Int {
        return R.layout.home_item_tab_pager_view
    }

    override fun onChanged(model: HomePagerViewModel) {
        super.onChanged(model)
        val tabList = model.tabList ?: emptyList()
        if (tabList.isEmpty()) return
        currentTabList = tabList
        for (itemData in tabList) {
            val newTab = tabLayout.newTab()
            val tabView = TextView(context)
            val states = arrayOfNulls<IntArray>(2)
            states[0] = intArrayOf(android.R.attr.state_selected)
            states[1] = intArrayOf()
            val colors = intArrayOf(activeColor, normalColor)
            val colorStateList = ColorStateList(states, colors)
            tabView.textSize = normalSize
            tabView.text = itemData.tabName.orEmpty()
            tabView.setTextColor(colorStateList)
            tabView.gravity = Gravity.CENTER
            newTab.customView = tabView
            tabLayout.addTab(newTab)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab ?: return
                val list = currentTabList ?: return
                val position = tab.position
                listener?.invoke(position, list[position])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}