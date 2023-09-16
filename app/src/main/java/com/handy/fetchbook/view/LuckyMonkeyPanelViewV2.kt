package com.handy.fetchbook.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.forEachIndexed
import coil.ImageLoader
import coil.request.ImageRequest
import com.handy.fetchbook.R
import com.handy.fetchbook.data.bean.DrawPrizeItemBean

/**
 * - Author: Charles
 * - Date: 2023/9/10
 * - Description:
 */
class LuckyMonkeyPanelViewV2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private var bg1: ImageView? = null
    private var bg2: ImageView? = null

    private var itemView1: PanelItemView? = null
    private var itemView2: PanelItemView? = null
    private var itemView3: PanelItemView? = null
    private var itemView4: PanelItemView? = null
    private var itemView6: PanelItemView? = null
    private var itemView7: PanelItemView? = null
    private var itemView8: PanelItemView? = null
    private var itemView9: PanelItemView? = null

    private var currentIndex = 0
    private var currentTotal = 0
    private var stayIndex = 0

    private var isMarqueeRunning = false
    private var isGameRunning = false
    private var isTryToStop = false

    private val DEFAULT_SPEED = 150
    private val MIN_SPEED = 50
    private var currentSpeed = DEFAULT_SPEED

    private val itemViewArr = arrayOfNulls<ItemView>(8)

    init {
        inflate(context, R.layout.draw_view_tourism, this)
        setupView()
    }

    private fun setupView() {
        bg1 = findViewById(R.id.bg_1)
        bg2 = findViewById(R.id.bg_2)
        itemView1 = findViewById(R.id.item1)
        itemView2 = findViewById(R.id.item2)
        itemView3 = findViewById(R.id.item3)
        itemView4 = findViewById(R.id.item4)
        itemView6 = findViewById(R.id.item6)
        itemView7 = findViewById(R.id.item7)
        itemView8 = findViewById(R.id.item8)
        itemView9 = findViewById(R.id.item9)
        itemViewArr[0] = itemView4
        itemViewArr[1] = itemView1
        itemViewArr[2] = itemView2
        itemViewArr[3] = itemView3
        itemViewArr[4] = itemView6
        itemViewArr[5] = itemView9
        itemViewArr[6] = itemView8
        itemViewArr[7] = itemView7
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //先注释掉
//        startMarquee()
    }

    override fun onDetachedFromWindow() {
        stopMarquee()
        super.onDetachedFromWindow()
    }

    private val currentList = mutableListOf<DrawPrizeItemBean>()

    suspend fun setItems(list: List<DrawPrizeItemBean>?) {
        val drawPrizeItemBeans = list ?: emptyList()
        currentList.clear()
        currentList.addAll(drawPrizeItemBeans)
        drawPrizeItemBeans.forEachIndexed { index, drawPrizeItemBean ->
            loadImage(index + 1, drawPrizeItemBean.image.orEmpty())
        }
    }

    private suspend fun loadImage(position: Int, url: String) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val drawable = imageLoader.execute(request).drawable
        if (position == 1) {
            itemView1?.background = drawable
        } else if (position == 2) {
            itemView2?.background = drawable
        } else if (position == 3) {
            itemView3?.background = drawable
        } else if (position == 4) {
            itemView4?.background = drawable
        } else if (position == 5) {
            itemView6?.background = drawable
        } else if (position == 6) {
            itemView7?.background = drawable
        } else if (position == 7) {
            itemView8?.background = drawable
        } else if (position == 8) {
            itemView9?.background = drawable
        }
    }

    private fun stopMarquee() {
        isMarqueeRunning = false
        isGameRunning = false
        isTryToStop = false
    }

    private fun startMarquee() {
        isMarqueeRunning = true
        Thread {
            while (isMarqueeRunning) {
                try {
                    Thread.sleep(250)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                post {
                    if (bg1 != null && bg2 != null) {
                        if (VISIBLE == bg1!!.visibility) {
                            bg1!!.visibility = GONE
                            bg2!!.visibility = VISIBLE
                        } else {
                            bg1!!.visibility = VISIBLE
                            bg2!!.visibility = GONE
                        }
                    }
                }
            }
        }.start()
    }

    private fun getInterruptTime(): Long {
        currentTotal++
        if (isTryToStop) {
            currentSpeed += 10
            if (currentSpeed > DEFAULT_SPEED) {
                currentSpeed = DEFAULT_SPEED
            }
        } else {
            if (currentTotal / itemViewArr.size > 0) {
                currentSpeed -= 10
            }
            if (currentSpeed < MIN_SPEED) {
                currentSpeed = MIN_SPEED
            }
        }
        return currentSpeed.toLong()
    }

    fun getGameRunning(): Boolean {
        return isGameRunning
    }

    fun setIsGameRunning(isGameRunning: Boolean) {
        this.isGameRunning = isGameRunning
    }

    fun startGame() {
        isGameRunning = true
        isTryToStop = false
        currentSpeed = DEFAULT_SPEED
        Thread {
            while (isGameRunning) {
                try {
                    Thread.sleep(getInterruptTime())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                post {
                    val preIndex = currentIndex
                    currentIndex++
                    if (currentIndex >= itemViewArr.size) {
                        currentIndex = 0
                    }
                    itemViewArr[preIndex]!!.setFocus(false)
                    itemViewArr[currentIndex]!!.setFocus(true)
                    if (isTryToStop && currentSpeed == DEFAULT_SPEED && stayIndex == currentIndex) {
                        isGameRunning = false
                    }
                }
            }
        }.start()
    }

    fun tryToStop(position: Int) {
        stayIndex = position
        isTryToStop = true
    }
}