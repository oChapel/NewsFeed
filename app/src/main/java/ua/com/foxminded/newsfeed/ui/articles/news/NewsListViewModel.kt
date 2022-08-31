package ua.com.foxminded.newsfeed.ui.articles.news

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.models.NewsRepository
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.network.listener.ConnectivityStatusListener
import ua.com.foxminded.newsfeed.mvi.MviViewModel
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

abstract class NewsListViewModel(
    protected val repository: NewsRepository,
    protected val dispatchers: DispatchersHolder,
    private val statusListener: ConnectivityStatusListener
) : MviViewModel<
        NewsListContract.View,
        NewsListScreenState,
        NewsListScreenEffect>(), NewsListContract.ViewModel {

    protected val newsFlow = MutableStateFlow(0)
    private val articleFlow = MutableSharedFlow<Article>(extraBufferCapacity = 1)
    protected var launch: Job? = null
    protected var offlineMode = false

    abstract fun launchJob()

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE && launch == null) {
            viewModelScope.launch {
                statusListener.getStatusFlow()
                    .collect { status ->
                        when (status) {
                            ConnectivityStatusListener.STATUS_AVAILABLE -> {
                                if (offlineMode) {
                                    offlineMode = false
                                    setEffect(NewsListScreenEffect.ShowPopupWindow())
                                }
                            }
                            ConnectivityStatusListener.STATUS_UNAVAILABLE -> {
                                offlineMode = true
                                setEffect(NewsListScreenEffect.ShowToast(R.string.offline_mode))
                            }
                        }
                    }
            }

            statusListener.checkIfNetworkAvailable()
            launchJob()

            viewModelScope.launch {
                articleFlow
                    .map { article -> Pair(article, repository.isBookmarked(article.guid)) }
                    .onEach { pair ->
                        when (pair.second) {
                            true -> repository.deleteArticle(pair.first)
                            false -> repository.saveArticle(pair.first.copy(isBookmarked = true))
                        }
                    }
                    .flowOn(dispatchers.getIO())
                    .catch { error -> setEffect(NewsListScreenEffect.ShowError(error)) }
                    .collect()
            }
        }
    }

    override fun reload() {
        checkIfJobCompleted()
        if (newsFlow.value == 0) {
            newsFlow.tryEmit(PAGE_ZERO)
        } else {
            newsFlow.tryEmit(0)
        }
    }

    override fun loadNews(page: Int) {
        checkIfJobCompleted()
        if (page == 0 && newsFlow.value == 0) {
            newsFlow.tryEmit(PAGE_ZERO)
        } else {
            newsFlow.tryEmit(page)
        }
    }

    override fun onBookmarkClicked(article: Article) {
        articleFlow.tryEmit(article)
    }

    override fun onPopupClicked() {
        reload()
    }

    private fun checkIfJobCompleted() {
        if (launch?.isCompleted == true) {
            launchJob()
        }
    }

    override fun onCleared() {
        statusListener.releaseRequest()
        launch = null
        super.onCleared()
    }

    companion object {
        const val PAGE_ZERO = -1
    }
}
