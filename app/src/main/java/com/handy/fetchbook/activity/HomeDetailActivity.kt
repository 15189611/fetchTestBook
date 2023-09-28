package com.handy.fetchbook.activity

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.data.bean.home.ScenicsDetailsBean
import com.handy.fetchbook.databinding.HomeActivityDetailBinding
import com.handy.fetchbook.viewModel.request.RequestRegionDetailsModel
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.zzhoujay.richtext.RichText
import com.zzhoujay.richtext.callback.OnUrlClickListener
import kotlinx.android.synthetic.main.home_activity_detail.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 瀑布流详情
 *
 * @author Handy
 * @since 2023/7/28 9:50 下午
 */
class HomeDetailActivity : BaseActivity<HomeViewModel, HomeActivityDetailBinding>() {

    var id: String = ""

    private val requestRegionDetailsModel: RequestRegionDetailsModel by viewModels()

    override fun layoutId(): Int = R.layout.home_activity_detail

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        id = intent.getStringExtra("id").toString()
        atv_btn1.setOnClickListener {
            requestRegionDetailsModel.evaluate(id, 0)
            showPing(atv_btn1)
        }

        atv_btn2.setOnClickListener {
            requestRegionDetailsModel.evaluate(id, 1)
            showPing(atv_btn2)
        }

        atv_btn3.setOnClickListener {
            finish()
        }


        requestRegionDetailsModel.details(id)
        requestRegionDetailsModel.detailsResult.observe(this) { resultState ->
            parseState(resultState, {
                load(it)
            })
        }
    }



    private var logoutPop: PopupWindow? = null
    private var logoutPopView: View? = null
    private fun showPing(view:View ) {

        val inflater = getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopView =
            inflater.inflate(R.layout.home_dialog_pinglu, null)
        logoutPop = PopupWindow(
            logoutPopView,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )
        logoutPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            logoutPop!!.dismiss()
        }
        logoutPop!!.setBackgroundDrawable(BitmapDrawable())
        logoutPop!!.animationStyle = R.style.common_CustomDialog
        logoutPop!!.showAsDropDown(view)
    }

    private fun load(bean: ScenicsDetailsBean) {
        aivHead.apply { load(bean.thumbnail) }
        atvTitle.apply { text = bean.title }

        RichText.initCacheDir(this)
        RichText.debugMode = true

        RichText.from(bean.content)
            .urlClick(OnUrlClickListener { url ->
                if (url.startsWith("code://")) {
                    Toast.makeText(
                        this@HomeDetailActivity,
                        url.replaceFirst("code://".toRegex(), ""),
                        Toast.LENGTH_SHORT
                    ).show()

                    return@OnUrlClickListener true
                }
                false
            })
            .into(atvContent)
    }


}