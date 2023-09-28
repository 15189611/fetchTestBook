package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.UpgradeAdapter
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.*
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.databinding.MeActivityMemberUpgradeBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.me_activity_member_upgrade.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 拼团列表
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class MemberUpgradeActivity : BaseActivity<HomeViewModel, MeActivityMemberUpgradeBinding>() {
    override fun layoutId() = R.layout.me_activity_member_upgrade
    private lateinit var upgradeAdapter: UpgradeAdapter
    private var openPop: PopupWindow? = null
    private var openPopView: View? = null
    var mCurrPage = 1
    private var noPop: PopupWindow? = null
    private var noPopView: View? = null

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener {
            finish()
        }
        upgradeAdapter = UpgradeAdapter(R.layout.upgrade_item, null)
        upgradeRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upgradeAdapter
        }

        upgradeAdapter.apply {
            setOnItemClickListener { adapter, view, position ->
//                var intent = Intent(this@MemberUpgradeActivity, EXPODetailActivity::class.java)
//                intent.putExtra("id", (adapter.data[position] as Items).id)
//                startActivity(intent)
                view.findViewById<TextView>(R.id.memberUpItemMore).setOnClickListener{
                    var intent = Intent(this@MemberUpgradeActivity, MemberItemMoreDetailActivity::class.java);
                    intent.putExtra("id", upgradeAdapter.data[position]?.uid)
                    intent.putExtra("price", upgradeAdapter.data[position]?.price)
                    startActivity(intent)
                };
            }
        }
        mViewModel.wallet()
        mViewModel.walletResult.observe(this){ resultState ->
            parseState(resultState, {
                //BooKLogger.d("我的钱包购买记录接口成功->" + it.cnyBalance)
                mDatabind.atvMoney.text = it.invest
            })
        }

        mViewModel.grouplist(true, mCurrPage)

        mViewModel.groupSharingBean.observe(this) { resultState ->
            parseMyState(resultState, { _, it ->
                upgradeAdapter.setNewData(it.items)
            })
        }

        mDatabind.memberHistoryClick.setOnClickListener {
            startActivity(Intent(this, MemberUpHistoryActivity::class.java))
        }
//        atvOpen!!.setOnClickListener {
//            val userType = CacheUtil.getUserInfo()?.type ?: 0
//            if (userType == 0) {
//                val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                openPopView =
//                    inflater.inflate(R.layout.me_dialog_open_expand, null)
//                openPop = PopupWindow(
//                    openPopView,
//                    WindowManager.LayoutParams.FILL_PARENT,
//                    WindowManager.LayoutParams.FILL_PARENT,
//                    true
//                )
//                openPopView!!.findViewById<TextView>(R.id.atvOpen).setOnClickListener {
//                    mViewModel.buyMembership(1)
//                    openPop!!.dismiss()
//                }
//                openPop!!.setBackgroundDrawable(BitmapDrawable())
//                openPop!!.animationStyle = R.style.common_CustomDialog
//                !!.showAsDropDown(atvOpen)openPop
//
//            }
//        }
//
//        mViewModel.buyMembershipResult.observe(this) { resultState ->
//            parseState(resultState, {
//                showDialog(true)
//            })
//        }
    }

//    private fun showDialog(isFinish: Boolean) {
//        if (isFinish) {
//
//            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            noPopView =
//                inflater.inflate(R.layout.me_dialog_no_expand, null)
//            noPop = PopupWindow(
//                noPopView,
//                WindowManager.LayoutParams.FILL_PARENT,
//                WindowManager.LayoutParams.FILL_PARENT,
//                true
//            )
//            noPopView!!.findViewById<TextView>(R.id.atvRecharge).setOnClickListener {
//                noPop!!.dismiss()
//                startActivity(Intent(this, WalletActivity::class.java))
//            }
//
//
//            noPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
//                noPop!!.dismiss()
//            }
//            noPop!!.setBackgroundDrawable(BitmapDrawable())
//            noPop!!.animationStyle = R.style.common_CustomDialog
//            noPop!!.showAsDropDown(atvOpen!!)
//        }
//    }
}