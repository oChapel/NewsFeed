package ua.com.foxminded.newsfeed.ui.articles.news.feed

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.models.NewsRepository
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.network.listener.ConnectivityStatusListener
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListViewModel
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

class SingleFeedViewModel(
    private val sourceType: Int,
    repository: NewsRepository,
    dispatchers: DispatchersHolder,
    statusListener: ConnectivityStatusListener
) : NewsListViewModel(repository, dispatchers, statusListener) {

    override fun launchJob() {
        launch = viewModelScope.launch {
            newsFlow
                .onEach { setState(NewsListScreenState.Loading()) }
                .flowOn(dispatchers.getMain())
                .map { p ->
                    val page = if (p == PAGE_ZERO) 0 else p
                    if (!offlineMode) {
                        val list = ArrayList<Article>()
                        when (sourceType) {
                            SourceTypes.NYT_FEED -> list.addAll(repository.loadNytNews(page).items)
                            SourceTypes.CNN_FEED -> list.addAll(repository.loadCnnNews(page).items)
                            SourceTypes.WIRED_FEED -> list.addAll(repository.loadWiredNews(page).items)
                        }
                        return@map list
                    } else {
                        return@map getCachedNews(page)
                    }
                }
                .combine(repository.getSavedNews()) { loadedNews, savedNews ->
                    return@combine loadedNews.map { a ->
                        a.copy(isBookmarked = savedNews.any { it.guid == a.guid })
                    }
                }
                .flowOn(dispatchers.getIO())
                .catch { error ->
                    setState(NewsListScreenState.Error())
                    setEffect(NewsListScreenEffect.ShowError(error))
                }
                .collect { list -> setState(NewsListScreenState.LoadNews(list)) }
        }
    }

    private suspend fun getCachedNews(page: Int): List<Article> {
        return when (sourceType) {
            SourceTypes.NYT_FEED -> repository.getCachedNewsBySource(page, Article.NYT_DOMAIN)
            SourceTypes.CNN_FEED -> repository.getCachedNewsBySource(page, Article.CNN_DOMAIN)
            else -> repository.getCachedNewsBySource(page, Article.WIRED_DOMAIN)
        }
    }
}
