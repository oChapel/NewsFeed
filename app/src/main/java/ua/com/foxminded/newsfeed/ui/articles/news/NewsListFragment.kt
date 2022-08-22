package ua.com.foxminded.newsfeed.ui.articles.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.com.foxminded.newsfeed.data.dto.NewsItem
import ua.com.foxminded.newsfeed.databinding.FragmentNewsListBinding
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

    protected var binding: FragmentNewsListBinding? = null
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

    override fun onRefresh() {
        scrollListener.resetState()
        model?.loadNews(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
