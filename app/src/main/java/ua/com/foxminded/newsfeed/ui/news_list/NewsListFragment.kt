package ua.com.foxminded.newsfeed.ui.news_list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.databinding.FragmentNewsListBinding
import ua.com.foxminded.newsfeed.mvi.fragments.HostedFragment
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import ua.com.foxminded.newsfeed.ui.adapter.NewsRecyclerAdapter
import ua.com.foxminded.newsfeed.ui.news_list.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.news_list.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.Constants

class NewsListFragment : HostedFragment<
        NewsListContract.View,
        NewsListScreenState,
        NewsListScreenEffect,
        NewsListContract.ViewModel,
        NewsListContract.Host>(),
    NewsListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private var binding: FragmentNewsListBinding? = null
    private val newsAdapter = NewsRecyclerAdapter()

    override fun createModel(): NewsListContract.ViewModel {
        return ViewModelProvider(this, NewsViewModelFactory())[NewsListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        setListeners()
    }

    override fun setProgress(isVisible: Boolean) {
        if (binding?.newsSwipeRefresh?.isRefreshing != isVisible) {
            binding?.newsSwipeRefresh?.isRefreshing = isVisible
        }
    }

    override fun showNews(list: List<Item>) {
        newsAdapter.setNews(list)
        (binding?.newsListRecyclerView?.layoutManager as LinearLayoutManager)
            .scrollToPositionWithOffset(0, 0)
    }

    override fun showToast(stringId: Int) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show()
    }

    override fun onItemChanged(article: Item, isArticleInDb: Boolean) {
        newsAdapter.notifyItemChanged(newsAdapter.newsList.indexOf(article), isArticleInDb)
    }

    override fun onRefresh() {
        model?.loadNews()
    }

    private fun setListeners() {
        newsAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_newsListFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable(Constants.KEY_STRING_ARTICLE, it)
                }
            )
        }

        newsAdapter.setOnBookmarkClickListener {
            model?.onBookmarkClicked(it)
        }

        binding?.newsSwipeRefresh?.setOnRefreshListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}