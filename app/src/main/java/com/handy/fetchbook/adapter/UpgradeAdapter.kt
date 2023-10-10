package com.handy.fetchbook.adapter

import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.group.Items

class UpgradeAdapter  (layoutResId: Int, data: List<Items?>?) : BaseQuickAdapter<Items?, BaseViewHolder>(layoutResId,
    data as MutableList<Items?>?
) {
    override fun convert(holder: BaseViewHolder, item: Items?) {
        holder.itemView
            .findViewById<ImageView>(R.id.aivImg)
            .load(item!!.image)
        holder.itemView
            .findViewById<TextView>(R.id.name).text = item.name
        holder.itemView
            .findViewById<TextView>(R.id.name1).text = item.price+" 美元"
    }



}