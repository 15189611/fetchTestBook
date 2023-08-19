package com.handy.fetchbook.adapter

import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.home.Items

/**
 * 文章列表适配器
 *
 * @author Handy
 * @since 2023/8/16 11:26 下午
 */
class ScenicsAdapter ( layoutResId: Int, data: List<Items?>?) : BaseQuickAdapter<Items?, BaseViewHolder>(layoutResId,
    data as MutableList<Items?>?
) {
    override fun convert(holder: BaseViewHolder, item: Items?) {
        holder.itemView
            .findViewById<ImageView>(R.id.aivImg)
            .load(item!!.thumbnail)
        holder.itemView
            .findViewById<ImageView>(R.id.aivImg).scaleType =  ImageView.ScaleType.CENTER_CROP
        holder.itemView
            .findViewById<TextView>(R.id.aivTitle).text = item.title
        if (item.must != 0 && item.plan != 0) {
            holder.itemView.findViewById<ImageView>(R.id.aivLevel1)
                .setImageResource(R.drawable.home_level_y)
            var total = item.plan?.let { item.must?.plus(it) }
            var showCout = (item.must!! / total!!) * 100
            if (showCout > 20) {
                holder.itemView.findViewById<ImageView>(R.id.aivLevel2)
                    .setImageResource(R.drawable.home_level_y)
            }
            if (showCout > 40) {
                holder.itemView.findViewById<ImageView>(R.id.aivLevel3)
                    .setImageResource(R.drawable.home_level_y)
            }
            if (showCout > 60) {
                holder.itemView.findViewById<ImageView>(R.id.aivLevel4)
                    .setImageResource(R.drawable.home_level_y)
            }

            if (showCout > 80) {
                holder.itemView.findViewById<ImageView>(R.id.aivLevel5)
                    .setImageResource(R.drawable.home_level_y)
            }

        }
    }



}