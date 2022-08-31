package ua.com.foxminded.newsfeed.ui.articles.news

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.databinding.FragmentNewsListBinding
import ua.com.foxminded.newsfeed.models.dto.NewsItem
import ua.com.foxminded.newsfeed.mvi.fragments.HostedFragment
import ua.com.foxminded.newsfeed.ui.EndlessScrollListener
import ua.com.foxminded.newsfeed.ui.articles.adapter.NewsRecyclerAdapter
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState

abstract class NewsListFragment : HostedFragment<
        NewsListContract.View,
        NewsListScreenState,
        NewsListScreenEffect,
        NewsListContract.ViewModel,
        NewsListContract.Host>(),
    NewsListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private var binding: FragmentNewsListBinding? = null
    private var popupWindow: PopupWindow? = null
    protected val newsAdapter = NewsRecyclerAdapter()
    private val scrollListener: EndlessScrollListener = object : EndlessScrollListener() {
        override fun onLoadMore(page: Int, totalItemCount: Int, view: RecyclerView) {
            model?.loadNews(page)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.newsListRecyclerView?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }
        binding?.newsSwipeRefresh?.setOnRefreshListener(this)

        val popupView = View.inflate(context, R.layout.popup_connection_restored, null)
        popupView.setOnClickListener {
            binding?.newsListRecyclerView?.scrollToPosition(0)
            model?.onPopupClicked()
            popupWindow?.dismiss()
        }
        popupWindow = PopupWindow(
            popupView,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            animationStyle = android.R.style.Animation_Dialog
        }
    }

    override fun setProgress(isVisible: Boolean) {
        if (binding?.newsSwipeRefresh?.isRefreshing != isVisible) {
            binding?.newsSwipeRefresh?.isRefreshing = isVisible
        }
    }

    override fun showNews(list: List<NewsItem>) {
        newsAdapter.submitList(ArrayList(list))
    }

    override fun showErrorDialog(error: Throwable) {
        fragmentHost?.showErrorDialog(error)
    }

    override fun showToast(resId: Int) {
        checkIfPopupIsShowing()
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
    }

    override fun showPopupWindow() {
        binding?.newsSwipeRefresh?.let { view ->
            view.post {
                popupWindow?.showAtLocation(
                    binding?.newsSwipeRefresh, Gravity.BOTTOM, 0, view.height
                )
            }
        }
    }

    override fun onRefresh() {
        checkIfPopupIsShowing()
        scrollListener.resetState()
        model?.reload()
    }

    private fun checkIfPopupIsShowing() {
        if (popupWindow?.isShowing == true) {
            popupWindow?.dismiss()
        }
    }

    override fun onDestroyView() {
        checkIfPopupIsShowing()
        binding = null
        super.onDestroyView()
    }
}
