package com.handy.fetchbook.fragment

import android.annotation.SuppressLint
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
import com.handy.fetchbook.R
import com.handy.fetchbook.activity.LoginActivity
import com.handy.fetchbook.activity.MemberUpgradeActivity
import com.handy.fetchbook.activity.RegActivity
import com.handy.fetchbook.activity.VideoPlayActivity
import com.handy.fetchbook.app.base.BaseFragment
import com.handy.fetchbook.app.util.*
import com.handy.fetchbook.databinding.TaskFragmentTaskBinding
import com.handy.fetchbook.viewModel.state.HomeViewModel
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 任务Fragment
 *
 * @author Handy
 * @since 2023/8/1 11:46 下午
 */
class TaskFragment : BaseFragment<HomeViewModel, TaskFragmentTaskBinding>() {
    override fun layoutId(): Int = R.layout.task_fragment_task

    override fun initView(savedInstanceState: Bundle?) {
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

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        if (!CacheUtil.isLogin()) {
            mDatabind.task.visibility = View.GONE
            mDatabind.noLogin.root.visibility = View.VISIBLE
        } else {
            mDatabind.task.visibility = View.VISIBLE
            mDatabind.noLogin.root.visibility = View.GONE
            val userType = CacheUtil.getUserInfo()?.type ?: 0
            if (userType == 0) {
                mDatabind.taskType.root.visibility = View.GONE
                mDatabind.aivUpgrade.visibility = View.VISIBLE
            } else {
                mDatabind.taskType.root.visibility = View.VISIBLE
                mDatabind.aivUpgrade.visibility = View.GONE
            }
        }

        mViewModel.taskStatus()
        mViewModel.taskStatusResult.observe(this) { resultState ->
            parseState(resultState, {
                mDatabind.pbTask.progress = it.day?.times(5)!!
                mDatabind.atvTaskCount.text =
                    (it.day?.times(5)).toString() + resources.getString(R.string.task_1050旅游宝)
                mDatabind.taskType.atvShareProgress.text = (it.share).toString() + "/210"
                mDatabind.taskType.atvVideoProgress.text = (it.secret).toString() + "/210"
                mDatabind.taskType.pbShare.progress = it.share!!
                mDatabind.taskType.pbVideo.progress = it.secret!!

            })
        }

        mDatabind.taskType.atvShare.setOnClickListener {
            showShare()
        }

        mDatabind.taskType.atvLook.setOnClickListener {
            mViewModel.videoList()
            mViewModel.videoListResult.observe(this) { resultState ->
                parseState(resultState, {
                    var intent = Intent(context, VideoPlayActivity::class.java)
                    intent.putExtra("thumbnail", "")
                    intent.putExtra("url", it)
                    intent.putExtra("title", "")
                    startActivity(intent)
                })
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