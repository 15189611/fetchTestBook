package com.handy.fetchbook.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.home.SearchBean

/**
 * 文章列表适配器
 *
 * @author Handy
 * @since 2023/8/16 11:26 下午
 */
class SearchAdapter(layoutResId: Int, data: List<SearchBean?>?) :
    BaseQuickAdapter<SearchBean?, BaseViewHolder>(
        layoutResId,
        data as MutableList<SearchBean?>?
    ) {
    override fun convert(holder: BaseViewHolder, item: SearchBean?) {
        holder.itemView
            .findViewById<TextView>(R.id.atvTitle).text = item!!.title
        holder.itemView
            .findViewById<TextView>(R.id.atvContent).text = item.description
        holder.itemView
            .findViewById<TextView>(R.id.atvProvince).text = item.province
    }
}
