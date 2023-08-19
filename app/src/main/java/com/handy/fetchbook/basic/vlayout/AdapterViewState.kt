package com.handy.fetchbook.basic.vlayout

import androidx.recyclerview.widget.RecyclerView

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
interface AdapterViewState<VH : RecyclerView.ViewHolder> {

    /**
     * Called when a view created by this adapter has been attached to a window.
     *
     *
     * This can be used as a reasonable signal that the view is about to be seen
     * by the user. If the adapter previously freed any resources in
     * [onViewDetachedFromWindow][.onViewDetachedFromWindow]
     * those resources should be restored here.
     *
     * @param holder Holder of the view being attached
     */
    fun onViewAttachedToWindow(holder: VH)

    /**
     * Called when a view created by this adapter has been detached from its window.
     *
     *
     * Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.
     *
     * @param holder Holder of the view being detached
     */
    fun onViewDetachedFromWindow(holder: VH)

}