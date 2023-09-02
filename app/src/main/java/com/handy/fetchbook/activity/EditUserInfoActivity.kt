package com.handy.fetchbook.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.airsaid.pickerviewlibrary.TimePickerView
import com.handy.fetchbook.R
import com.handy.fetchbook.app.util.CacheUtil
import com.handy.fetchbook.basic.ext.addListener
import com.handy.fetchbook.basic.ext.toAvatar
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

    private val avaList = listOf(
        "/preset-avatars/avatar-1.png",
        "/preset-avatars/avatar-2.png",
        "/preset-avatars/avatar-3.png",
        "/preset-avatars/avatar-4.png",
        "/preset-avatars/avatar-5.png",
        "/preset-avatars/avatar-6.png",
        "/preset-avatars/avatar-7.png",
        "/preset-avatars/avatar-8.png",
    )

    private var oldBirthday: String? = null
    private var oldGender: String? = null
    private var oldNikeName: String? = null
    private var oldAvatar: String? = null

    private var birthday: String? = null
    private var gender: String? = null
    private var nickname: String? = null
    private var avatar: String? = null

    override fun initView(savedInstanceState: Bundle?) {
        back.setOnClickListener { finish() }

        ivAvata1.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata1)
            avatar = avaList[0]
            checkChange()
        }
        ivAvata2.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata2)
            avatar = avaList[1]
            checkChange()
        }
        ivAvata3.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata3)
            avatar = avaList[2]
            checkChange()
        }
        ivAvata4.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata4)
            avatar = avaList[3]
            checkChange()
        }
        ivAvata5.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata5)
            avatar = avaList[4]
            checkChange()
        }
        ivAvata6.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata6)
            avatar = avaList[5]
            checkChange()
        }
        ivAvata7.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata7)
            avatar = avaList[6]
            checkChange()
        }
        ivAvata8.setOnClickListener {
            aivHead.setImageResource(R.drawable.me_avata8)
            avatar = avaList[7]
            checkChange()
        }

        editSaveBtn.setOnClickListener {
            val params = mutableMapOf<String, String>()
            params["birthday"] = birthday.orEmpty()
            params["nickname"] = nickname.orEmpty()
            params["gender"] = gender.orEmpty()
            params["avatar"] = avatar.orEmpty()
            mViewModel.updateAvatar(avatar.orEmpty())
            mViewModel.updateProfile(params)
        }

        atvNick.addListener { s, start, before, count ->
            nickname = s.toString()
            checkChange()
            BooKLogger.d("文案-> $s")
        }

        birthdayParent.setOnClickListener {
            val timePick = MyTimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY)
            timePick.setCyclic(true)
            timePick.setRange(1949, 2040)
            timePick.setTime(Date())
            timePick.show()
            timePick.setOnTimeSelectListener {
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                birthday = format.format(it)
                birthdayContext.text = birthday
                checkChange()
                Toast.makeText(this, format.format(it), Toast.LENGTH_SHORT).show()
            }
        }

        sexNan.setOnClickListener {
            gender = "1"
            sexNan.setTextColor(Color.parseColor("#FBB403"))
            sexNv.setTextColor(Color.parseColor("#6A7180"))
            checkChange()
        }
        sexNv.setOnClickListener {
            gender = "0"
            sexNan.setTextColor(Color.parseColor("#6A7180"))
            sexNv.setTextColor(Color.parseColor("#FBB403"))
            checkChange()
        }

        mViewModel.getEditUserInfo()
    }

    override fun createObserver() {
        atvAccount.text = userInfo?.account.orEmpty()
        val membershipAt = userInfo?.membershipAt.orEmpty()
        val split = membershipAt.split("-")
        val first = split[0]
        val second = split[1]
        val third = split[2].substring(0, 2)
        val finallyMember = "$first-$second-$third"
        BooKLogger.d("finallyMember = $finallyMember")

        oldNikeName = userInfo?.nickname.orEmpty()
        nickname = oldNikeName
        oldBirthday = userInfo?.birthday.orEmpty()
        birthday = oldBirthday
        oldAvatar = userInfo?.avatar.orEmpty()
        avatar = oldAvatar
        oldGender = userInfo?.gender.toString()
        gender = oldGender

        atvMembershipAt.text = if (userInfo?.membershipAt?.isNotEmpty() == true) finallyMember else "未开通"
        atvVerify.setTextColor(if (userInfo?.verify == 1) Color.parseColor("#00D125") else Color.parseColor("#f25252"))
        atvVerify.text = if (userInfo?.verify == 1) "已认证" else "未完成"
        locationParent.isVisible = (userInfo?.location?.isNotEmpty() == true)
        atvLocation.text = userInfo?.location.orEmpty()
        atvNick.setText(userInfo?.nickname.orEmpty())
        sexNan.setTextColor(if (oldGender == "1") Color.parseColor("#FBB403") else Color.parseColor("#6A7180"))
        sexNv.setTextColor(if (oldGender == "0") Color.parseColor("#FBB403") else Color.parseColor("#6A7180"))
        aivHead.load(userInfo?.avatar?.toAvatar().orEmpty()) {
            transformations(CircleCropTransformation())
        }
        birthdayContext.text = oldBirthday

        mViewModel.editUserInfoResult.observe(this) {
            parseState(it, { info ->
                BooKLogger.d("编辑个人页面接口成功 = $info")
            }, { error ->
                BooKLogger.d("编辑个人页面 接口失败 = ${error.message}")
            })
        }
        mViewModel.updateProfileResult.observe(this) {
            parseState(it, { info ->
                BooKLogger.d("编辑个人页面 保存接口成功 = $info")
                finish()
            }, { error ->
                BooKLogger.d("编辑个人页面 保存接口失败 = ${error.message}")
            })
        }
        mViewModel.updateAvatarResult.observe(this) {
            parseState(it, { info ->
                BooKLogger.d("编辑个人页面 上传头像成功 = $info")
            }, { error ->
                BooKLogger.d("编辑个人页面 上传头像失败 = ${error.message}")
            })
        }
    }

    private fun checkChange(): Boolean {
        if (oldAvatar != avatar || oldBirthday != birthday || oldNikeName != nickname || oldGender != gender) {
            editSaveBtn.isClickable = true
            editSaveBtn.isEnabled = true
            editSaveBtn.setBackgroundResource(R.drawable.common_btn_login_gradient)
            return true
        }
        editSaveBtn.isClickable = false
        editSaveBtn.isEnabled = false
        editSaveBtn.setBackgroundResource(R.drawable.common_btn_un_save_gradient)
        return false
    }

}