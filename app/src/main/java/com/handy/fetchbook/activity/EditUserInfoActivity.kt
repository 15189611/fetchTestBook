package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.airsaid.pickerviewlibrary.TimePickerView
import com.handy.fetchbook.R
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.me.UserInfoBean
import com.handy.fetchbook.view.MyTimePickerView
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.me_activity_edit_userinfo.*
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.ext.parseState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 编辑个人信息
 *
 */
@SuppressLint("CustomSplashScreen")
class EditUserInfoActivity : BaseVmActivity<HomeViewModel>() {
    private val userInfo: UserInfoBean? by lazy {
        CacheUtil.getUserInfo()
    }

    override fun layoutId() = R.layout.me_activity_edit_userinfo
    override fun showLoading(message: String) {}
    override fun dismissLoading() {}

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        ivAvata1.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata1)
        }
        ivAvata2.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata2)
        }
        ivAvata3.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata3)
        }
        ivAvata4.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata4)
        }
        ivAvata5.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata5)
        }
        ivAvata6.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata6)
        }
        ivAvata7.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata7)
        }
        ivAvata8.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata8)
        }

        birthdayParent.setOnClickListener {
            val timePick = MyTimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY)
            timePick.setCyclic(true)
            timePick.setRange(1949, 2040)
            timePick.setTime(Date())
            timePick.show()
            timePick.setOnTimeSelectListener {
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                Toast.makeText(this, format.format(it), Toast.LENGTH_SHORT).show()
            }
        }

        mViewModel.getEditUserInfo()
    }

    override fun createObserver() {
        atvAccount.text = userInfo?.account.orEmpty()
        atvMembershipAt.text = userInfo?.membershipAt ?: "未开通"
        atvVerify.text = if (userInfo?.verify == 1) "已认证" else "未完成"
        locationParent.isVisible = (userInfo?.location?.isNotEmpty() == true)
        atvLocation.text = userInfo?.location.orEmpty()

        mViewModel.editUserInfoResult.observe(this) {
            parseState(it, { info ->
                BooKLogger.d("编辑个人页面接口成功 = $info")
            }, { error ->
                BooKLogger.d("编辑个人页面 接口失败 = ${error.message}")
            })
        }
    }

}