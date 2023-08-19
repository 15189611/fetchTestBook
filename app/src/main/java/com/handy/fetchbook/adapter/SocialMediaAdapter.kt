package com.handy.fetchbook.adapter

import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.home.SocialMediaBean

/**
 * 文章列表适配器
 *
 * @author Handy
 * @since 2023/8/16 11:26 下午
 */
class SocialMediaAdapter(layoutResId: Int, data: List<SocialMediaBean?>?) :
    BaseQuickAdapter<SocialMediaBean?, BaseViewHolder>(
        layoutResId,
        data as MutableList<SocialMediaBean?>?
    ) {
    override fun convert(holder: BaseViewHolder, item: SocialMediaBean?) {
        holder.itemView
            .findViewById<ImageView>(R.id.aivImg)
            .load(item!!.thumbnail)
    }
}
