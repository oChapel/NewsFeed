package ua.com.foxminded.newsfeed.ui.news_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.databinding.FragmentNewsListBinding
import ua.com.foxminded.newsfeed.mvi.fragments.HostedFragment
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import ua.com.foxminded.newsfeed.ui.news_list.adapter.NewsListAdapter
import ua.com.foxminded.newsfeed.ui.news_list.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.news_list.state.NewsListScreenState

class NewsListFragment : HostedFragment<
        NewsListContract.View,
        NewsListScreenState,
        NewsListScreenEffect,
        NewsListContract.ViewModel,
        NewsListContract.Host>(),
    NewsListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private var binding: FragmentNewsListBinding? = null
    private val newsListAdapter = NewsListAdapter()

    override fun createModel(): NewsListContract.ViewModel {
        return ViewModelProvider(this, NewsViewModelFactory())[NewsListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding?.newsListRecyclerView
        recyclerView?.apply {
            adapter = newsListAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        binding?.newsSwipeRefresh?.setOnRefreshListener(this)
    }

    override fun setProgressVisibility(isVisible: Boolean) {
        val progressBar = binding?.newsListProgressBar
        val progressTargetAlpha = if (isVisible) 1f else 0f
        val shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        if (progressTargetAlpha != progressBar?.alpha) {
            progressBar?.animate()?.alpha(progressTargetAlpha)
                ?.withStartAction(if (isVisible) Runnable { progressBar.visibility = View.VISIBLE } else null)
                ?.setDuration(shortAnimationDuration.toLong())
                ?.withEndAction(if (isVisible) null else Runnable { progressBar.visibility = View.INVISIBLE })
                ?.start()
        }
    }

    override fun showNews(list: List<Item>) {
        newsListAdapter.setNews(list)
    }

    override fun showToast(stringId: Int) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show()
    }

    override fun onRefresh() {
        model?.reload()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}