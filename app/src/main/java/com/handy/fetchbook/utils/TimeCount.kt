package com.handy.fetchbook.utils

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.widget.TextView


//创建一个倒计时功能类
class TimeCount(
    millisInFuture: Long,
    countDownInterval: Long,
    private var btn_vcode_get: TextView
) :
    CountDownTimer(millisInFuture, countDownInterval) {

    //每次间隔时间的回调，millisUntilFinished剩余的时间，单位是毫秒
    @SuppressLint("SetTextI18n")
    override fun onTick(millisUntilFinished: Long) {
        btn_vcode_get.isClickable = false
        btn_vcode_get.isEnabled = false
        btn_vcode_get.textSize = 10F
        var tmp = millisUntilFinished / 1000
        btn_vcode_get.text = "重新发送(" + tmp + "s" + ")"
    }

    //倒计时结束时的回调
    override fun onFinish() {
        btn_vcode_get.isClickable = true
        btn_vcode_get.isEnabled = true
        btn_vcode_get.textSize = 10F
        btn_vcode_get.text = "获取验证码"
    }
}