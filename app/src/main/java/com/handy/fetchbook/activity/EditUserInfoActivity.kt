package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.app.base.BaseActivity
import com.handy.fetchbook.app.util.SpUtils
import com.handy.fetchbook.constant.SpKey
import com.handy.fetchbook.databinding.ActivitySplashBinding
import com.handy.fetchbook.databinding.MeActivityEditUserinfoBinding
import com.handy.fetchbook.databinding.MeActivityQrBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import com.handy.fetchbook.viewModel.state.MainViewModel
import kotlinx.android.synthetic.main.me_activity_qr.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 启动页
 *
 * @author Handy
 * @since 2023/7/28 9:47 下午
 */
@SuppressLint("CustomSplashScreen")
class EditUserInfoActivity : BaseActivity<HomeViewModel, MeActivityEditUserinfoBinding>() {
    override fun layoutId() = R.layout.me_activity_edit_userinfo

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }
        back.setOnClickListener { finish() }
        mDatabind.ivAvata1.setOnClickListener {
            mDatabind.aivHead.setImageResource(R.drawable.me_avata1)
        }


        mDatabind.ivAvata1.setOnClickListener {
            mDatabind.aivHead.setImageResource(R.drawable.me_avata1)
        }

        mDatabind.ivAvata2.setOnClickListener {
            mDatabind.aivHead.setImageResource(R.drawable.me_avata2)
        }

        mDatabind.ivAvata3.setOnClickListener {
            mDatabind.aivHead.setImageResource(R.drawable.me_avata3)
        }

        mDatabind. ivAvata4.setOnClickListener {
            mDatabind. aivHead.setImageResource(R.drawable.me_avata4)
        }

        mDatabind.ivAvata5.setOnClickListener {
            mDatabind.aivHead.setImageResource(R.drawable.me_avata5)
        }

        mDatabind.ivAvata6.setOnClickListener {
            mDatabind.aivHead.setImageResource(R.drawable.me_avata6)
        }

        mDatabind. ivAvata7.setOnClickListener {
            mDatabind.aivHead.setImageResource(R.drawable.me_avata7)
        }

        mDatabind.ivAvata8.setOnClickListener {
            mDatabind.aivHead.setImageResource(R.drawable.me_avata8)
        }
    }

}