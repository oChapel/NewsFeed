package ua.com.foxminded.newsfeed.ui.articles.news

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.com.foxminded.newsfeed.data.NewsRepository
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.mvi.MviViewModel
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

abstract class NewsListViewModel(
    protected val repository: NewsRepository,
    protected val dispatchers: DispatchersHolder
) : MviViewModel<
        NewsListContract.View,
        NewsListScreenState,
        NewsListScreenEffect>(),
    NewsListContract.ViewModel {

    protected val newsFlow = MutableStateFlow(0)
    protected val articleFlow = MutableSharedFlow<Article>(extraBufferCapacity = 1)
    protected var launch: Job? = null

    abstract fun launchJob()

    override fun loadNews(page: Int) {
        if (launch?.isCompleted == true) {
            launchJob()
        }

        if (page == 0 && newsFlow.value == 0) {
            newsFlow.tryEmit(-1)
        } else {
            newsFlow.tryEmit(page)
        }
    }

    override fun onBookmarkClicked(article: Article) {
        articleFlow.tryEmit(article)
    }

    override fun onCleared() {
        launch = null
        super.onCleared()
    }
}
