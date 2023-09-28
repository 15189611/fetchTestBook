package com.handy.fetchbook.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.LoginActivity
import com.handy.fetchbook.activity.MemberUpgradeActivity
import com.handy.fetchbook.activity.RegActivity
import com.handy.fetchbook.activity.VideoPlayActivity
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.app.util.*
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.data.bean.me.UserInfoBean
import com.handy.fetchbook.databinding.TaskFragmentTaskBinding
import com.handy.fetchbook.eventBus.EventVideoPlayOver
import com.handy.fetchbook.viewModel.state.HomeViewModel
import me.hgj.jetpackmvvm.ext.parseState
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 任务Fragment
 */
class TaskFragment : BaseFragment<HomeViewModel, TaskFragmentTaskBinding>() {
    override fun layoutId(): Int = R.layout.task_fragment_task

    private var videoUrl: String? = null

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {

        mViewModel.taskStatus()
        mViewModel.videoList()

        mDatabind.noLogin.crrlBtnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        mDatabind.noLogin.crrlBtnReg.setOnClickListener {
            startActivity(Intent(context, RegActivity::class.java))
        }
        mDatabind.aivUpgrade.setOnClickListener {
            startActivity(Intent(context, MemberUpgradeActivity::class.java))
        }
        mDatabind.aivIssue.setOnClickListener { showGet(resources.getString(R.string.task_pop_content)) }
        mDatabind.taskType.aivIssueShare.setOnClickListener { showGet(resources.getString(R.string.task_pop_content1)) }
        mDatabind.taskType.aivIssueVideo.setOnClickListener { showGet(resources.getString(R.string.task_pop_content2)) }

        mDatabind.taskType.atvShare.setOnClickListener {
            showShare()
        }

        mDatabind.taskType.atvLook.setOnClickListener {
            val intent = Intent(context, VideoPlayActivity::class.java)
            intent.putExtra("thumbnail", "")
            intent.putExtra("url", videoUrl)
            intent.putExtra("title", "")
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        super.initData()
        mViewModel.taskStatusResult.observe(this) { resultState ->
            parseState(resultState, {
                BooKLogger.d("task 接口成功 = $it")
                mDatabind.pbTask.progress = it.day?.times(5)!!
                mDatabind.atvTaskCount.text =
                    (it.day?.times(5)).toString() + resources.getString(R.string.task_1050旅游宝)
                mDatabind.taskType.atvShareProgress.text = (it.shareTotal).toString() + "/210"
                mDatabind.taskType.atvVideoProgress.text = (it.secretTotal).toString() + "/210"
                mDatabind.taskType.pbShare.progress = it.shareTotal ?: 0
                mDatabind.taskType.pbVideo.progress = it.secretTotal ?: 0
            }, { error ->
                BooKLogger.d("task 接口失败 = ${error.message}")
            })
        }

        mViewModel.videoListResult.observe(this) { resultState ->
            parseState(resultState, {
                videoUrl = it
            })
        }
        mViewModel.userinfoResult.observe(this) { resultState ->
            parseState(resultState, {
                CacheUtil.saveUserInfo(it)
                updateInfo(it)
            })
        }
        mViewModel.taskVideoResult.observe(this) {
            parseState(it, {
                BooKLogger.d("视频任务接口成功")
                mViewModel.userinfo()
            }, { error ->
                BooKLogger.d("视频任务接口失败 = ${error.message}")
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun videoOver(event: EventVideoPlayOver) {
        BooKLogger.d("task接受播放视频结束")
        mViewModel.taskVideo()
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        mViewModel.taskStatus()
        updateInfo(CacheUtil.getUserInfo())
    }

    private fun updateInfo(userInfoBean: UserInfoBean?) {
        if (!CacheUtil.isLogin()) {
            mDatabind.task.visibility = View.GONE
            mDatabind.noLogin.root.visibility = View.VISIBLE
        } else {
            mDatabind.task.visibility = View.VISIBLE
            mDatabind.noLogin.root.visibility = View.GONE
            val userType = userInfoBean?.type ?: 0
            val finishVideo = userInfoBean?.notification?.secret ?: 0
            val finishShare = userInfoBean?.notification?.share ?: 0
            if (userType == 0) {
                mDatabind.taskType.root.visibility = View.GONE
                mDatabind.aivUpgrade.visibility = View.VISIBLE
            } else {
                mDatabind.taskType.root.visibility = View.VISIBLE
                mDatabind.taskType.atvLook.isVisible = finishVideo != 1
                mDatabind.taskType.aivShare.setImageResource(if (finishShare == 1) R.drawable.finish_task_img else R.drawable.task_share)
                mDatabind.taskType.aivVideo.setImageResource(if (finishVideo == 1) R.drawable.finish_task_img else R.drawable.task_video)
                mDatabind.aivUpgrade.visibility = View.GONE
            }
        }
    }

    private var logoutPop: PopupWindow? = null
    private var logoutPopView: View? = null
    private fun showShare() {

        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopView =
            inflater.inflate(R.layout.pop_share, null)
        logoutPop = PopupWindow(
            logoutPopView,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )
        logoutPopView!!.findViewById<ImageView>(R.id.aivWenxin).setOnClickListener {
            logoutPop!!.dismiss()
            mViewModel.taskShare()
            mViewModel.taskShareResult.observe(this) { resultState ->
                parseState(resultState, {
                    mViewModel.taskStatus()
                })
            }

        }
        logoutPopView!!.findViewById<ImageView>(R.id.aivWeibo).setOnClickListener {
            logoutPop!!.dismiss()
            mViewModel.taskShare()
            mViewModel.taskShareResult.observe(this) { resultState ->
                parseState(resultState, {
                    mViewModel.taskStatus()
                })
            }

        }
        logoutPopView!!.findViewById<ImageView>(R.id.aivQzone).setOnClickListener {
            logoutPop!!.dismiss()
            mViewModel.taskShare()
            mViewModel.taskShareResult.observe(this) { resultState ->
                parseState(resultState, {
                    mViewModel.taskStatus()
                })
            }
        }

        logoutPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            logoutPop!!.dismiss()
        }
        logoutPop!!.setBackgroundDrawable(BitmapDrawable())
        logoutPop!!.animationStyle = R.style.common_CustomDialog
        logoutPop!!.showAsDropDown(mDatabind.taskType.atvShare)
    }

    private fun showGet(content: String) {

        val inflater =
            context?.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopView =
            inflater.inflate(R.layout.task_dialog_issue, null)
        logoutPop = PopupWindow(
            logoutPopView,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )

        logoutPopView!!.findViewById<TextView>(R.id.atv_content).text = content
        logoutPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            logoutPop!!.dismiss()
        }
        logoutPop!!.setBackgroundDrawable(BitmapDrawable())
        logoutPop!!.animationStyle = R.style.common_CustomDialog
        logoutPop!!.showAsDropDown(mDatabind.taskType.atvShare)
    }
}