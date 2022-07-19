package ua.com.foxminded.newsfeed.ui.news_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.com.foxminded.newsfeed.R
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

        binding?.newsListRecyclerView?.apply {
            adapter = newsListAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        newsListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(KEY_STRING_ARTICLE, it)
            }
            findNavController().navigate(
                R.id.action_newsListFragment_to_articleFragment,
                bundle
            )
        }

        binding?.newsSwipeRefresh?.setOnRefreshListener(this)
    }

    override fun setProgress(isVisible: Boolean) {
        if (binding?.newsSwipeRefresh?.isRefreshing != isVisible) {
            binding?.newsSwipeRefresh?.isRefreshing = isVisible
        }
    }

    override fun showNews(list: List<Item>) {
        newsListAdapter.setNews(list)
    }

    override fun showToast(stringId: Int) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show()
    }

    override fun onRefresh() {
        model?.loadNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val KEY_STRING_ARTICLE = "article"
    }
}