package com.handy.fetchbook.viewModel.state

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.data.bean.group.GroupSharingBean
import com.handy.fetchbook.data.bean.home.*
import com.handy.fetchbook.data.bean.me.HelpCenterBean
import com.handy.fetchbook.data.bean.me.UserInfoBean
import com.handy.fetchbook.data.bean.me.WalletBean
import com.handy.fetchbook.data.bean.model.BaseApiModel
import com.handy.fetchbook.data.bean.task.TaskStatusBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.ext.requestNoCheck
import me.hgj.jetpackmvvm.state.ResultState
import com.handy.fetchbook.app.network.postBodyOf
import com.handy.fetchbook.basic.ext.*
import com.handy.fetchbook.data.bean.*
import com.handy.fetchbook.data.bean.expo.*

class HomeViewModel : BaseViewModel() {

    var userinfoResult = MutableLiveData<ResultState<UserInfoBean>>()
    fun userinfo() {
        request({ apiService.userInfo() }, userinfoResult)
    }

    var walletResult = MutableLiveData<ResultState<WalletBean>>()
    fun wallet() {
        request({ apiService.wallet() }, walletResult)
    }

    var helpCenterResult = MutableLiveData<ResultState<List<HelpCenterBean>>>()
    fun helpCenter() {
        request({ apiService.helpCenter() }, helpCenterResult)
    }

    var buyMembershipResult = MutableLiveData<ResultState<Any>>()
    fun buyMembership(type: Int) {
        request({ apiService.buyMembership(type) }, buyMembershipResult)
    }

    var totalResult = MutableLiveData<ResultState<InfoCenterTotalBean>>()
    fun total() {
        request({ apiService.total() }, totalResult)
    }

    var getExpoListResult = MutableLiveData<MyResultState<ExpoListBean>>()
    fun getExpoList(isRefresh: Boolean, region: String, page: Int) {
        requestForFresh({ apiService.list(region, if (isRefresh) 1 else page) }, getExpoListResult, isRefresh)
    }

    var expoSearchListResult = MutableLiveData<MyResultState<Data>>()
    fun expoSearchList(isRefresh: Boolean, keyword: String, page: Int) {
        requestForFresh({ apiService.expoSearchList(keyword, if (isRefresh) 1 else page) }, expoSearchListResult, isRefresh)
    }

    var getExpoBannerResult = MutableLiveData<ResultState<Any>>()
    fun getExpoBanner() {
        request({ apiService.getExpoBanner() }, getExpoBannerResult)
    }

    var groupSharingBean = MutableLiveData<MyResultState<GroupSharingBean>>()
    fun grouplist(isRefresh: Boolean = true, page: Int) {
        requestForFresh({ apiService.grouplist(if (isRefresh) 1 else page) }, groupSharingBean, isRefresh)
    }

    var socialMediaResult = MutableLiveData<ResultState<List<SocialMediaBean>>>()
    fun socialMedia() {
        request({ apiService.socialMedia() }, socialMediaResult)
    }

    var messageResult = MutableLiveData<MyResultState<SystemInfoBean>>()
    fun message(isRefresh: Boolean, type: Int, page: Int) {
        requestForFresh({ apiService.message(type, if (isRefresh) 1 else page) }, messageResult, isRefresh)
    }

    var messageReadResult = UnPeekLiveData<ResultState<BaseApiModel>>()
    fun messageRead(uuid: String) {
        requestNoCheck({ apiService.messageRead(uuid) }, messageReadResult)
    }

    var noticeResult = MutableLiveData<MyResultState<NoticeBean>>()
    fun announcements(isRefresh: Boolean, page: Int) {
        requestForFresh({ apiService.announcements(if (isRefresh) 1 else page) }, noticeResult, isRefresh)
    }

    var noticeReadResult = UnPeekLiveData<ResultState<BaseApiModel>>()
    fun noticeRead(uuid: String) {
        requestNoCheck({ apiService.noticeRead(uuid) }, noticeReadResult)
    }

    var searchScenicResult = MutableLiveData<ResultState<List<SearchBean>>>()
    fun searchScenic(key: String, page: Int) {
        request({ apiService.searchScenic(key, page) }, searchScenicResult)
    }

    var expoDetailsResult = MutableLiveData<ResultState<ExpoDetailsBean>>()
    fun details(id: Int) {
        request({ apiService.details(id) }, expoDetailsResult)
    }

    var commentExpoResult = MutableLiveData<MyResultState<Any>>()
    fun commentExpo(expo_id: String, comment: String, rating: Int) {
        requestForFresh({ apiService.commentExpo(expo_id, comment, rating) }, commentExpoResult)
    }

    var drawResult = UnPeekLiveData<ResultState<BaseApiModel>>()
    fun draw() {
        requestNoCheck({ apiService.draw() }, drawResult)
    }

    var taskStatusResult = UnPeekLiveData<ResultState<TaskStatusBean>>()
    fun taskStatus() {
        request({ apiService.taskStatus() }, taskStatusResult)
    }

    var taskShareResult = UnPeekLiveData<ResultState<BaseApiModel>>()
    fun taskShare() {
        requestNoCheck({ apiService.taskShare() }, taskShareResult)
    }

    var taskVideoResult = UnPeekLiveData<ResultState<BaseApiModel>>()
    fun taskVideo() {
        requestNoCheck({ apiService.taskVideo() }, taskVideoResult)
    }

    var videoListResult = UnPeekLiveData<ResultState<String>>()
    fun videoList() {
        request({ apiService.videoList() }, videoListResult)
    }

    val editUserInfoResult = MutableLiveData<ResultState<EditUserInfoBean>>()
    fun getEditUserInfo() {
        request({ apiService.getUserVerified() }, editUserInfoResult)
    }

    val updateProfileResult = MutableLiveData<ResultState<Any>>()
    fun updateProfile(params: Map<String, Any>) {
        val body = postBodyOf(params)
        request({ apiService.updateProfile(body) }, updateProfileResult)
    }

    val updateAvatarResult = MutableLiveData<ResultState<Any>>()
    fun updateAvatar(avatar: String) {
        val body = postBodyOf("avatar" to avatar)
        request({ apiService.updateAvatar(body) }, updateAvatarResult)
    }

    val investResult = MutableLiveData<ResultState<Any>>()
    fun investInfo(params: Map<String, Any>) {
        val body = postBodyOf(params)
        request({ apiService.invest(body) }, investResult)
    }

    val openRedPacketResult = MutableLiveData<MyResultState<DrawOpenRedPacketBean>>()
    fun openRedPacket(params: Map<String, Any>) {
        val body = postBodyOf(params)
        requestForFresh({ apiService.openRedPacket(body) }, openRedPacketResult)
    }

    val priceListResult = MutableLiveData<ResultState<List<DrawPrizeItemBean>>>()
    fun getPrizeList() {
        request({ apiService.getPrizeList() }, priceListResult)
    }

    val drawWinnersResult = MutableLiveData<ResultState<List<String>>>()
    fun getWinners() {
        request({ apiService.getWinners() }, drawWinnersResult)
    }
}