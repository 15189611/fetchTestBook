package com.handy.fetchbook.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.*
import com.handy.fetchbook.adapter.HelpCenterAdapter
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.app.ext.languageSet
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.ext.toAvatar
import com.handy.fetchbook.data.bean.me.HelpCenterBean
import com.handy.fetchbook.data.bean.me.UserInfoBean
import com.handy.fetchbook.databinding.MeFragmentMeBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.me_fragment_me.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 我的Fragment
 *
 */
class MeFragment : BaseFragment<HomeViewModel, MeFragmentMeBinding>() {
    override fun layoutId(): Int = R.layout.me_fragment_me

    private var logoutPop: PopupWindow? = null
    private var logoutPopView: View? = null
    private lateinit var helpCenterAdapter: HelpCenterAdapter

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        //编辑中心
        mDatabind.myNameParent.setOnClickListener {
            startActivity(Intent(context, EditUserInfoActivity::class.java))
        }
        mDatabind.aivHead.setOnClickListener {
            //startActivity(Intent(context, CircleTurntableActivity::class.java))
            startActivity(Intent(context, EditUserInfoActivity::class.java))
        }
        //客服中心
        mDatabind.meAivCustomerService.setOnClickListener {
            val intent = Intent(context, H5Activity::class.java)
            intent.putExtra(
                "url",
                "https://happycat.ladesk.com/scripts/generateWidget.php?v=5.37.2.18&t=1680352328&cwid=0rfm205n&cwrt=C&cwt=chat_popout"
            )
            startActivity(intent)
        }
        //推荐QR
        mDatabind.aivQr.setOnClickListener {
            startActivity(Intent(context, QrActivity::class.java))
        }
        //登录
        mDatabind.noLogin.crrlBtnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        //注册
        mDatabind.noLogin.crrlBtnReg.setOnClickListener {
            startActivity(Intent(context, RegActivity::class.java))
        }
        //人民币钱包
        mDatabind.crllWalletItem1.setOnClickListener {
            startActivity(Intent(context, WalletActivity::class.java))
        }
        //旅游钱包
        mDatabind.crllWalletItem2.setOnClickListener {
//            startActivity(Intent(context, WalletBalanceActivity::class.java))
        }    //旅游宝应用
        mDatabind.crllWalletItem4.setOnClickListener {
            startActivity(Intent(context, MemberUpgradeActivity::class.java))
        }
        //更多
        atvMore.setOnClickListener {
            val intent = Intent(context, HelpCenterActivity::class.java)
            startActivity(intent)
        }
        //修改密码
        crllChangePwd.setOnClickListener {
            val intent = Intent(context, ChangePwdActivity::class.java)
            startActivity(intent)
        }
        //退出
        crllLogout.setOnClickListener {
            showLogout()
        }
        //语言设置
        crllLanguage.setOnClickListener {
            languageSet(requireActivity(), crllLanguage)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        super.initData()
        mViewModel.walletResult.observe(this) { resultState ->
            parseState(resultState, {
//                val cny = it.cnyBalance.orEmpty().toFloat()
//                mDatabind.crllWalletItem1.isVisible = cny > 0
//                mDatabind.atvWallet1.text = it.cnyBalance
//                mDatabind.atvWallet2.text = it.invest
                val cny = (it.cnyBalance ?: "0").toFloat()
                mDatabind.crllWalletItem1.isVisible = cny > 0
                mDatabind.atvWallet1.text = it.cnyBalance
                mDatabind.atvWallet2.text = it.invest
            })
        }
        mViewModel.userinfoResult.observe(this) { resultState ->
            parseState(resultState, {
                updateUserInfo(it)
            })
        }

        helpCenterAdapter = HelpCenterAdapter(R.layout.me_item_help_center, null)
        rvHelpCenter.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = helpCenterAdapter
        }


        helpCenterAdapter.apply {
            setOnItemClickListener { adapter, view, position ->
                val intent = Intent(context, VideoPlayActivity::class.java)
                intent.putExtra("thumbnail", (adapter.data[position] as HelpCenterBean).thumbnail)
                intent.putExtra("url", (adapter.data[position] as HelpCenterBean).video)
                intent.putExtra("title", (adapter.data[position] as HelpCenterBean).title)
                startActivity(intent)

            }
        }
        mViewModel.helpCenterResult.observe(this) { resultState ->
            parseState(resultState, {
                helpCenterAdapter.setNewData(it)
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUserInfo(userInfo: UserInfoBean?) {
        userInfo ?: return
        CacheUtil.saveUserInfo(userInfo)
        mDatabind.atvNick.text = if (userInfo.nickname?.isEmpty() == true) userInfo.account.orEmpty() else userInfo.nickname.orEmpty()
        mDatabind.atvShare.text = "ID/推荐码 : ${userInfo.id}"
        if (userInfo.avatar?.isEmpty() == true) {
            mDatabind.aivHead.load(R.drawable.me_avator)
        } else {
            mDatabind.aivHead.load(userInfo.avatar?.toAvatar().orEmpty()) {
                transformations(CircleCropTransformation())
            }
        }
        var agent = getString(R.string.me_福袋会员)
        when (userInfo.level) {
            1 -> {
                agent = getString(R.string.me_福袋会员)
            }

            2 -> {
                agent = getString(R.string.me_超级福袋会员)
            }

            3 -> {
                agent = getString(R.string.me_黄金会员)
            }

            4 -> {
                agent = getString(R.string.me_荣耀会员)
            }

            5 -> {
                agent = getString(R.string.me_王者会员)
            }

            6 -> {
                agent = getString(R.string.me_至尊会员)
            }

        }

        mDatabind.atvAgent.text = agent
    }

    override fun onResume() {
        super.onResume()
        mDatabind.noLogin.crlNoLogin.isVisible = !CacheUtil.isLogin()
        mDatabind.toolbar.isVisible = CacheUtil.isLogin()
        mDatabind.crllUserinfo.isVisible = CacheUtil.isLogin()
        mDatabind.crrlWallet.isVisible = CacheUtil.isLogin()
        mDatabind.crrlMarket.isVisible = CacheUtil.isLogin()
        mDatabind.crrlSetting.isVisible = CacheUtil.isLogin()
        if (CacheUtil.isLogin()) {
            mViewModel.helpCenter()
            mViewModel.wallet()
            if (CacheUtil.getUserInfo() != null) {
                updateUserInfo(CacheUtil.getUserInfo())
            }
            mViewModel.userinfo()
        }
    }

    private fun showLogout() {
        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopView =
            inflater.inflate(R.layout.me_dialog_logout, null)
        logoutPop = PopupWindow(
            logoutPopView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        logoutPopView!!.findViewById<TextView>(R.id.atvOpen).setOnClickListener {
            logoutPop!!.dismiss()
            CacheUtil.clearUserAll()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        logoutPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            logoutPop!!.dismiss()
        }
        logoutPop!!.setBackgroundDrawable(BitmapDrawable())
        logoutPop!!.animationStyle = R.style.common_CustomDialog
        logoutPop!!.showAsDropDown(mDatabind.crllLogout)
    }
}