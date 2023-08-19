package com.handy.fetchbook.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.home.SystemInfoItems

/**
 * 文章列表适配器
 *
 * @author Handy
 * @since 2023/8/16 11:26 下午
 */
class SystemInfoAdapter(layoutResId: Int, data: List<SystemInfoItems?>?) :
    BaseQuickAdapter<SystemInfoItems?, BaseViewHolder>(
        layoutResId,
        data as MutableList<SystemInfoItems?>?
    ) {
    override fun convert(holder: BaseViewHolder, item: SystemInfoItems?) {
        holder.itemView
            .findViewById<TextView>(R.id.atvTitle).text = item!!.title
        holder.itemView
            .findViewById<TextView>(R.id.atvContent).text = item.content
        holder.itemView
            .findViewById<TextView>(R.id.atvTime).text = item.createdAt
    }
}
