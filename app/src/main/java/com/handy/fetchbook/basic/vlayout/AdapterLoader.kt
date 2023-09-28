package com.handy.fetchbook.basic.vlayout

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */

import android.view.View
import android.view.ViewGroup
import com.handy.fetchbook.basic.vlayout.holder.DuViewHolder

/**
 * Created by Joe on 2018-11-20
 * Email: lovejjfg@gmail.com
 */
interface AdapterLoader<Item> {
    /**
     * You can call this method to add items to RecycleView,if you want to append items,you should call
     * [.appendItems], Maybe You should call setTotalCount() before setItems() .
     *
     * @param items the items you want to add
     */
    fun setItems(items: List<Item>)

    /**
     * You can call this method to add items to RecycleView.It's like setItems and
     * can clear already exist list in RecyclerView when you give items is null or empty
     *
     * @param items the items you want to add
     */
    fun setItemsSafely(items: List<Item>?)

    /**
     * clear current data.
     */
    fun clearItems()

    /**
     * clear current data.
     */
    fun clearItems(notify: Boolean)

    /**
     * @param items the items you want to add.
     */
    fun appendItems(items: List<Item>)
    /**
     * @param items the items you want to add, support null check inner.
     */
    fun appendItemsSafely(items: List<Item>?)

    /**
     * insert the new list items within the start Position.
     *
     * @param items new list to insert
     * @param startPos the start position
     */
    fun insertItems(items: List<Item>, startPos: Int)
    /**
     * insert the new list items within the start Position, support null check inner.
     *
     * @param items new list to insert
     * @param startPos the start position
     */
    fun insertItemsSafely(items: List<Item>?, startPos: Int)

    /**
     * append or set the new list items.
     */
    fun autoInsertItems(items: List<Item>)
    /**
     * append or set the new list items, support null check inner.
     */
    fun autoInsertItemsSafely(items: List<Item>?)

    /**
     * check the position is illegal.
     *
     * @return true the position is out of [0,list.size()-1] ,else legal.
     */
    fun checkIllegalPosition(position: Int): Boolean

    /**
     * remove the specified position in the list. If this method throw RecyclerView Exception when you delete the
     * last one.
     * consider about using the [com.shizhuang.duapp.common.recyclerview.manager.FixedGridLayoutManager] or
     * [com.shizhuang.duapp.common.recyclerview.manager.FixedLinearLayoutManager] to avoid crash when you want to delete the lasted item.
     *
     * @param position he specified position to remove
     * @return if successful return the removed object,otherwise null
     */
    fun removeItem(position: Int): Item?

    /**
     * remove the specified position in the list. If this method throw RecyclerView Exception when you delete the
     * last one.
     * consider about using the [com.shizhuang.duapp.common.recyclerview.manager.FixedGridLayoutManager] or
     * [com.shizhuang.duapp.common.recyclerview.manager.FixedLinearLayoutManager] to avoid crash when you want to delete the lasted item.
     *
     * @param position he specified position to remove
     * @return if successful return true,otherwise false
     */
    fun removeItem(item: Item): Boolean

    /**
     * call this method to update the item Holder's itemView.
     *
     * @return true find the item in current list, false otherwise.
     */
    fun updateItem(item: Item, payload: Any? = null): Boolean

    /**
     * call this method to update the item Holder's itemView with empty payload
     *
     * @return true find the item in current list, false otherwise.
     */
    fun updateItemWithEmptyPayload(item: Item): Boolean

    /**
     * get the data of list in this RecyclerView.
     * return the data in specified position, or null.
     *
     * @param position the specified position
     */
    fun getItem(position: Int): Item?

    /**
     * remove the specified position in the list.
     *
     * @param position he specified position to remove
     */
    fun insertItem(position: Int, item: Item)

    /**
     * @param position the current pos .
     * @return the current Type.
     */
    fun getItemViewTypes(position: Int): Int

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the DuViewHolder#itemView to reflect the item at
     * the given position.
     *
     * @param holder current holder.
     * @param position current pos.
     */
    fun onViewHolderBind(holder: DuViewHolder<Item>, position: Int)

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the DuViewHolder#itemView to reflect the item at
     * the given position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full
     * update.
     */
    fun onViewHolderBind(holder: DuViewHolder<Item>, position: Int, payloads: List<Any>)

    /**
     * Called when RecyclerView needs a new [DuViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onViewHolderBind] (ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    fun onViewHolderCreate(parent: ViewGroup, viewType: Int): DuViewHolder<Item>

    /**
     * handle the holder itemview click.
     *
     * @param holder the holder that click
     * @param position the position of holder in RecyclerView
     * @param item the data of holder.
     */
    fun performClick(holder: DuViewHolder<Item>, position: Int, item: Item)

    /**
     * handle the holder itemview long click.
     *
     * @param holder the holder that click
     * @param position the position of holder in RecyclerView
     * @param item the data of holder.
     */
    fun performLongClick(holder: DuViewHolder<Item>, position: Int, item: Item): Boolean

    /**
     * call this method to get the first position of the viewType
     *
     * @param viewType the viewType you set ,default is 0
     * @return the index of list
     */
    fun findFirstPositionOfType(viewType: Int): Int

    /**
     * call this method to get the first position of the viewType start with the offsetPosition end to list end.
     *
     * @param viewType the viewType you set ,default is 0
     * @param offsetPosition the position to offset.
     * @return the index of list
     */
    fun findFirstPositionOfType(viewType: Int, offsetPosition: Int): Int

    /**
     * call this method to get the last position of the viewType
     *
     * @param viewType the viewType you set ,default is 0
     * @return the index of list
     */
    fun findLastPositionOfType(viewType: Int): Int

    /**
     * call this method to get the last position of the viewType start with the offsetPosition end to 0.
     *
     * @param viewType the viewType you set ,default is 0
     * @param offsetPosition the position to offset.
     * @return the index of list
     */
    fun findLastPositionOfType(viewType: Int, offsetPosition: Int): Int

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    fun setOnItemClickListener(listener: OnItemClickListener<Item>?)

    fun getSpanSize(position: Int): Int

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked.
     *
     * @param listener The callback that will be invoked.
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener<Item>?)
}

/**
 * Interface definition for a callback to be invoked when an item in this
 * Adapter has been clicked.
 */
typealias OnItemLongClickListener<T> = (holder: DuViewHolder<T>, position: Int, item: T) -> Boolean

/**
 * Interface definition for a callback to be invoked when an item in this
 * Adapter has been long clicked.
 */
typealias OnItemClickListener<T> = (holder: DuViewHolder<T>, position: Int, item: T) -> Unit

/**
 * clickListener of RecyclerView child view
 */
typealias OnItemChildClickListener<T> = (holder: DuViewHolder<T>, position: Int, view: View) -> Unit

/**
 * long clickListener of RecyclerView child View
 */
typealias OnItemChildLongClickListener<T> = (holder: DuViewHolder<T>, position: Int, view: View) -> Unit
