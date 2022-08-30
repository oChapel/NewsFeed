package ua.com.foxminded.newsfeed.ui.articles.news.feed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import ua.com.foxminded.newsfeed.ui.articles.adapter.ClickEvent
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListContract
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListFragment

class SingleFeedFragment : NewsListFragment() {

    private val args: SingleFeedFragmentArgs by navArgs()

    override fun createModel(): NewsListContract.ViewModel {
        return ViewModelProvider(
            this, NewsViewModelFactory().apply { this.sourceType = args.sourceType }
        )[SingleFeedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            newsAdapter.getClickFlow().collect {
                if (it is ClickEvent.OnItemClicked) {
                    findNavController().navigate(
                        SingleFeedFragmentDirections.actionSingleFeedFragmentToArticleFragment(
                            it.article
                        )
                    )
                } else if (it is ClickEvent.OnBookmarkClicked) {
                    model?.onBookmarkClicked(it.article)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = when (args.sourceType) {
            SourceTypes.NYT_FEED -> getString(R.string.source_name_nyt)
            SourceTypes.CNN_FEED -> getString(R.string.source_name_cnn)
            else -> getString(R.string.source_name_wired)
        }
    }
}
