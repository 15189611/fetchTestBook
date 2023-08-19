package com.handy.fetchbook.view.home

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.EXPOActivity
import com.handy.fetchbook.activity.H5Activity
import com.handy.fetchbook.activity.HelpCenterActivity
import com.handy.fetchbook.activity.LoginActivity
import com.handy.fetchbook.activity.MemberUpgradeActivity
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.basic.AbsModuleView
import com.handy.fetchbook.basic.ext.appCompatActivity
import com.handy.fetchbook.basic.ext.doOnLifecycle
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.constant.SpKey
import com.handy.fetchbook.model.home.HomeGoldViewModel
import kotlinx.android.synthetic.main.home_item_gold_view.view.atvNoBtn
import kotlinx.android.synthetic.main.home_item_gold_view.view.crllItem1
import kotlinx.android.synthetic.main.home_item_gold_view.view.crllItem2
import kotlinx.android.synthetic.main.home_item_gold_view.view.crllItem3
import kotlinx.android.synthetic.main.home_item_gold_view.view.crllItem4
import kotlinx.android.synthetic.main.home_item_gold_view.view.crllItem5
import kotlinx.android.synthetic.main.home_item_gold_view.view.crllItem6
import kotlinx.android.synthetic.main.home_item_gold_view.view.noLogin

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
class HomeGoldView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbsModuleView<HomeGoldViewModel>(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewTreeLifecycleOwner()?.lifecycle?.doOnLifecycle(
            onResume = { onResume() },
            onPause = { onPause() },
            onDestroy = { onDestroy() })
    }

    private var activityPop: PopupWindow? = null
    private var activityPopView: View? = null

    override fun getLayoutId(): Int {
        return R.layout.home_item_gold_view
    }

    override fun onChanged(model: HomeGoldViewModel) {
        super.onChanged(model)
        crllItem1.setOnClickListener {
            showActivitytFinsh()
        }

        crllItem2.setOnClickListener {
            val intent = Intent(context, H5Activity::class.java)
            intent.putExtra("url", "http://www.baidu.com")
            appCompatActivity().startActivity(intent)
        }
        crllItem3.setOnClickListener {
            val intent = Intent(context, EXPOActivity::class.java)
            appCompatActivity().startActivity(intent)
        }
        crllItem4.setOnClickListener {
            val intent = Intent(context, HelpCenterActivity::class.java)
            appCompatActivity().startActivity(intent)
        }
        crllItem5.setOnClickListener {
            val intent = Intent(context, H5Activity::class.java)
            intent.putExtra("url", "http://www.baidu.com")
            appCompatActivity().startActivity(intent)
        }

        crllItem6.setOnClickListener {
            val intent = Intent(context, MemberUpgradeActivity::class.java)
            appCompatActivity().startActivity(intent)
        }


        atvNoBtn.setOnClickListener {
            if (CacheUtil.isLogin()) return@setOnClickListener
            appCompatActivity().startActivity(Intent(context, LoginActivity::class.java))
        }
        noLogin.setOnClickListener {
            if (CacheUtil.isLogin()) return@setOnClickListener
            appCompatActivity().startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private fun showActivitytFinsh() {
        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        activityPopView = inflater.inflate(R.layout.home_dialog_readme, null)
        activityPop = PopupWindow(
            activityPopView,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )
        activityPopView?.findViewById<ImageView>(R.id.aiv_close)?.setOnClickListener {
            activityPop?.dismiss()
        }
        activityPop?.setBackgroundDrawable(BitmapDrawable())
        activityPop?.animationStyle = R.style.common_CustomDialog
        activityPop?.showAsDropDown(noLogin)
    }

    private fun onResume() {
        BooKLogger.d("gold onResume -> isLogin = ${CacheUtil.isLogin()}")
        atvNoBtn.isVisible = !CacheUtil.isLogin()
    }

    private fun onPause() {}


    private fun onDestroy() {}

}