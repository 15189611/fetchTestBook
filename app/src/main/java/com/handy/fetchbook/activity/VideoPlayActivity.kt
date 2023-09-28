package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import coil.load
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.databinding.MeActivityVideoPlayBinding
import com.handy.fetchbook.eventBus.EventVideoPlayOver
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import org.greenrobot.eventbus.EventBus

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class VideoPlayActivity : BaseActivity<HomeViewModel, MeActivityVideoPlayBinding>() {
    override fun layoutId() = R.layout.me_activity_video_play
    private var orientationUtils: OrientationUtils? = null

    var url: String? = null
    var title: String? = null
    var thumbnail: String? = null
    override fun initView(savedInstanceState: Bundle?) {
        url = intent.getStringExtra("url").toString()
        title = intent.getStringExtra("title").toString()
        thumbnail = intent.getStringExtra("thumbnail").toString()

        mDatabind.videoPlayer.setUp(url, true, title)

        //增加封面
        val imageView = ImageView(this@VideoPlayActivity)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(thumbnail)
        mDatabind.videoPlayer.thumbImageView = imageView
        //增加title
        mDatabind.videoPlayer.titleTextView.visibility = View.VISIBLE
        //设置返回键
        mDatabind.videoPlayer.backButton.visibility = View.VISIBLE
        //设置旋转
        orientationUtils = OrientationUtils(this@VideoPlayActivity, mDatabind.videoPlayer)
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        mDatabind.videoPlayer.fullscreenButton
            .setOnClickListener(View.OnClickListener { // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
                // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
                //orientationUtils.resolveByClick();
                finish()
            })
        mDatabind.videoPlayer.setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
            BooKLogger.d("视频播放进度 = $progress --> total = $duration")
            if (progress >= 98) {
                EventBus.getDefault().post(EventVideoPlayOver())
                finish()
            }
        }
        //是否可以滑动调整
        mDatabind.videoPlayer.setIsTouchWiget(true)
        //设置返回按键功能
        mDatabind.videoPlayer.backButton.setOnClickListener(View.OnClickListener { onBackPressed() })
        ///不需要屏幕旋转
        mDatabind.videoPlayer.isNeedOrientationUtils = false
        mDatabind.videoPlayer.startPlayLogic()
    }

    override fun onPause() {
        super.onPause()
        mDatabind.videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        mDatabind.videoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        GSYVideoManager.releaseAllVideos()
        super.onDestroy()
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }

    override fun onBackPressed() {
        mDatabind.videoPlayer.setVideoAllCallBack(null)
        super.onBackPressed()
    }

}