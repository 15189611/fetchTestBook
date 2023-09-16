package com.handy.fetchbook.app.network

import com.handy.fetchbook.data.bean.LanguageBean
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * - Author: Charles
 * - Date: 2023/9/10
 * - Description:
 */
interface OtherApiService {
    companion object {
        const val SERVER_URL = "https://cdn.simplelocalize.io/aa208541c839428c9ad67d6737027c10/"
    }

    //获取文案
    @GET("{urlPath}")
    suspend fun getLanguageContent(@Path("urlPath", encoded = false) path: String): LanguageBean
}