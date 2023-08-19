package com.handy.fetchbook.fragment

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.*
import com.handy.fetchbook.adapter.HelpCenterAdapter
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.app.ext.languageSet
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.constant.SpKey
import com.handy.fetchbook.data.bean.me.HelpCenterBean
import com.handy.fetchbook.databinding.MeFragmentMeBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_no_login.view.*
import kotlinx.android.synthetic.main.me_fragment_me.*
import me.hgj.jetpackmvvm.ext.parseState


/**
 * 首页Fragment
 *
 * @author Handy
 * @since 2023/8/1 11:46 下午
 */
class MeFragment : BaseFragment<HomeViewModel, MeFragmentMeBinding>() {
    override fun layoutId(): Int = R.layout.me_fragment_me


    private var logoutPop: PopupWindow? = null
    private var logoutPopView: View? = null


    private lateinit var helpCenterAdapter: HelpCenterAdapter

    override fun initView(savedInstanceState: Bundle?) {


        mDatabind.crllUserinfo.setOnClickListener {
            startActivity(Intent(context, EditUserInfoActivity::class.java))
        }
        mDatabind.aivQr.setOnClickListener {
            startActivity(Intent(context, QrActivity::class.java))
        }
        mDatabind. noLogin.crrlBtnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        mDatabind. noLogin.crrlBtnReg.setOnClickListener {
            startActivity(Intent(context, RegActivity::class.java))
        }

        mDatabind. crllWalletItem1.setOnClickListener {
            startActivity(Intent(context, WalletActivity::class.java))
        }

        mDatabind. crllWalletItem2.setOnClickListener {
            startActivity(Intent(context, WalletBalanceActivity::class.java))
        }


        mDatabind.noLogin.crrlBtnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        mDatabind.noLogin.crrlBtnReg.setOnClickListener {
            startActivity(Intent(context, RegActivity::class.java))
        }
        if (TextUtils.isEmpty(SpUtils.getString(SpKey.TOKEN, ""))) {
            mDatabind.noLogin.crlNoLogin.visibility = View.VISIBLE
            mDatabind.crllUserinfo.visibility = View.GONE
        } else {
            mDatabind.atvNick.text = SpUtils.getString(SpKey.LOGINNAME, "")
            mDatabind.atvShare.text = "ID/推荐码 : " + 0
            mViewModel.helpCenter()
            mViewModel.wallet()
            mViewModel.userinfo()
            mDatabind.toolbar.visibility = View.VISIBLE
            mDatabind.noLogin.crlNoLogin.visibility = View.GONE
            mDatabind.crllUserinfo.visibility = View.VISIBLE
            mDatabind.crrlWallet.visibility = View.VISIBLE
//            mDatabind.crrlMarket.visibility = View.VISIBLE
//            mDatabind.crrlTeam.visibility = View.VISIBLE
            mDatabind.crrlSetting.visibility = View.VISIBLE
//            mDatabind.crrlHelp.visibility = View.VISIBLE
        }

        atvMore.setOnClickListener {
            var intent = Intent(context, HelpCenterActivity::class.java)
            startActivity(intent)
        }

        crllChangePwd.setOnClickListener {
            var intent = Intent(context, ChangePwdActivity::class.java)
            startActivity(intent)
        }

        crllLogout.setOnClickListener {
            showLogout()
        }

        crllLanguage.setOnClickListener {
            languageSet(crllLanguage)
        }


    }

    override fun onResume() {
        super.onResume()

        if (TextUtils.isEmpty(SpUtils.getString(SpKey.TOKEN, ""))) {
            mDatabind.noLogin.root.visibility = View.VISIBLE
        } else {
            mDatabind.noLogin.root.visibility = View.GONE
        }

        mViewModel.walletResult.observe(this) { resultState ->
            parseState(resultState, {
                mDatabind.atvWallet1.text = it.cnyBalance
                mDatabind.atvWallet2.text = it.invest
            })
        }
        mViewModel.userinfoResult.observe(this) { resultState ->
            parseState(resultState, {
                mDatabind.atvNick.text = it.account
                mDatabind.atvShare.text = "ID/推荐码 : " + it.id

                var agent = getString(R.string.me_福袋会员)

                when (it.level) {
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
            })
        }

        helpCenterAdapter = HelpCenterAdapter(R.layout.me_item_help_center, null)
        rvHelpCenter.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = helpCenterAdapter
        }



        helpCenterAdapter.apply {
            setOnItemClickListener { adapter, view, position ->
                var intent = Intent(context, VideoPlayActivity::class.java)
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

    private fun showLogout() {

        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopView =
            inflater.inflate(R.layout.me_dialog_logout, null)
        logoutPop = PopupWindow(
            logoutPopView,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )
        logoutPopView!!.findViewById<TextView>(R.id.atvOpen).setOnClickListener {
            logoutPop!!.dismiss()
            var intent = Intent(context, LoginActivity::class.java)
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