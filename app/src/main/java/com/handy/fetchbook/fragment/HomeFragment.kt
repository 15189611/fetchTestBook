package com.handy.fetchbook.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.*
import com.handy.fetchbook.adapter.ImageBannerAdapter
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.app.ext.languageSet
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.app.util.SpKey
import com.handy.fetchbook.data.bean.home.Banner
import com.handy.fetchbook.databinding.FragmentHomeBinding
import com.handy.fetchbook.viewModel.request.RequestHomeViewModel
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*
import me.hgj.jetpackmvvm.ext.parseState


/**
 * 首页Fragment
 *
 * @author Handy
 * @since 2023/8/1 11:46 下午
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun layoutId(): Int = R.layout.fragment_home

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()


    private val activeColor: Int = Color.parseColor("#333333")
    private val normalColor: Int = Color.parseColor("#666666")

    private val activeSize = 14f
    private val normalSize = 14f

    private var mediator: TabLayoutMediator? = null


    private var activityPop: PopupWindow? = null
    private var activityPopView: View? = null


    private fun showActiviytFinsh() {

        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        activityPopView =
            inflater.inflate(R.layout.home_dialog_readme, null)
        activityPop = PopupWindow(
            activityPopView,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )


        activityPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            activityPop!!.dismiss()
        }
        activityPop!!.setBackgroundDrawable(BitmapDrawable())
        activityPop!!.animationStyle = R.style.common_CustomDialog
        activityPop!!.showAsDropDown(mDatabind.noLogin)
    }

    override fun initView(savedInstanceState: Bundle?) {
        getData()
        aivLang.setOnClickListener {
            languageSet(aivLang)
        }

        crrlSearch.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }

        aivShare.setOnClickListener {
            startActivity(Intent(context, SocialMediaActivity::class.java))
        }
        aivInfoCenter.setOnClickListener {
            startActivity(Intent(context, InfoCenterActivity::class.java))
        }
        atvNoBtn.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        noLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        val tabs = arrayOf(
            resources.getString(R.string.home_中国),
            resources.getString(R.string.home_亚洲),
            resources.getString(R.string.home_大洋洲),
            resources.getString(R.string.home_北美洲),
            resources.getString(R.string.home_南美洲)
        )
        val regions = arrayOf("China", "Asia", "Oceania", "BirtgAmerical", "SouthAmerical")
        //禁用预加载
        viewPager2?.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        viewPager2.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        viewPager2.adapter = object :
            FragmentStateAdapter(childFragmentManager, lifecycle) {
            override fun createFragment(position: Int): Fragment {
                //FragmentStateAdapter内部自己会管理已实例化的fragment对象。
                // 所以不需要考虑复用的问题
                return RegionFragment.newInstance(regions[position])
            }

            override fun getItemCount(): Int {
                return tabs.size
            }
        }
        //viewPager 页面切换监听监听
        viewPager2.registerOnPageChangeCallback(changeCallback)

        mediator = TabLayoutMediator(
            mDatabind.tabLayout, viewPager2
        ) { tab, position -> //这里可以自定义TabView
            val tabView = TextView(context)
            val states = arrayOfNulls<IntArray>(2)
            states[0] = intArrayOf(android.R.attr.state_selected)
            states[1] = intArrayOf()
            val colors = intArrayOf(activeColor, normalColor)
            val colorStateList = ColorStateList(states, colors)
            tabView.text = tabs[position]
            tabView.textSize = normalSize
            tabView.setTextColor(colorStateList)
            tabView.gravity = Gravity.CENTER
            tab.customView = tabView
        }
        //要执行这一句才是真正将两者绑定起来
        mediator!!.attach()



        crllItem1.setOnClickListener {

            showActiviytFinsh()

        }

        crllItem2.setOnClickListener {
            var intent = Intent(context, H5Activity::class.java)
            intent.putExtra("url", "http://www.baidu.com")
            startActivity(intent)
        }
        crllItem3.setOnClickListener {
            var intent = Intent(context, EXPOActivity::class.java)
            startActivity(intent)
        }
        crllItem4.setOnClickListener {
            var intent = Intent(context, HelpCenterActivity::class.java)
            startActivity(intent)
        }
        crllItem5.setOnClickListener {
            var intent = Intent(context, H5Activity::class.java)
            intent.putExtra("url", "http://www.baidu.com")
            startActivity(intent)
        }

        crllItem6.setOnClickListener {
            var intent = Intent(context, MemberUpgradeActivity::class.java)
            startActivity(intent)
        }

        requestHomeViewModel.bannerResult.observe(this) { resultState ->
            parseState(resultState, {
                vBanner.apply {
                    val numbers: MutableList<Banner> = it.banner.toMutableList()
                    numbers.retainAll { !TextUtils.isEmpty(it.url) }
                    vBanner.setLoopTime(5000)
                    vBanner.addBannerLifecycleObserver(this@HomeFragment)
                        .setAdapter(ImageBannerAdapter(requireActivity(), numbers)).indicator =
                        CircleIndicator(requireActivity())
                    vBanner.setIndicatorNormalColor(Color.DKGRAY)
                    vBanner.setIndicatorSelectedColor(Color.RED)


                    var showContent = ""
                    for (e in it.announcement) {
                        if (!TextUtils.isEmpty(e.title)) {
                            showContent = "$showContent${e.title}       "
                        }
                    }
                    mDatabind.tvNotice.text = showContent
                    mDatabind.tvNotice.isSelected = true
                }
            })
        }
    }

    fun getData() {
        requestHomeViewModel.banner()
    }

    override fun onResume() {
        super.onResume()
        if (!TextUtils.isEmpty(SpUtils.getString(SpKey.TOKEN, ""))) {
            mDatabind.atvNoBtn.visibility = View.INVISIBLE
        }
    }

    private val changeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //可以来设置选中时tab的大小
                val tabCount: Int = mDatabind.tabLayout.tabCount
                for (i in 0 until tabCount) {
                    val tab: TabLayout.Tab? = mDatabind.tabLayout.getTabAt(i)
                    val tabView = tab?.customView as TextView?
                    if (tab != null) {
                        if (tab.position == position) {
                            tabView!!.textSize = activeSize
                            tabView.typeface = Typeface.DEFAULT_BOLD
                        } else {
                            tabView!!.textSize = normalSize
                            tabView.typeface = Typeface.DEFAULT
                        }
                    }
                }
            }
        }

}