package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.*
import com.blankj.utilcode.util.KeyboardUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.basic.ext.addListener
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.me.WalletBean
import com.handy.fetchbook.databinding.MeActivityWalletBalanceBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.me_activity_wallet_balance.back
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 我的钱包
 *
 * @author Charles
 */
@SuppressLint("CustomSplashScreen")
class WalletBalanceActivity : BaseActivity<HomeViewModel, MeActivityWalletBalanceBinding>() {
    override fun layoutId() = R.layout.me_activity_wallet_balance
    private var balance: String? = null
    private var walletBean: WalletBean? = null
    private var popupWindow: PopupWindow? = null
    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        mViewModel.wallet()
        mDatabind.aetAccount.addListener { s, start, before, count ->
            balance = s.toString()
            mDatabind.walletValueOne.text = "$balance 美元"
            mDatabind.walletValueTwo.text = "$balance 美元"
            checkout()
        }
        mDatabind.btnOk.setOnClickListener {
            val body = mutableMapOf<String, Any>("amount" to balance.orEmpty(), "type" to 1)
            mViewModel.investInfo(body)
            KeyboardUtils.hideSoftInput(this)
        }
        mDatabind.btnAll.setOnClickListener {
            mDatabind.aetAccount.setText(walletBean?.balance.orEmpty())
        }
        mDatabind.buyHistoryClick.setOnClickListener {
            startActivity(Intent(this, MyBuyWalletHistoryActivity::class.java))
        }
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.walletResult.observe(this) { resultState ->
            parseState(resultState, {
                walletBean = it
                mDatabind.atvMoney.text = it.usd
                mDatabind.walletBalance.text = it.balance.orEmpty()
            })
        }
        mViewModel.investResult.observe(this) {
            parseState(it, { info ->
                BooKLogger.d("我的钱包 兑换接口成功 = $info")
                showDialog()
                mViewModel.wallet()
                startActivity(Intent(this, MyBuyWalletHistoryActivity::class.java))
            }, { error ->
                BooKLogger.d("我的钱包 兑换接口失败 = ${error.message}")
            })
        }
    }

    private fun showDialog() {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val parentView = inflater.inflate(R.layout.my_wallet_invest_success, null)
        popupWindow = PopupWindow(parentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true)
        parentView!!.findViewById<ImageView>(R.id.aiv_Close).setOnClickListener {
            popupWindow?.dismiss()
        }
        val content = resources.getString(R.string.wallet_invest_tip)
        parentView.findViewById<TextView>(R.id.atv_content).text = String.format(content, balance)
        popupWindow?.setBackgroundDrawable(BitmapDrawable())
        popupWindow?.animationStyle = R.style.common_CustomDialog
        popupWindow?.showAsDropDown(mDatabind.walletRootView)
    }

    private fun checkout(): Boolean {
        if (balance?.isNotEmpty() == true) {
            mDatabind.btnOk.isClickable = true
            mDatabind.btnOk.isEnabled = true
            mDatabind.btnOk.setBackgroundResource(R.drawable.common_btn_login_gradient)
            return true
        }
        mDatabind.btnOk.isClickable = false
        mDatabind.btnOk.isEnabled = false
        mDatabind.btnOk.setBackgroundResource(R.drawable.common_btn_un_save_gradient)
        return false
    }
}