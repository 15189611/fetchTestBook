package com.handy.fetchbook.viewModel.state

import androidx.lifecycle.MutableLiveData
import com.handy.fetchbook.app.network.apiService
import com.handy.fetchbook.data.bean.EditUserInfoBean
import com.handy.fetchbook.data.bean.expo.ExpoDetailsBean
import com.handy.fetchbook.data.bean.expo.ExpoListBean
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

    var getExpoListResult = MutableLiveData<ResultState<ExpoListBean>>()
    fun getExpoList(region: String, page: Int) {
        request({ apiService.list(region, page) }, getExpoListResult)
    }

    var groupSharingBean = MutableLiveData<ResultState<GroupSharingBean>>()
    fun grouplist(page: Int) {
        request({ apiService.grouplist(page) }, groupSharingBean)
    }

    var socialMediaResult = MutableLiveData<ResultState<List<SocialMediaBean>>>()
    fun socialMedia() {
        request({ apiService.socialMedia() }, socialMediaResult)
    }

    var messageResult = MutableLiveData<ResultState<SystemInfoBean>>()
    fun message(type: Int, page: Int) {
        request({ apiService.message(type, page) }, messageResult)
    }

    var noticeResult = MutableLiveData<ResultState<NoticeBean>>()
    fun announcements(page: Int) {
        request({ apiService.announcements(page) }, noticeResult)
    }

    var searchScenicResult = MutableLiveData<ResultState<List<SearchBean>>>()
    fun searchScenic(key: String, page: Int) {
        request({ apiService.searchScenic(key, page) }, searchScenicResult)
    }

    var expoDetailsResult = MutableLiveData<ResultState<ExpoDetailsBean>>()
    fun details(id: Int) {
        request({ apiService.details(id) }, expoDetailsResult)
    }

    var commentExpoResult = MutableLiveData<ResultState<ArrayList<String>>>()
    fun commentExpo(expo_id: String, comment: String, rating: Int) {
        request({ apiService.commentExpo(expo_id, comment, rating) }, commentExpoResult)
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
}