package ua.com.foxminded.newsfeed.ui.articles.news

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.data.Article
import ua.com.foxminded.newsfeed.data.NewsRepository
import ua.com.foxminded.newsfeed.mvi.MviViewModel
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

class NewsListViewModel(
    private val repository: NewsRepository,
    private val dispatchers: DispatchersHolder
) : MviViewModel<
        NewsListContract.View,
        NewsListScreenState,
        NewsListScreenEffect>(),
    NewsListContract.ViewModel {

    private val newsFlow = MutableStateFlow(0)
    private val articleFlow = MutableSharedFlow<Article>(extraBufferCapacity = 1)
    private var launch: Job? = null

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE && launch == null) {
            launch = viewModelScope.launch {
                newsFlow
                    .onEach { setState(NewsListScreenState.Loading()) }
                    .flowOn(dispatchers.getMain())
                    .map {
                        val list = ArrayList<Article>()
                        for (response in repository.loadAllNews()) {
                            list.addAll(response.items)
                        }
                        return@map list.sortedByDescending { it.pubDate }
                    }
                    .combine(repository.getAllArticlesFromDb()) { loadedNews, savedNews ->
                        for (article in loadedNews) {
                            article.isSaved = savedNews.any { it.guid == article.guid }
                        }
                        return@combine loadedNews
                    }
                    .flowOn(dispatchers.getIO())
                    .catch { error ->
                        setState(NewsListScreenState.Error())
                        setEffect(NewsListScreenEffect.ShowError(error))
                    }
                    .collect { list -> setState(NewsListScreenState.LoadNews(list)) }
            }

            viewModelScope.launch {
                articleFlow
                    .map { article -> Pair(article, repository.existsInDb(article.guid)) }
                    .onEach { pair ->
                        when (pair.second) {
                            true -> repository.deleteArticleByGuid(pair.first.guid)
                            false -> repository.saveArticle(pair.first)
                        }
                    }
                    .flowOn(dispatchers.getIO())
                    .catch { error -> setEffect(NewsListScreenEffect.ShowError(error)) }
                    .collect()
            }
        }
    }

    override fun loadNews() {
        if (newsFlow.value == 0) {
            newsFlow.tryEmit(1)
        } else {
            newsFlow.tryEmit(0)
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
