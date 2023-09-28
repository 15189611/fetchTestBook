package com.handy.fetchbook.basic.listener;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
public abstract class OnViewRefreshLoadMoreListener implements OnRefreshLoadMoreListener {

    @Override
    public final void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        onRefreshLoadMore(false, refreshLayout);
    }

    @Override
    public final void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefreshLoadMore(true, refreshLayout);
    }

    public abstract void onRefreshLoadMore(boolean isRefresh, RefreshLayout refreshLayout);
}
