package com.handy.fetchbook.activity

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.FeedbackAdapter
import com.handy.fetchbook.adapter.ImageStringAdapter
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.expo.ExpoDetailsBean
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.hjq.toast.ToastUtils
import com.youth.banner.indicator.CircleIndicator
import com.zzhoujay.richtext.RichText
import com.zzhoujay.richtext.callback.OnUrlClickListener
import kotlinx.android.synthetic.main.expo_activity_expo_detail.*
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.ext.parseState

/**
 * EXPO详情activity
 *
 */
class EXPODetailActivity : BaseVmActivity<HomeViewModel>() {

    private var id: Int = 0

    private lateinit var feedbackAdapter: FeedbackAdapter

    override fun layoutId(): Int = R.layout.expo_activity_expo_detail
    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {}

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        id = intent.getIntExtra("id", 0)
        mViewModel.details(id)
        atvSumbit.setOnClickListener {
            if (TextUtils.isEmpty(aet_content.text.toString())) {
                ToastUtils.show("反馈不能空!")
                return@setOnClickListener
            }
            mViewModel.commentExpo(
                id.toString(),
                aet_content.text.toString(),
                arbRatingEdit.rating.toInt()
            )
        }
    }

    override fun createObserver() {
        mViewModel.commentExpoResult.observe(this) {
            parseMyState(it, onSuccess = { _, model, message ->
                BooKLogger.d("反馈评论成功 = $model -> message = $message")
                ToastUtils.show(message.orEmpty())
                mViewModel.details(id)
            }, onError = { _, error ->
                BooKLogger.d("反馈评论失败 message = ${error.message}")
                ToastUtils.show(error.message.orEmpty())
            })
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

        aivLogo.load(bean.logo)
        atvTitle.apply { text = bean.title }
        arbRating.apply { rating = bean.rating!!.toFloat() }
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
            .into(atvContent)

        expoUserFeedBackTv.isVisible = bean.expoComments.isNotEmpty()
        expoRv.isVisible = bean.expoComments.isNotEmpty()
        feedbackAdapter = FeedbackAdapter(R.layout.expo_item_feedback, null)
        expoRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = feedbackAdapter
        }
        feedbackAdapter.setNewData(bean.expoComments)

    }

}