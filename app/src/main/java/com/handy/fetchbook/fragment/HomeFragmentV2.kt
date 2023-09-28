package com.handy.fetchbook.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.*
import com.handy.fetchbook.adapter.ImageBannerAdapter
import com.handy.fetchbook.app.ext.languageSet
import com.handy.fetchbook.basic.VLModuleAdapter
import com.handy.fetchbook.basic.ext.dp
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.listener.OnViewRefreshLoadMoreListener
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.basic.vlayout.VLDelegateAdapter
import com.handy.fetchbook.basic.vlayout.VLVirtualLayoutManager
import com.handy.fetchbook.data.bean.home.Banner
import com.handy.fetchbook.model.home.*
import com.handy.fetchbook.view.home.*
import com.handy.fetchbook.viewModel.request.RequestHomeViewModel
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home_v2.*
import me.hgj.jetpackmvvm.base.fragment.BaseVmFragment
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 首页Fragment
 *
 */
class HomeFragmentV2 : BaseVmFragment<HomeViewModel>() {
    //请求tab的参数
    private val regions =
        mutableListOf("China", "Asia", "Oceania", "BirtgAmerical", "SouthAmerical")

    override fun layoutId(): Int = R.layout.fragment_home_v2

    override fun lazyLoadData() {}

    override fun showLoading(message: String) {}

    override fun createObserver() {}

    override fun dismissLoading() {}

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()
    private var vtLayoutManager: VLVirtualLayoutManager? = null
    private var defaultAdapter: VLDelegateAdapter? = null
    private val headerAdapter = VLModuleAdapter()
    private val contentAdapter = VLModuleAdapter()
    private val headerList = mutableListOf<Any>()

    private var currentPage = 1
    private var hasLoadMore = false
    private var currentRegion = regions.first()

    private fun getData() {
        requestHomeViewModel.banner()
        getProductData(true, currentPage, regions.first())
    }

    private fun registerView() {
        headerAdapter.register { HomeGoldView(it.context) }
        headerAdapter.register {
            HomeTabPagerView(it.context, listener = { position, model ->
                //点击tab刷新接口
                currentPage = 1
                hasLoadMore = false
                currentRegion = model.region.orEmpty()
                getProductData(true, currentPage, currentRegion)
            })
        }
        contentAdapter.register(gridSize = 2) { HomeProductItemView(it.context) }
    }

