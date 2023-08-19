package com.handy.fetchbook.activity

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.FeedbackAdapter
import com.handy.fetchbook.adapter.ImageBannerAdapter
import com.handy.fetchbook.adapter.ImageStringAdapter
import com.handy.fetchbook.adapter.SearchAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.data.bean.expo.ExpoDetailsBean
import com.handy.fetchbook.data.bean.home.Banner
import com.handy.fetchbook.data.bean.home.ScenicsDetailsBean
import com.handy.fetchbook.databinding.ExpoActivityExpoDetailBinding
import com.handy.fetchbook.databinding.HomeActivityDetailBinding
import com.handy.fetchbook.viewModel.request.RequestRegionDetailsModel
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.youth.banner.indicator.CircleIndicator
import com.zzhoujay.richtext.RichText
import com.zzhoujay.richtext.callback.OnUrlClickListener
import kotlinx.android.synthetic.main.expo_activity_expo_detail.*
import kotlinx.android.synthetic.main.expo_activity_expo_detail.vBanner
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_activity_detail.*
import kotlinx.android.synthetic.main.home_activity_detail.back
import kotlinx.android.synthetic.main.home_activity_sociamedia.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 首页
 *
 * @author Handy
 * @since 2023/7/28 9:50 下午
 */
class EXPODetailActivity : BaseActivity<HomeViewModel, ExpoActivityExpoDetailBinding>() {


    var id: Int = 0

    lateinit var feedbackAdapter: FeedbackAdapter

    override fun layoutId(): Int = R.layout.expo_activity_expo_detail

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        id = intent.getIntExtra("id", 0)


        mViewModel.details(id)

        atvSumbit.setOnClickListener {
            if (TextUtils.isEmpty(mDatabind.aetContent.text.toString())) {
                return@setOnClickListener
            }
            mViewModel.commentExpo(
                id.toString(),
                mDatabind.aetContent.text.toString(),
                arbRatingEdit.rating.toInt()
            )
        }
        mViewModel.expoDetailsResult.observe(this) { resultState ->
            parseState(resultState, {
                load(it)
            })

        }
    }


    private fun load(bean: ExpoDetailsBean) {

        vBanner.setLoopTime(5000)
        vBanner.addBannerLifecycleObserver(this@EXPODetailActivity)
            .setAdapter(ImageStringAdapter(this, bean.banner)).indicator =
            CircleIndicator(this)
        vBanner.setIndicatorNormalColor(Color.DKGRAY)
        vBanner.setIndicatorSelectedColor(Color.RED)

        mDatabind.aivLogo.apply { load(bean.logo) }
        mDatabind.atvTitle.apply { text = bean.title }
        mDatabind.arbRating.apply { rating = bean.rating!!.toFloat() }
        RichText.initCacheDir(this)
        RichText.debugMode = true

        RichText.from(bean.content)
            .urlClick(OnUrlClickListener { url ->
                if (url.startsWith("code://")) {
                    Toast.makeText(
                        this@EXPODetailActivity,
                        url.replaceFirst("code://".toRegex(), ""),
                        Toast.LENGTH_SHORT
                    ).show()

                    return@OnUrlClickListener true
                }
                false
            })
            .into(mDatabind.atvContent)

        feedbackAdapter = FeedbackAdapter(R.layout.expo_item_feedback, null)
        expoRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = feedbackAdapter
        }

        feedbackAdapter.setNewData(bean.expoComments)

    }


}