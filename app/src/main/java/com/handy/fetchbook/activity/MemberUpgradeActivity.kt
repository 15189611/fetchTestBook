package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.*
import com.handy.fetchbook.databinding.MeActivityMemberUpgradeBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.me_activity_member_upgrade.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class MemberUpgradeActivity : BaseActivity<HomeViewModel, MeActivityMemberUpgradeBinding>() {
    override fun layoutId() = R.layout.me_activity_member_upgrade

    private var openPop: PopupWindow? = null
    private var openPopView: View? = null

    private var noPop: PopupWindow? = null
    private var noPopView: View? = null

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener {
            finish()
        }

        atvOpen!!.setOnClickListener {
            val userType = CacheUtil.getUserInfo()?.type ?: 0
            if (userType == 0) {
                val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                openPopView =
                    inflater.inflate(R.layout.me_dialog_open_expand, null)
                openPop = PopupWindow(
                    openPopView,
                    WindowManager.LayoutParams.FILL_PARENT,
                    WindowManager.LayoutParams.FILL_PARENT,
                    true
                )
                openPopView!!.findViewById<TextView>(R.id.atvOpen).setOnClickListener {
                    mViewModel.buyMembership(1)
                    openPop!!.dismiss()
                }
                openPop!!.setBackgroundDrawable(BitmapDrawable())
                openPop!!.animationStyle = R.style.common_CustomDialog
                openPop!!.showAsDropDown(atvOpen)

            }
        }

        mViewModel.buyMembershipResult.observe(this) { resultState ->
            parseState(resultState, {
                showDialog(true)
            })
        }
    }

    private fun showDialog(isFinish: Boolean) {
        if (isFinish) {

            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            noPopView =
                inflater.inflate(R.layout.me_dialog_no_expand, null)
            noPop = PopupWindow(
                noPopView,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT,
                true
            )
            noPopView!!.findViewById<TextView>(R.id.atvRecharge).setOnClickListener {
                noPop!!.dismiss()
                startActivity(Intent(this, WalletActivity::class.java))
            }


            noPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
                noPop!!.dismiss()
            }
            noPop!!.setBackgroundDrawable(BitmapDrawable())
            noPop!!.animationStyle = R.style.common_CustomDialog
            noPop!!.showAsDropDown(atvOpen!!)
        }
    }
}