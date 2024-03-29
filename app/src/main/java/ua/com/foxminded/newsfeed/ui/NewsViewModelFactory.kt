package ua.com.foxminded.newsfeed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.models.NewsRepository
import ua.com.foxminded.newsfeed.models.network.listener.ConnectivityStatusListener
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListViewModel
import ua.com.foxminded.newsfeed.ui.articles.news.feed.SingleFeedViewModel
import ua.com.foxminded.newsfeed.ui.articles.news.feeds.AllFeedsViewModel
import ua.com.foxminded.newsfeed.ui.articles.saved.SavedNewsViewModel
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder
import javax.inject.Inject

class NewsViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    private val appComponent = App.instance.component

    var sourceType: Int = 0

    @Inject
    lateinit var repository: NewsRepository

    @Inject
    lateinit var dispatchers: DispatchersHolder

    @Inject
    lateinit var statusListener: ConnectivityStatusListener

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        appComponent.inject(this)
        if (modelClass.isAssignableFrom(NewsListViewModel::class.java)) {
            return AllFeedsViewModel(repository, dispatchers, statusListener) as T
        } else if (modelClass.isAssignableFrom(SavedNewsViewModel::class.java)) {
            return SavedNewsViewModel(repository, dispatchers) as T
        } else if (modelClass.isAssignableFrom(SingleFeedViewModel::class.java)) {
            return SingleFeedViewModel(sourceType, repository, dispatchers, statusListener) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
