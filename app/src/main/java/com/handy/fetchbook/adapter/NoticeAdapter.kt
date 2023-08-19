package com.handy.fetchbook.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.home.NoticeItems

/**
 * 文章列表适配器
 *
 * @author Handy
 * @since 2023/8/16 11:26 下午
 */
class NoticeAdapter(layoutResId: Int, data: List<NoticeItems?>?) :
    BaseQuickAdapter<NoticeItems?, BaseViewHolder>(
        layoutResId,
        data as MutableList<NoticeItems?>?
    ) {
    override fun convert(holder: BaseViewHolder, item: NoticeItems?) {
        holder.itemView
            .findViewById<TextView>(R.id.atvTitle).text = item!!.title
        holder.itemView
            .findViewById<TextView>(R.id.atvTime).text = item.createdAt
    }
}
