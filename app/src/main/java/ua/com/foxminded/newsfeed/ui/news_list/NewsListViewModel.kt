package ua.com.foxminded.newsfeed.ui.news_list

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.data.NewsRepository
import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.mvi.MviViewModel
import ua.com.foxminded.newsfeed.ui.news_list.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.news_list.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.Result

class NewsListViewModel(private val repository: NewsRepository) : MviViewModel<
        NewsListContract.View,
        NewsListScreenState,
        NewsListScreenEffect>(),
    NewsListContract.ViewModel {

    private val newsFlow = MutableStateFlow(0)
    private val articleFlow = MutableSharedFlow<Item>(extraBufferCapacity = 1)
    private var launch: Job? = null

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE && launch == null) {
            launch = viewModelScope.launch {
                newsFlow
                    .onEach { setState(NewsListScreenState.Loading()) }
                    .flowOn(Dispatchers.Main)
                    .map { repository.loadAllNews() }
                    .flowOn(Dispatchers.IO)
                    .catch { error ->
                        error.printStackTrace()
                        setEffect(NewsListScreenEffect.ShowToast(R.string.failed_to_load_news))
                    }
                    .collect { result ->
                        if (result.isSuccessful) {
                            val list = ArrayList<Item>()
                            for (response in (result as Result.Success).data) {
                                for (article in response.items) {
                                    if (repository.existsInDb(article.title)) {
                                        article.isSaved = true
                                    }
                                }
                                list.addAll(response.items)
                            }
                            setState(NewsListScreenState.LoadNews(list))
                        } else {
                            (result as Result.Error).error?.printStackTrace()
                            setEffect(NewsListScreenEffect.ShowToast(R.string.failed_to_load_news))
                        }
                    }
            }

            viewModelScope.launch {
                articleFlow
                    .map { article -> Pair(article, repository.existsInDb(article.title)) }
                    .onEach { pair ->
                        when (pair.second) {
                            true -> {
                                repository.deleteArticleByTitle(pair.first.title)
                                pair.first.isSaved = false
                            }
                            false -> {
                                repository.saveArticle(pair.first)
                                pair.first.isSaved = true
                            }
                        }
                    }
                    .flowOn(Dispatchers.IO)
                    .catch { error ->
                        error.printStackTrace()
                        setEffect(
                            NewsListScreenEffect.ShowToast(R.string.failed_to_connect_with_db)
                        )
                    }
                    .collect { pair ->
                        setEffect(
                            NewsListScreenEffect.ItemChanged(pair.first, !pair.second)
                        )
                    }
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

    override fun onBookmarkClicked(article: Item) {
        articleFlow.tryEmit(article)
    }

    override fun onCleared() {
        launch = null
        super.onCleared()
    }
}