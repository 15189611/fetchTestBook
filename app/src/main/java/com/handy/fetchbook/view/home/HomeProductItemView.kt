package com.handy.fetchbook.view.home

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.ImageView
import com.handy.fetchbook.R
import com.handy.fetchbook.basic.AbsModuleView
import com.handy.fetchbook.data.bean.home.Items
import kotlinx.android.synthetic.main.home_item_senics.view.aivTitle
import coil.load
import com.handy.fetchbook.activity.HomeDetailActivity
import kotlinx.android.synthetic.main.home_item_senics.view.aivImg
import kotlinx.android.synthetic.main.home_item_senics.view.aivLevel1
import kotlinx.android.synthetic.main.home_item_senics.view.aivLevel2
import kotlinx.android.synthetic.main.home_item_senics.view.aivLevel3
import kotlinx.android.synthetic.main.home_item_senics.view.aivLevel4
import kotlinx.android.synthetic.main.home_item_senics.view.aivLevel5

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
class HomeProductItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbsModuleView<Items>(context, attrs) {

    override fun getLayoutId(): Int {
        return R.layout.home_item_senics
    }

    override fun onChanged(model: Items) {
        super.onChanged(model)
        aivImg.scaleType = ImageView.ScaleType.CENTER_CROP
        aivImg.load(model.thumbnail.orEmpty())
        aivTitle.text = model.title
        val plan = model.plan ?: 0
        val must = model.must ?: 0
        val total = plan + must
        val showCount = (must / total) * 100
        aivLevel1.setImageResource(R.drawable.home_level_y)
        if (showCount > 20) {
            aivLevel2.setImageResource(R.drawable.home_level_y)
        }
        if (showCount > 40) {
            aivLevel3.setImageResource(R.drawable.home_level_y)
        }
        if (showCount > 60) {
            aivLevel4.setImageResource(R.drawable.home_level_y)
        }
        if (showCount > 80) {
            aivLevel5.setImageResource(R.drawable.home_level_y)
        }
        this.setOnClickListener {
            val intent = Intent(context, HomeDetailActivity::class.java)
            intent.putExtra("id", model.uuid)
            context.startActivity(intent)
        }
    }
}