package com.handy.fetchbook.basic.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.handy.fetchbook.basic.listener.OnViewRefreshListener;
import com.handy.fetchbook.basic.listener.OnViewRefreshLoadMoreListener;
import com.handy.fetchbook.basic.manager.SmartLayoutManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
public class ViewSmartLayout extends SmartRefreshLayout {
    // 是否使用 SmartLoadMoreViewNew 代替 SmartLoadMoreView
    private boolean mEnableLoadMoreWebpAnima;

    public ViewSmartLayout(Context context) {
        super(context);
        init();
    }

    public ViewSmartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        SmartLayoutManager.init();
        setEnableLoadMoreWhenContentNotFull(false);
        setEnableOverScrollDrag(false);
        setEnableOverScrollBounce(false);
        setEnableAutoLoadMore(true);

        setEnableRefresh(false);
        setEnableLoadMore(false);
        setPrimaryColors(Color.WHITE);
    }

    public void onRefreshComplete() {
        onRefreshComplete(false);
    }

    public void onRefreshComplete(boolean canLoadMore) {
        finishRefresh();
        setNoMoreData(!canLoadMore);
    }

    public void onLoadMoreComplete(boolean canLoadMore) {
        int delayed = 0;
        if (!canLoadMore) {
            delayed = 1000;
        }
        finishLoadMore(delayed, true, !canLoadMore);
        setNoMoreData(!canLoadMore);
    }

    public void setPrimaryColor(int color) {
        setPrimaryColors(color);
    }

    public void setDuRefreshLoadMoreListener(OnViewRefreshLoadMoreListener onDuRefreshLoadMoreListener) {
        setEnableRefresh(true);
        setEnableLoadMore(true);
        setOnRefreshLoadMoreListener(onDuRefreshLoadMoreListener);
    }

    public void setDuRefreshListener(OnViewRefreshListener onDuRefreshListener) {
        setEnableRefresh(true);
        setOnRefreshListener(onDuRefreshListener);
    }

    public void setDuLoadMoreListener(OnViewLoadMoreListener onDuLoadMoreListener) {
        setEnableLoadMore(true);
        setOnLoadMoreListener(onDuLoadMoreListener);
    }

    @Deprecated
    public void changeStatus(boolean isRefresh, boolean canLoadMore) {
        if (isRefresh) {
            onRefreshComplete(canLoadMore);
        } else {
            onLoadMoreComplete(canLoadMore);
        }
    }

    public boolean isEnableRefresh() {
        return mEnableRefresh && !mEnablePureScrollMode;
    }

    public boolean isEnableLoadMore() {
        return mEnableLoadMore && !mEnablePureScrollMode;
    }

    public boolean isRefreshing() {
        return mState.isOpening;
    }

    public boolean isEnableLoadMoreWebpAnima() {
        return mEnableLoadMoreWebpAnima;
    }

    public void enableLoadMoreWebpAnima(boolean enable) {
        this.mEnableLoadMoreWebpAnima = enable;
    }

    public boolean isEnableOverScrollBounce() {
        return mEnableOverScrollBounce;
    }

    public boolean isEnablePureScrollMode() {
        return mEnablePureScrollMode;
    }

    public boolean autoRefresh(int delayed, final int duration, final float dragRate) {
        return autoRefresh(delayed, duration, dragRate, false);
    }

    public void onRefreshLoadMoreComplete(boolean isRefresh, boolean canLoadMore) {
        if (isRefresh) {
            onRefreshComplete(canLoadMore);
        } else {
            onLoadMoreComplete(canLoadMore);
        }
    }
}