    override fun initView(savedInstanceState: Bundle?) {
        BooKLogger.d("HomeFragmentV2 initView")
        infoCenterRed.background = GradientDrawable().apply {
            setBounds(0, 0, 8.dp(), 8.dp())
            cornerRadius = 5.dp().toFloat()
            setColor(Color.RED)
        }
        getData()
        registerView()
        topViewClick()
        initAdapter()
        viewSmartLayout.setDuRefreshLoadMoreListener(object : OnViewRefreshLoadMoreListener() {
            override fun onRefreshLoadMore(isRefresh: Boolean, refreshLayout: RefreshLayout) {
                if (isRefresh) {
                    doRefresh(refreshLayout)
                } else {
                    doLoadMore(refreshLayout)
                }
            }
        })
        viewSmartLayout.isEnableLoadMore = false

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                onRecyclerViewScroll(recyclerView)
            }
        })
        oneStepToTop.rotation = 90f
        oneStepToTop.setOnClickListener {
            recyclerView.stopScroll()
            vtLayoutManager?.scrollToPositionWithOffset(0, 0)
        }
    }

    private var lastToTopVisible = false
    private fun onRecyclerViewScroll(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as? VLVirtualLayoutManager ?: return

        val findLastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        // 一键回顶View
        val otherCountSize = 2
        val isVisible = findLastVisiblePosition > 6 + otherCountSize
        if (isVisible != lastToTopVisible) {
            oneStepToTop.isVisible = isVisible
            lastToTopVisible = isVisible
        }
    }

    private fun topViewClick() {
        aivLang.setOnClickListener {
            languageSet(requireActivity(), aivLang)
        }

        crrlSearch.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }

        aivShare.setOnClickListener {
            startActivity(Intent(context, SocialMediaActivity::class.java))
        }
        //消息中心
        aivInfoCenter.setOnClickListener {
            startActivity(Intent(context, InfoCenterActivity::class.java))
        }
    }

    private fun initAdapter() {
        vtLayoutManager = VLVirtualLayoutManager(requireContext())
        defaultAdapter = VLDelegateAdapter(vtLayoutManager)
        defaultAdapter?.apply {
            addAdapter(headerAdapter)
            addAdapter(contentAdapter)
        }
        recyclerView.layoutManager = vtLayoutManager
        recyclerView.adapter = defaultAdapter
        recyclerView.itemAnimator = null
    }

    override fun initData() {
        BooKLogger.d("HomeFragmentV2 initData")
        super.initData()

        //红心
        mViewModel.totalResult.observe(this) { resultState ->
            parseState(resultState, { model ->
                val announcement = model.announcement ?: 0
                val message = model.message ?: 0
                val system = model.system ?: 0
                infoCenterRed.isVisible = announcement > 0 || message > 0 || system > 0
            }, {
                infoCenterRed.isVisible = false
            })
        }

        //banner
        requestHomeViewModel.bannerResult.observe(this) { resultState ->
            parseState(resultState, {
                vBanner.apply {
                    val numbers: MutableList<Banner> = it.banner.toMutableList()
                    //numbers.retainAll { !TextUtils.isEmpty(it.url) }
                    vBanner.setLoopTime(5000)
                    vBanner.addBannerLifecycleObserver(this@HomeFragmentV2)
                        .setAdapter(ImageBannerAdapter(requireActivity(), numbers)).indicator =
                        CircleIndicator(requireActivity())
                    vBanner.setIndicatorNormalColor(Color.DKGRAY)
                    vBanner.setIndicatorSelectedColor(Color.RED)

                    var showContent = ""
                    for (e in it.announcement) {
                        if (!TextUtils.isEmpty(e.title)) {
                            showContent = "$showContent${e.title}"
                        }
                    }
                    tv_notice.text = showContent
                    tv_notice.isSelected = true
                }
            })
        }

        //rv
        val tabList = mutableListOf<HomeTabPageItemModel>()
        val tabNames = mutableListOf(
            resources.getString(R.string.home_中国),
            resources.getString(R.string.home_亚洲),
            resources.getString(R.string.home_大洋洲),
            resources.getString(R.string.home_北美洲),
            resources.getString(R.string.home_南美洲)
        )
        for (i in 0..4) {
            tabList.add(HomeTabPageItemModel(tabNames[i], regions[i]))
        }
        headerList.clear()
        //headerList.add(HomeGoldViewModel())
        headerList.add(HomePagerViewModel(tabList))
        headerAdapter.setItems(headerList)

        //瀑布流数据
        requestHomeViewModel.secenicsResult.observe(this) {
            parseMyState(it, onSuccess = { isRefresh, model ->
                val items = model.items
                BooKLogger.d(" itemsSize = ${items.size} -> total = ${model.total}")
                if (isRefresh) contentAdapter.setItems(items) else contentAdapter.appendItems(items)

                hasLoadMore = contentAdapter.getItems().size < (model.total ?: 0)
                if (hasLoadMore) ++currentPage

                viewSmartLayout.isEnableLoadMore = hasLoadMore
                viewSmartLayout.onRefreshLoadMoreComplete(isRefresh, hasLoadMore)
                BooKLogger.d("hasLoadMore  = $hasLoadMore --> currentPage = $currentPage")
            }, onError = { isRefresh, _ ->
                if (isRefresh) viewSmartLayout.onRefreshComplete() else
                    viewSmartLayout.onLoadMoreComplete(false)
            })
        }
    }

    private fun doRefresh(refreshLayout: RefreshLayout) {
        currentPage = 1
        getProductData(true, currentPage, currentRegion)
    }

    private fun doLoadMore(refreshLayout: RefreshLayout) {
        getProductData(false, currentPage, currentRegion)
    }

    private fun getProductData(isRefresh: Boolean, page: Int, region: String) {
        requestHomeViewModel.scenics(isRefresh, page, region)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.total()
    }
}