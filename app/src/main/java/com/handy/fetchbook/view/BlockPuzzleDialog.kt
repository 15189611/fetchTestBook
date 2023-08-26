package com.handy.fetchbook.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.handy.fetchbook.R
import com.handy.fetchbook.model.CaptchaCheckOt
import com.handy.fetchbook.model.CaptchaGetOt
import com.handy.fetchbook.model.Point
import com.handy.fetchbook.utils.AESUtil
import com.handy.fetchbook.utils.ImageUtil
import com.handy.fetchbook.net.Configuration
import kotlinx.android.synthetic.main.dialog_block_puzzle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Date:2020/5/6
 * author:wuyan
 */
class BlockPuzzleDialog : Dialog {
    constructor(mContext: Context) : this(mContext, 0)
    constructor(mContext: Context, themeResId: Int) : super(
        mContext,
        R.style.me_dialog
    ) {
        window!!.setGravity(Gravity.CENTER)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val windowManager = (mContext as Activity).windowManager
        val display = windowManager.defaultDisplay
        val lp = window!!.attributes
        lp.width = display.width * 9 / 10//设置宽度为屏幕的0.9
        window!!.attributes = lp
        setCanceledOnTouchOutside(false)//点击外部Dialog不消失
    }

    var baseImageBase64: String = ""//背景图片
    var slideImageBase64: String = ""//滑动图片
    var key: String = ""//ase加密密钥
    var handler: Handler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_block_puzzle)

        tv_delete.setOnClickListener {
            dismiss()
        }

        tv_refresh.setOnClickListener {
            loadCaptcha()
        }

        //设置默认图片
        val bitmap: Bitmap = ImageUtil.getBitmap(context, R.drawable.bg_default)
        dragView.setUp(bitmap, bitmap)
        dragView.setSBUnMove(false)
        loadCaptcha()
    }

    private fun loadCaptcha() {
        Configuration.token = ""
        GlobalScope.launch(Dispatchers.Main) {
            try {

                dragView.visibility = INVISIBLE
                rl_pb.visibility = VISIBLE

                val o = CaptchaGetOt(
                    captchaType = "blockPuzzle"
                )
                val b = Configuration.server.getAsync().await().body()
                when (b?.code) {

                    0 -> {
                        baseImageBase64 = b.data!!.originalImageBase64
                        slideImageBase64 = b.data!!.jigsawImageBase64
                        Configuration.token = b.data!!.token
                        key = b.data!!.secretKey

                        dragView.setUp(
                            ImageUtil.base64ToBitmap(baseImageBase64)!!,
                            ImageUtil.base64ToBitmap(slideImageBase64)!!
                        )
                        dragView.setSBUnMove(true)
                        initEvent()
                    }
                    else -> {
                        dragView.setSBUnMove(false)
                    }
                }
                dragView.visibility = VISIBLE
                rl_pb.visibility = GONE

            } catch (e: Exception) {
                e.printStackTrace()
                runUIDelayed(
                    Runnable {
                        dragView.setSBUnMove(false)
                        dragView.visibility = VISIBLE
                        rl_pb.visibility = GONE
                        Toast.makeText(context, "网络请求错误", Toast.LENGTH_SHORT).show()
                    }, 1000
                )
            }
        }
    }

    private fun checkCaptcha(sliderXMoved: Double) {
        val point = Point(sliderXMoved, 5.0)
        var pointStr = Gson().toJson(point).toString()
        Log.e("wuyan", pointStr)
        Log.e("wuyan", AESUtil.encode(pointStr, key))
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val o = CaptchaCheckOt(
                    captchaType = "blockPuzzle",
                    pointJson = AESUtil.encode(pointStr, key),
                    token = Configuration.token
                )
                val b = Configuration.server.checkAsync(o).await().body()
                when (b?.code) {

                    0 -> {
                        try {
                            dragView.ok()
//                            runUIDelayed(
//                                Runnable {
//                                    dragView.reset()
//                                    dismiss()
//                                    loadCaptcha()
//                                }, 2000
//                            )
                            val result = Configuration.token + "---" + pointStr
                            mOnResultsListener!!.onResultsClick(AESUtil.encode(result, key))
                        } catch (e: Exception) {
                        }
                    }
                    else -> {
                        dragView.fail()
                        //刷新验证码
                        loadCaptcha()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                dragView.fail()
                loadCaptcha();
            }
        }
    }

    fun initEvent() {
        dragView.setDragListenner(object : DragImageView.DragListenner {
            override fun onDrag(position: Double) {
                checkCaptcha(position)
            }
        })
    }

    fun runUIDelayed(run: Runnable, de: Int) {
        if (handler == null)
            handler = Handler(Looper.getMainLooper())
        handler!!.postDelayed(run, de.toLong())
    }

    var mOnResultsListener: OnResultsListener? = null

    interface OnResultsListener {
        fun onResultsClick(result: String)
    }

    fun setOnResultsListener(mOnResultsListener: OnResultsListener) {
        this.mOnResultsListener = mOnResultsListener
    }

    fun reset() {
        dragView.reset()
        loadCaptcha()
    }
}