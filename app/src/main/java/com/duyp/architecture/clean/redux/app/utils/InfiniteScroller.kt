package com.duyp.architecture.clean.redux.app.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.Observable

class InfiniteScroller(
    private val visibleThreshold: Int,
    private val canLoadMore: () -> Boolean,
    private val loadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        // dy <= 0 mean scrolling up
        if (dy <= 0) return

        if (!canLoadMore()) return

        recyclerView.layoutManager?.let {
            val totalItemCount = it.itemCount

            val lastVisibleItemPosition = when (it) {
                is StaggeredGridLayoutManager -> {
                    getLastVisibleItem(it.findLastVisibleItemPositions(null))
                }
                is GridLayoutManager ->
                    it.findLastVisibleItemPosition()
                is LinearLayoutManager ->
                    it.findLastVisibleItemPosition()
                else -> 0
            }

            if ((lastVisibleItemPosition + getVisibleThreshold(it)) > totalItemCount) {
                loadMore()
            }
        }
    }

    private fun getVisibleThreshold(layoutManager: RecyclerView.LayoutManager): Int {
        return when (layoutManager) {
            is GridLayoutManager -> visibleThreshold * layoutManager.spanCount
            is StaggeredGridLayoutManager -> visibleThreshold * layoutManager.spanCount
            else -> visibleThreshold
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}

fun RecyclerView.infiniteScroller(visibleThreshold: Int = 5, canLoadMore: () -> Boolean) =
    Observable.create<Unit> {
        val listener = InfiniteScroller(visibleThreshold, canLoadMore) {
            it.onNext(Unit)
        }
        addOnScrollListener(listener)
        it.setCancellable { removeOnScrollListener(listener) }
    }
