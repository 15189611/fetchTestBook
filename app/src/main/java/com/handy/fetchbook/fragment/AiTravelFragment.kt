package com.handy.fetchbook.fragment

import AIChatAdapter
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.KeyboardUtils.OnSoftInputChangedListener
import com.blankj.utilcode.util.ToastUtils
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.LoginActivity
import com.handy.fetchbook.activity.RegActivity
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.data.ChatDataBean
import com.handy.fetchbook.data.bean.ChatDataBeanRequest
import com.handy.fetchbook.utils.ViewUtils
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.ai_chat_input_view.*
import kotlinx.android.synthetic.main.draw_fragment_draw.*
import kotlinx.android.synthetic.main.fragment_ai_travel_view.*
import kotlinx.android.synthetic.main.fragment_ai_travel_view.no_login
import kotlinx.android.synthetic.main.layout_no_login.*
import kotlinx.android.synthetic.main.layout_no_login.view.*
import me.hgj.jetpackmvvm.base.fragment.BaseVmFragment
import me.hgj.jetpackmvvm.ext.parseState

/**
 * - Author: Charles
 * - Date: 2023/9/27
 * - Description: AI旅游fragment
 */
class AiTravelFragment : BaseVmFragment<HomeViewModel>() {

    companion object {
        fun newInstance(): AiTravelFragment {
            return AiTravelFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!CacheUtil.isLogin()) {
            no_login.visibility = View.VISIBLE
        } else {
            no_login.visibility = View.GONE
        }
    }

    private var chatValue = ""
    override fun showLoading(message: String) {}
    private var vlAiChatAdapter: AIChatAdapter? = null
    override fun dismissLoading() {}

    override fun layoutId(): Int {
        return R.layout.fragment_ai_travel_view
    }

    override fun initView(savedInstanceState: Bundle?) {
        //登录
        no_login.crrlBtnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        //注册
        crrlBtnReg.crrlBtnReg.setOnClickListener {
            startActivity(Intent(context, RegActivity::class.java))
        }
        vlAiChatAdapter = AIChatAdapter()
        rl_chat_list.adapter = vlAiChatAdapter
        tv_send.setOnClickListener {
            onSendMsg()
        }
        activity?.let {
            KeyboardUtils.registerSoftInputChangedListener(it.window
            ) { height ->
                if (height > 0) {
                    ViewUtils.setMarginBottom(lin_input, height - ViewUtils.dp2px(64f))
                } else {
                    ViewUtils.setMarginBottom(lin_input, -height)
                }
            }
        }
    }

    override fun onDestroy() {
        activity?.let {
            KeyboardUtils.unregisterSoftInputChangedListener(it.window)
        }
        super.onDestroy()
    }

    private fun onSendMsg() {
        chatValue = et_problem.text.toString().trim()
        if (TextUtils.isEmpty(chatValue)) {
            ToastUtils.showShort("请输入问题!")
            return
        }
        mViewModel.getMessage(ChatDataBeanRequest(chatValue))
        vlAiChatAdapter?.addMsg(
            rl_chat_list,
            ChatDataBean(
                chatValue,
                isLoading = false,
                isMySelf = true
            )
        )
        rl_chat_list.postDelayed({
            vlAiChatAdapter?.addMsg(
                rl_chat_list,
                ChatDataBean("", isLoading = true, isMySelf = false)
            )
        }, 300)
        et_problem.setText("")
        KeyboardUtils.hideSoftInput(activity)
        tv_send.isEnabled = false
    }

    override fun lazyLoadData() {
        mViewModel.getMessageResult.observe(this) { resultState ->
            parseState(resultState, {
                val lastData = vlAiChatAdapter?.getLastData()
                lastData?.isLoading = false
                lastData?.data = it
                lastData?.isMySelf = false
                vlAiChatAdapter?.notifyView(rl_chat_list)
                tv_send.isEnabled = true

            }, {
                val lastData = vlAiChatAdapter?.getLastData()
                lastData?.isLoading = false
                lastData?.data = it.errorMsg
                lastData?.isMySelf = false
                vlAiChatAdapter?.notifyView(rl_chat_list)
                tv_send.isEnabled=true
            })
        }
    }

    override fun createObserver() {
    }
}