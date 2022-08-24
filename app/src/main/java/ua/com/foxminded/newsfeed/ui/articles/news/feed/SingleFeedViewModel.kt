package ua.com.foxminded.newsfeed.ui.articles.news.feed

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.data.NewsRepository
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListViewModel
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

class SingleFeedViewModel(
    private val sourceType: Int,
    repository: NewsRepository,
    dispatchers: DispatchersHolder
) : NewsListViewModel(repository, dispatchers) {

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE && launch == null) {
            launch = viewModelScope.launch {
                newsFlow
                    .onEach { setState(NewsListScreenState.Loading()) }
                    .flowOn(dispatchers.getMain())
                    .map { p ->
                        val page = if (p == -1) 0 else p
                        val list = ArrayList<Article>()
                        when (sourceType) {
                            SourceTypes.NYT_FEED -> list.addAll(repository.getNytNews(page).items)
                            SourceTypes.CNN_FEED -> list.addAll(repository.getCnnNews(page).items)
                            SourceTypes.WIRED_FEED -> list.addAll(repository.getWiredNews(page).items)
                        }
                        return@map list
                    }
                    .combine(repository.getAllArticlesFromDb()) { loadedNews, savedNews ->
                        return@combine loadedNews.map { a ->
                            a.copy().apply { isSaved = savedNews.any { it.guid == a.guid } }
                        }
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
}