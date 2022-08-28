package ua.com.foxminded.newsfeed.ui.articles.news.feeds

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.data.NewsRepository
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.model.network.ConnectivityStatusListener
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListViewModel
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

class AllFeedsViewModel(
    repository: NewsRepository,
    dispatchersHolder: DispatchersHolder,
    statusListener: ConnectivityStatusListener
) : NewsListViewModel(repository, dispatchersHolder, statusListener) {

    override fun launchJob() {
        launch = viewModelScope.launch {
            newsFlow
                .onEach { setState(NewsListScreenState.Loading()) }
                .flowOn(dispatchers.getMain())
                .map { p ->
                    val page = if (p == PAGE_ZERO) 0 else p
                    if (!offlineMode) {
                        val list = ArrayList<Article>()
                        for (response in repository.loadAllNews(page)) {
                            list.addAll(response.items)
                        }
                        repository.saveNews(list)
                        return@map list.sortedByDescending { it.pubDate }
                    } else {
                        return@map repository.getAllCachedNews(page)
                    }
                }
                .combine(repository.getSavedNews()) { loadedNews, savedNews ->
                    return@combine loadedNews.map { a ->
                        a.copy().apply { isBookmarked = savedNews.any { it.guid == a.guid } }
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
}
