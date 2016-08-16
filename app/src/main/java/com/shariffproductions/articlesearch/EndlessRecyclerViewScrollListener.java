package com.shariffproductions.articlesearch;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold = 8;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loadInProgress = true;
    private int startingPageIndex = 0;

    RecyclerView.LayoutManager mLayoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = getLastVisibleItemPosition();
        int totalItemCount = mLayoutManager.getItemCount();

        if (totalItemCount < previousTotalItemCount) {
            resetInvalidatedList(totalItemCount);
        }

        if (loadInProgress && (totalItemCount > previousTotalItemCount)) {
            markLoadingAsFinished(totalItemCount);
        }

        if (!loadInProgress && thresholdReached(lastVisibleItemPosition, totalItemCount)) {
            reload(totalItemCount);
        }
    }

    private int getLastVisibleItemPosition() {
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            return getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        return 0;
    }

    private boolean thresholdReached(int lastVisibleItemPosition, int totalItemCount) {
        return ((lastVisibleItemPosition + visibleThreshold) > totalItemCount);
    }

    private void reload(int totalItemCount) {
        currentPage++;
        onLoadMore(currentPage, totalItemCount);
        loadInProgress = true;
    }

    private void markLoadingAsFinished(int totalItemCount) {
        loadInProgress = false;
        previousTotalItemCount = totalItemCount;
    }

    private void resetInvalidatedList(int totalItemCount) {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = totalItemCount;
        if (totalItemCount == 0) {
            this.loadInProgress = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);
}
