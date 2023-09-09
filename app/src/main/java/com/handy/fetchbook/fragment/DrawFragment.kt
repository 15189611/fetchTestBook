package com.handy.fetchbook.fragment

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.*
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.ext.MyResultState
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.DrawOpenRedPacketBean
import com.handy.fetchbook.databinding.DrawFragmentDrawBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.draw_fragment_draw.atvTourismLottery
import kotlinx.android.synthetic.main.draw_view_lucky.view.rl_start
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.ext.parseState
import java.util.Random

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
    private var mLuckyTurntable: ImageView? = null
    private var isRunning = false

    private var message = ""
    private var tabNum = 0

    private val winningResult = 0 //中奖结果

    private val mItemCount = 7
    //转到转盘的位置，1:代表左边1。2：代表左2 3：代表左3
    private val list = mutableListOf<Pair<String, Int>>()
    private var lastPrizePosition = 1
    private var currentDrawModel: DrawOpenRedPacketBean? = null
    private var currentDawMessage: String? = null

    override fun initView(savedInstanceState: Bundle?) {
        list.clear()
        list.add(Pair("1.5%", 1))
        list.add(Pair("1.2%", 2))
        list.add(Pair("0.9%", 3))
        list.add(Pair("0.5%", 4))
        list.add(Pair("10%", 5))
        list.add(Pair("5%", 6))
        list.add(Pair("3%", 7))

        mLuckyTurntable = mDatabind.rlRight.idLuckyTurntable
        mDatabind.noLogin.crrlBtnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        mDatabind.noLogin.crrlBtnReg.setOnClickListener {
            startActivity(Intent(context, RegActivity::class.java))
        }
        mDatabind.drawHistoryParent.setOnClickListener {
            //历史记录
            startActivity(Intent(context, DrawWalletHistoryActivity::class.java))
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

        mStartAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        mStartAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                endAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        //旅游抽奖次数
        mDatabind.btnAction.setOnClickListener {
            if (mDatabind.luckyPanel.isGameRunning) return@setOnClickListener
            mViewModel.draw()
        }

        mDatabind.rlRight.root.rl_start.setOnClickListener {
            //福袋抽奖次数
            if (isRunning) return@setOnClickListener
            mViewModel.openRedPacket(mutableMapOf())
        }

    }

    override fun initData() {
        super.initData()
        mViewModel.drawResult.observe(this) { resultState ->
            parseState(resultState, {
                BooKLogger.d("抽奖draw接口成功-> code = ${it.code} -> message  = ${it.message}")
                message = it.message
                if (it.code == 0) {
                    mDatabind.luckyPanel.setIsGameRunning(true)
                    mDatabind.luckyPanel.startGame()
                    lifecycleScope.launch {
                        delay(1000)
                        stopLeft()
                    }
                } else {
                    showLogout()
                }
            }, { error ->
                message = error.message.orEmpty()
                BooKLogger.d("抽奖draw接口失败-> code = ${error.errCode} -> message  = ${error.message}")
            })
        }
        mViewModel.openRedPacketResult.observe(this) {
            parseMyState(it, onSuccess = { _, model, message ->
                BooKLogger.d("福袋接口成功->$model")
                currentDrawModel = model
                currentDawMessage = message
                val profit = list.firstOrNull { list -> list.first == model.profit }
                lastPrizePosition = profit?.second ?: 1
                if (isRunning) return@parseMyState

                mStartAnimation?.reset()
                mStartAnimation?.cancel()
                mLuckyTurntable?.startAnimation(mStartAnimation)
                isRunning = true
            }, onError = { _, error ->
                message = "您今日已开过福袋了"
                showLogout()
                BooKLogger.d("福袋接口成功-> code = ${error.errCode} -> message  = ${error.message}")
            })
        }
    }

    override fun onResume() {
        super.onResume()
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
        showLevelUi()
    }

    private fun showLevelUi() {
        val level = CacheUtil.getUserInfo()?.wheel_level ?: 0
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
        mDatabind.rlRight.drawLuckViewBg.setBackgroundResource(bg)
        mDatabind.rlRight.rlStart.setImageResource(startbg)
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
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
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
        val position: Int = lastPrizePosition
        val oneDegree = 360 / mItemCount
        val toDegreeMin = oneDegree * (position - 0.5f)
        BooKLogger.d("value = $$toDegreeMin")
        val toDegree = toDegreeMin + 360 * 5 //3周 + 偏移量
        BooKLogger.d("value = $$toDegree")

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
        mEndAnimation?.duration = 3000 // 设置旋转时间
        mEndAnimation?.repeatCount = 0 // 设置重复次数
        mEndAnimation?.fillAfter = true // 动画执行完后是否停留在执行完的状态
        mEndAnimation?.interpolator = DecelerateInterpolator() // 动画播放的速度
        mEndAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                isRunning = false
                drawEndAnimationOver()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mEndAnimation?.cancel()
        mLuckyTurntable?.startAnimation(mEndAnimation)
    }
    //动画结束弹框
    private fun drawEndAnimationOver() {
        val inflater = context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val logoutPopView = inflater.inflate(R.layout.dialog_draw_luck_dialog_view, null)
        val drawPopWindow = PopupWindow(
            logoutPopView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        val text = currentDawMessage
            ?: ("恭喜您获得" + currentDrawModel?.profit.orEmpty() + "的福袋奖励,获得" + currentDrawModel?.amount.orEmpty() + "忧旅游宝")
        logoutPopView.findViewById<TextView>(R.id.drawSuccessDialogContent).text = text
        logoutPopView.findViewById<ImageView>(R.id.drawClose).setOnClickListener {
            drawPopWindow.dismiss()
        }
        drawPopWindow.setBackgroundDrawable(BitmapDrawable())
        drawPopWindow.animationStyle = R.style.common_CustomDialog
        drawPopWindow.showAsDropDown(mDatabind.btnAction)
    }

    //停止动画（异常情况，没有奖品）
    private fun stopAnimation() {
        //转盘停止回到初始状态
        if (isRunning) {
            mStartAnimation?.cancel()
            mEndAnimation?.cancel()
            mLuckyTurntable?.clearAnimation()
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
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        logoutPopViewLucky!!.findViewById<TextView>(R.id.atvJump).setOnClickListener {
            logoutPopLucky!!.dismiss()
            startActivity(Intent(context, WalletBalanceActivity::class.java))
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
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
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