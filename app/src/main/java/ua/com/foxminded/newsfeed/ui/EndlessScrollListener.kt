package ua.com.foxminded.newsfeed.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {

    private val visibleThreshold = 5
    private val startingPageIndex = 0
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = false
    private var reverseDirection = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (if (!reverseDirection) dy > 0 else dy < 0) {
            val lm = recyclerView.layoutManager
            val totalItemCount = lm!!.itemCount
            val lastVisibleItemPosition = (lm as LinearLayoutManager).findLastVisibleItemPosition()

            if (totalItemCount < previousTotalItemCount) {
                currentPage = startingPageIndex
                previousTotalItemCount = totalItemCount
                if (totalItemCount == 0) {
                    loading = true
                }
            }

            if (loading && totalItemCount > previousTotalItemCount) {
                loading = false
                previousTotalItemCount = totalItemCount
            }

            if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                currentPage++
                onLoadMore(currentPage, totalItemCount, recyclerView)
                loading = true
            }
        }
    }

    fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    abstract fun onLoadMore(page: Int, totalItemCount: Int, view: RecyclerView)
}