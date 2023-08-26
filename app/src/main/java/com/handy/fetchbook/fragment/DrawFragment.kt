package com.handy.fetchbook.fragment

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.LoginActivity
import com.handy.fetchbook.activity.MemberUpgradeActivity
import com.handy.fetchbook.activity.RegActivity
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.app.util.*
import com.handy.fetchbook.databinding.DrawFragmentDrawBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.draw_fragment_draw.*
import kotlinx.android.synthetic.main.draw_view_lucky.view.*
import me.hgj.jetpackmvvm.ext.parseState
import java.util.*


/**
 * 抽奖fragment
 *
 * @author Handy
 * @since 2023/8/1 11:46 下午
 */
class DrawFragment : BaseFragment<HomeViewModel, DrawFragmentDrawBinding>() {
    override fun layoutId(): Int = R.layout.draw_fragment_draw

    private var mStartAnimation: Animation? = null
    private var mEndAnimation: Animation? = null
    private val mLuckyTurntable: ImageView? = null
    private var isRunning = false

    private var isGameRunning = false

    private var message = ""
    private val mItemCount = 3
    private val winningResult = 0 //中奖结果
    private var tabNum = 0
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.noLogin.crrlBtnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        mDatabind.noLogin.crrlBtnReg.setOnClickListener {
            startActivity(Intent(context, RegActivity::class.java))
        }

        mDatabind.aivUpgrade.setOnClickListener {
            startActivity(Intent(context, MemberUpgradeActivity::class.java))
        }
        mDatabind.atvTourismLottery.setOnClickListener {
            if (tabNum == 0) {
                showTourism()
            } else {
                showLucky()
            }
        }

        mDatabind.crlLeft.setOnClickListener {

            mDatabind.iconTitle.setImageResource(R.drawable.draw_icon_title)
            mDatabind.rlRight.root.visibility = View.GONE
            mDatabind.rlLeft.visibility = View.VISIBLE
            tabNum = 0
            atvTourismLottery.text = getText(R.string.draw_旅游抽奖规则说明)
            mDatabind.crlLeft.setBackgroundResource(R.drawable.draw_btn_bg)
            mDatabind.crlRight.setBackgroundResource(R.drawable.draw_white_bg)
        }

