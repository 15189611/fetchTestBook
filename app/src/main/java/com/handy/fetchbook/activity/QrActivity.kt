package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ImageUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.ext.dp
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.hjq.toast.ToastUtils
import kotlinx.android.synthetic.main.me_activity_qr.*
import kotlinx.coroutines.*
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity

/**
 * 推荐QR ->activity
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class QrActivity : BaseVmActivity<HomeViewModel>() {

    private var h5LoginUrl = "http://p28web.bbq.bet/sign-up?referral="

    private var lastCopyUrl = ""
    private var qrcodeBitmap: Bitmap? = null

    override fun layoutId() = R.layout.me_activity_qr
    override fun showLoading(message: String) {
    }

    private val bg = GradientDrawable().apply {
        setColor(Color.WHITE)
        setStroke(2.dp(), Color.parseColor("#FDDB08"))
        cornerRadius = 4.dp().toFloat()
    }

    override fun createObserver() {

    }

    override fun dismissLoading() {
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        val userInfo = CacheUtil.getUserInfo()
        qrTextContent.text = getString(R.string.me_推荐码).format(userInfo?.id)
        qrRecommendUrl.text = h5LoginUrl + userInfo?.id
        lastCopyUrl = h5LoginUrl + userInfo?.id
        qrImageView.background = bg
        qrImageView.updatePadding(20.dp(), 20.dp(), 20.dp(), 20.dp())
        lifecycleScope.launch(Dispatchers.IO) {
            val bitMapUrl = lastCopyUrl
            val drawable = ContextCompat.getDrawable(this@QrActivity, R.drawable.me_avata2)
            val logoBitMap = drawable?.toBitmap(30.dp(), 30.dp())
            qrcodeBitmap = QRCodeEncoder.syncEncodeQRCode(bitMapUrl, 220.dp(), Color.BLACK, Color.WHITE, logoBitMap)
            withContext(Dispatchers.Main) {
                qrImageView.setImageBitmap(qrcodeBitmap)
            }
        }

        qrImageView.setOnLongClickListener {
            val fileName = "qr_" + System.currentTimeMillis() + ".png"
            val save2Album = ImageUtils.save2Album(qrcodeBitmap, fileName, Bitmap.CompressFormat.PNG)
            if (save2Album != null) {
                ToastUtils.show(R.string.qr_long_click_success)
            }
            true
        }
        btnCopy.setOnClickListener {
            ClipboardUtils.copyText(lastCopyUrl)
            ToastUtils.show(R.string.qr_copy_text)
        }
        back.setOnClickListener { finish() }
    }

}