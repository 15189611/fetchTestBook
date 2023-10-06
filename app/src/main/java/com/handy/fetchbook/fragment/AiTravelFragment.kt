package com.handy.fetchbook.fragment

import AIChatAdapter
import android.os.Bundle
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.vlayout.VLDelegateAdapter
import com.handy.fetchbook.basic.vlayout.VLVirtualLayoutManager
import com.handy.fetchbook.data.ChatDataBean
import com.handy.fetchbook.viewModel.state.HomeViewModel
import kotlinx.android.synthetic.main.ai_chat_input_view.tv_send
import kotlinx.android.synthetic.main.expo_activity.*
import kotlinx.android.synthetic.main.fragment_ai_travel_view.*
import me.hgj.jetpackmvvm.base.fragment.BaseVmFragment

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
    private var chatValue=""
    override fun showLoading(message: String) {}
    private var vlAiChatAdapter: AIChatAdapter? = null
    override fun dismissLoading() {}

    override fun layoutId(): Int {
        return R.layout.fragment_ai_travel_view
    }

    override fun initView(savedInstanceState: Bundle?) {
        vlAiChatAdapter = AIChatAdapter()
        rl_chat_list.adapter = vlAiChatAdapter
        tv_send.setOnClickListener {
            onSendMsg()
        }
    }

    private fun onSendMsg() {
        chatValue="西藏七天遊"
        mViewModel.getMessage(ChatDataBean(chatValue))
//        vlAiChatAdapter?.addMsg(ChatDataBean(chatValue,0,"",System.currentTimeMillis(),true))
//        tv_send.postDelayed({   vlAiChatAdapter?.addMsg(ChatDataBean(chatValue+"aaaa",0,"",System.currentTimeMillis(),false))},2000)
    }

    override fun lazyLoadData() {
        mViewModel.getMessageResult.observe(this, {
            var it1 = it
//            vlAiChatAdapter?.addMsg(ChatDataBean(chatValue,0,"",System.currentTimeMillis(),true))
        })
    }
    //test001@qq.com aa123456
    override fun createObserver() {
    }
}