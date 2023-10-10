package com.handy.fetchbook.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.annotation.RequiresApi
import com.handy.fetchbook.R
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureParameterStyle
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


object PictureSelectorUtils {
    /**
     * 拍照7.0
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun initPhotoError() {
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }

    fun createFileRequestBody(files: MutableList<File>): MultipartBody {
        val builder = MultipartBody.Builder()
//            .addFormDataPart("tel", tel)
            .setType(MultipartBody.FORM)
        files.forEach {
            builder.addFormDataPart(
                "file",
                it.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), it)
            )
        }
        return builder.build()
    }

    /**
     * 启动相机程序
     */
    fun takeCamera(
        activity: Activity?,
        isBig: Boolean,
        upImageListener: (List<LocalMedia>) -> Unit,
    ) {
        PictureSelector.create(activity).openCamera(PictureMimeType.ofImage())
            .enableCrop(isBig)
            .rotateEnabled(false)
            .withAspectRatio(3, 4)
            .compress(true).forResult(object : OnResultCallbackListener {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    result?.let { upImageListener(it) }
                }

                override fun onCancel() {}
            })
    }

    /**
     * 从相册选择
     */
    inline fun takeMoreAlbum(
        activity: Activity?,
        count: Int,
        crossinline upImageListener: (List<LocalMedia>) -> Unit,
    ) {
        PictureSelector.create(activity).openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine())
            .isCamera(true)
            .enableCrop(false)
            .isWeChatStyle(true)
            .rotateEnabled(false)
            .maxSelectNum(count) // 最大图片选择数量 int
            .withAspectRatio(3, 4)
            .compress(true).forResult(object : OnResultCallbackListener {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    result?.let { upImageListener(it) }
                }

                override fun onCancel() {}
            })
    }

    /**
     * 预览本地和网络图片
     *
     * @param activity
     * @param position
     * @param selectList
     */
    fun pictureSelectorPreviewPicture(
        activity: Activity?,
        position: Int,
        selectList: List<LocalMedia?>?,
    ) {
        PictureSelector.create(activity)
            .themeStyle(R.style.picture_default_style)
            .setPictureStyle(getNumStyle())
            .isNotPreviewDownload(true)
            .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
            .openExternalPreview(position, selectList)
    }

    /**
     * 预览本地视频,预览网络视频卡顿
     *
     * @param activity
     * @param videoPath
     */
    fun pictureSelectorPreviewVideo(activity: Activity?, videoPath: String?) {
        PictureSelector.create(activity)
            .themeStyle(R.style.picture_default_style)
            .isNotPreviewDownload(true)
            .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
            .externalPictureVideo(videoPath)
    }

    /**
     * 适配10.0系统
     */
    fun getPath(localMedia: LocalMedia): String? {
        val path: String
        path = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            localMedia.path
        } else {
            localMedia.androidQToPath
        }
        return path
    }

    private fun getNumStyle(): PictureParameterStyle? {
        // 相册主题
        val mPictureParameterStyle = PictureParameterStyle()
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = true
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = true
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = true
        // 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#000000")
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#000000")
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = Color.parseColor("#000000")
        mPictureParameterStyle.picturePreviewBottomBgColor = Color.parseColor("#000000")
        return mPictureParameterStyle
    }
}