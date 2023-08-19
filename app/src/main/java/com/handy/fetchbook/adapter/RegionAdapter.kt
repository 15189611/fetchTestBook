package com.handy.fetchbook.adapter

import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.expo.Items
import per.wsj.library.AndRatingBar

/**
 * 文章列表适配器
 *
 * @author Handy
 * @since 2023/8/16 11:26 下午
 */
class RegionAdapter (layoutResId: Int, data: List<Items?>?) : BaseQuickAdapter<Items?, BaseViewHolder>(layoutResId,
    data as MutableList<Items?>?
) {
    override fun convert(holder: BaseViewHolder, item: Items?) {
        holder.itemView
            .findViewById<ImageView>(R.id.aivImg)
            .load(item!!.logo)
        holder.itemView
            .findViewById<TextView>(R.id.name).text = item.title
        holder.itemView
            .findViewById<TextView>(R.id.atvRating).text = item.rating
        holder.itemView
            .findViewById<AndRatingBar>(R.id.arbRating).rating = item.rating?.toFloat()!!
    }



}