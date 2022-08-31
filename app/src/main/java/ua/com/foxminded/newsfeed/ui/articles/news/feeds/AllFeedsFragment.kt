package ua.com.foxminded.newsfeed.ui.articles.news.feeds

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import ua.com.foxminded.newsfeed.ui.articles.adapter.ClickEvent
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListContract
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListFragment
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListViewModel

class AllFeedsFragment : NewsListFragment() {

    override fun createModel(): NewsListContract.ViewModel {
        return ViewModelProvider(this, NewsViewModelFactory())[NewsListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            newsAdapter.getClickFlow().collect {
                if (it is ClickEvent.OnItemClicked) {
                    findNavController().navigate(
                        AllFeedsFragmentDirections.actionAllFeedsFragmentToArticleFragment(
                            it.article
                        )
                    )
                } else if (it is ClickEvent.OnBookmarkClicked) {
                    model?.onBookmarkClicked(it.article)
                }
            }
        }
    }
}