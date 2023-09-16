package com.handy.fetchbook.adapter

import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.expo.ExpoComments
import per.wsj.library.AndRatingBar

/**
 * 文章列表适配器
 *
 * @author Handy
 * @since 2023/8/16 11:26 下午
 */
class FeedbackAdapter (layoutResId: Int, data: List<ExpoComments?>?) : BaseQuickAdapter<ExpoComments?, BaseViewHolder>(layoutResId,
    data as MutableList<ExpoComments?>?
) {
    override fun convert(holder: BaseViewHolder, item: ExpoComments?) {
        holder.itemView
            .findViewById<TextView>(R.id.atvRating).text = item!!.rating.toString()
        holder.itemView
            .findViewById<AndRatingBar>(R.id.arbRating).rating = item.rating?.toFloat()!!
        holder.itemView
            .findViewById<TextView>(R.id.atvContent).text = item.comment
    }



}