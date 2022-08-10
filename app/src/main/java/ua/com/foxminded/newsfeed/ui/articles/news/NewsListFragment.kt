package ua.com.foxminded.newsfeed.ui.articles.news

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.data.Article
import ua.com.foxminded.newsfeed.databinding.FragmentNewsListBinding
import ua.com.foxminded.newsfeed.mvi.fragments.HostedFragment
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import ua.com.foxminded.newsfeed.ui.article.ArticleFragment
import ua.com.foxminded.newsfeed.ui.articles.adapter.ClickEvent
import ua.com.foxminded.newsfeed.ui.articles.adapter.NewsRecyclerAdapter
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState

class NewsListFragment : HostedFragment<
        NewsListContract.View,
        NewsListScreenState,
        NewsListScreenEffect,
        NewsListContract.ViewModel,
        NewsListContract.Host>(),
    NewsListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private var binding: FragmentNewsListBinding? = null
    private val newsAdapter = NewsRecyclerAdapter(false)

    override fun createModel(): NewsListContract.ViewModel {
        return ViewModelProvider(this, NewsViewModelFactory())[NewsListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launch {
            newsAdapter.getClickFlow().collect {
                if (it is ClickEvent.OnItemClicked) {
                    findNavController().navigate(
                        R.id.action_newsListFragment_to_articleFragment,
                        Bundle().apply {
                            putSerializable(ArticleFragment.KEY_STRING_ARTICLE, it.article)
                        }
                    )
                } else if (it is ClickEvent.OnBookmarkClicked) {
                    model?.onBookmarkClicked(it.article)
                }
            }
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
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.news_list_options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.option_menu_item_refresh -> {
                        model?.loadNews()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding?.newsListRecyclerView?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding?.newsSwipeRefresh?.setOnRefreshListener(this)
    }

    override fun setProgress(isVisible: Boolean) {
        if (binding?.newsSwipeRefresh?.isRefreshing != isVisible) {
            binding?.newsSwipeRefresh?.isRefreshing = isVisible
        }
    }

    override fun showNews(list: List<Article>) {
        newsAdapter.submitList(list)
    }

    override fun showErrorDialog(error: Throwable) {
        fragmentHost?.showErrorDialog(error)
    }

    override fun onRefresh() {
        model?.loadNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