        mDatabind.crlRight.setOnClickListener {
            mDatabind.iconTitle.setImageResource(R.drawable.draw_icon_title1)
            mDatabind.rlRight.root.visibility = View.VISIBLE
            mDatabind.rlLeft.visibility = View.GONE
            tabNum = 1
            atvTourismLottery.text = getText(R.string.draw_福袋抽奖规则说明)
            mDatabind.crlRight.setBackgroundResource(R.drawable.draw_btn_bg)
            mDatabind.crlLeft.setBackgroundResource(R.drawable.draw_white_bg)
        }
    }

    override fun onResume() {
        super.onResume()

        mStartAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        mStartAnimation!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })
        if (!CacheUtil.isLogin()) {
            mDatabind.viewContainer.visibility = View.GONE
            mDatabind.noLogin.root.visibility = View.VISIBLE
        } else {
            mDatabind.viewContainer.visibility = View.VISIBLE
            mDatabind.noLogin.root.visibility = View.GONE
        }


        //会员类型 0=普通会员, 1=拓展会员, 2=福袋会员
        val userType = CacheUtil.getUserInfo()?.type ?: 0
        if (userType == 2) {
            mDatabind.aivUpgrade.visibility = View.GONE
            mDatabind.btnAction.visibility = View.VISIBLE
        }
        mViewModel.draw()
        mViewModel.drawResult.observe(this) { resultState ->
            parseState(resultState, {
                isGameRunning = it.code == 0
                isRunning = it.code == 0
                message = it.message
            })
        }

        //旅游抽奖次数
        val luckyTicket = CacheUtil.getUserInfo()?.luckyTicket ?: 0
        if (luckyTicket != 0) {
            mDatabind.btnAction.setOnClickListener {
                if (isGameRunning) {
                    mDatabind.luckyPanel.startGame()
                } else {
                    showLogout()
                }
            }

        }


        mDatabind.rlRight.root.rl_start.setOnClickListener {
            //福袋抽奖次数
            val luckyBag = CacheUtil.getUserInfo()?.luckyBag ?: 0
            if (luckyBag != 0) {
                // 未抽过奖并有抽奖的机会
                if (isRunning) {
                    mStartAnimation!!.reset()
                    mLuckyTurntable!!.startAnimation(mStartAnimation)
                    mEndAnimation?.cancel()
                    Handler().postDelayed({ endAnimation() }, 2000)
                }
            } else {
                showLogout()
            }
        }

        val level = CacheUtil.getUserInfo()?.level ?: 0
        var bg = R.drawable.draw_lucky_bg1
        var startbg = R.drawable.draw_lucky_go1

        var agent = getString(R.string.me_福袋会员)
        when (level) {
            1 -> {
                startbg = R.drawable.draw_lucky_go1
                bg = R.drawable.draw_lucky_bg1
                agent = getString(R.string.me_福袋会员)
            }
            2 -> {
                startbg = R.drawable.draw_lucky_go2
                bg = R.drawable.draw_lucky_bg2
                agent = getString(R.string.me_超级福袋会员)
            }
            3 -> {
                startbg = R.drawable.draw_lucky_go3
                bg = R.drawable.draw_lucky_bg3
                agent = getString(R.string.me_黄金会员)
            }
            4 -> {
                startbg = R.drawable.draw_lucky_go4
                bg = R.drawable.draw_lucky_bg4
                agent = getString(R.string.me_荣耀会员)
            }
            5 -> {
                startbg = R.drawable.draw_lucky_go5
                bg = R.drawable.draw_lucky_bg5
                agent = getString(R.string.me_王者会员)
            }
            6 -> {
                startbg = R.drawable.draw_lucky_go6
                bg = R.drawable.draw_lucky_bg6
                agent = getString(R.string.me_至尊会员)
            }

        }
        mDatabind.rlRight.tvLevel.text = agent
        mDatabind.rlRight.root.setBackgroundResource(bg)
        mDatabind.rlRight.idStartBtn.setImageResource(startbg)

    }

    private var logoutPop: PopupWindow? = null
    private var logoutPopView: View? = null
    private fun showLogout() {

        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopView =
            inflater.inflate(R.layout.draw_dialog_no, null)
        logoutPop = PopupWindow(
            logoutPopView,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )
        logoutPopView!!.findViewById<TextView>(R.id.atv_content).text = message

        logoutPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            logoutPop!!.dismiss()
        }
        logoutPop!!.setBackgroundDrawable(BitmapDrawable())
        logoutPop!!.animationStyle = R.style.common_CustomDialog
        logoutPop!!.showAsDropDown(mDatabind.btnAction)
    }

    private fun stopLeft() {
        val stayIndex = Random().nextInt(8)
        Log.e("LuckyMonkeyPanelView", "====stay===$stayIndex")
        mDatabind.luckyPanel.tryToStop(stayIndex)
    }

    // 结束动画，慢慢停止转动，抽中的奖品定格在指针指向的位置
    private fun endAnimation() {
        val position = winningResult
        val toDegreeMin = 360 / mItemCount * (position - 0.5f) + 1
        val random = Random()
        val randomInt = random.nextInt(360 / mItemCount - 1)
        val toDegree = toDegreeMin + randomInt + 360 * 5 //5周 + 偏移量

        // 按中心点旋转 toDegree度
        // 参数：旋转的开始角度、旋转的结束角度、X轴的伸缩模式、X坐标的伸缩值、Y轴的伸缩模式、Y坐标的伸缩值
        mEndAnimation = RotateAnimation(
            0f,
            toDegreeMin,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        mEndAnimation!!.duration = 3000 // 设置旋转时间
        mEndAnimation!!.repeatCount = 0 // 设置重复次数
        mEndAnimation!!.fillAfter = true // 动画执行完后是否停留在执行完的状态
        mEndAnimation!!.interpolator = DecelerateInterpolator() // 动画播放的速度
        mEndAnimation!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                isRunning = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mLuckyTurntable!!.startAnimation(mEndAnimation)
        mStartAnimation!!.cancel()
    }


    //停止动画（异常情况，没有奖品）
    private fun stopAnimation() {

        //转盘停止回到初始状态
        if (isRunning) {
            mStartAnimation!!.cancel()
            mLuckyTurntable!!.clearAnimation()
            isRunning = false
        }
    }

    private var logoutPopLucky: PopupWindow? = null
    private var logoutPopViewLucky: View? = null

    private fun showLucky() {

        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopViewLucky =
            inflater.inflate(R.layout.draw_dialog_lucky_lottery_rules, null)
        logoutPopLucky = PopupWindow(
            logoutPopViewLucky,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )
        logoutPopViewLucky!!.findViewById<TextView>(R.id.atvJump).setOnClickListener {
            logoutPopLucky!!.dismiss()
//            ARouter.getInstance()
//                .build(RouteUrl.Me.WalletBalanceActivity)
//                .navigation()

        }

        logoutPopViewLucky!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            logoutPopLucky!!.dismiss()
        }
        logoutPopLucky!!.setBackgroundDrawable(BitmapDrawable())
        logoutPopLucky!!.animationStyle = R.style.common_CustomDialog
        logoutPopLucky!!.showAsDropDown(mDatabind.viewContainer)
    }

    private var logoutPopTourism: PopupWindow? = null
    private var logoutPopViewTourism: View? = null

    private fun showTourism() {

        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopViewTourism =
            inflater.inflate(R.layout.draw_dialog_tourism_lottery_rules, null)
        logoutPopTourism = PopupWindow(
            logoutPopViewTourism,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )


        logoutPopViewTourism!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            logoutPopTourism!!.dismiss()
        }
        logoutPopTourism!!.setBackgroundDrawable(BitmapDrawable())
        logoutPopTourism!!.animationStyle = R.style.common_CustomDialog
        logoutPopTourism!!.showAsDropDown(mDatabind.viewContainer)
    }
}