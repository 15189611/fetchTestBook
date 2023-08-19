package com.handy.fetchbook.basic.vlayout

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.VirtualLayoutManager

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
class VLVirtualLayoutManager @JvmOverloads constructor(
    context: Context,
    orientation: Int = VERTICAL,
    reverseLayout: Boolean = false
) : VirtualLayoutManager(context, orientation, reverseLayout) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try { // try catch一下
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
            RecyclerViewConfig.handleCatchError(e)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            RecyclerViewConfig.handleCatchError(e)
        }
    }
}