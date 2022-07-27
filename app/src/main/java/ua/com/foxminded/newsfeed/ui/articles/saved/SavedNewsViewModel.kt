package ua.com.foxminded.newsfeed.ui.articles.saved

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.data.NewsRepository
import ua.com.foxminded.newsfeed.data.dto.Item
import ua.com.foxminded.newsfeed.mvi.MviViewModel
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenState

class SavedNewsViewModel(private val repository: NewsRepository) : MviViewModel<
        SavedNewsContract.View,
        SavedNewsScreenState,
        SavedNewsScreenEffect>(), SavedNewsContract.ViewModel {

    private var launch: Job? = null
    private val articleFlow = MutableSharedFlow<Item>(extraBufferCapacity = 1)

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE && launch == null) {
            launch = viewModelScope.launch {
                repository.getAllArticlesFromDb()
                    .onEach { list ->
                        for (article in list) {
                            article.isSaved = true
                        }
                    }
                    .collect { list ->
                        if (list.isEmpty()) {
                            setState(SavedNewsScreenState.ShowEmptyScreen)
                        } else {
                            setState(SavedNewsScreenState.ShowNews(list))
                        }
                    }
            }

            viewModelScope.launch {
                articleFlow
                    .map { article -> Pair(article, repository.existsInDb(article.title)) }
                    .onEach { pair ->
                        when (pair.second) {
                            false -> repository.saveArticle(pair.first)
                            true -> repository.deleteArticle(pair.first)
                        }
                    }
                    .flowOn(Dispatchers.IO)
                    .catch { error ->
                        error.printStackTrace()
                        setEffect(
                            SavedNewsScreenEffect.ShowErrorToast(R.string.failed_to_connect_with_db)
                        )
                    }
                    .collect { pair ->
                        if (pair.second) {
                            setEffect(SavedNewsScreenEffect.ShowUndoSnackBar(pair.first))
                        }
                    }
            }
        }
    }

    override fun onArticleStateChanged(article: Item) {
        articleFlow.tryEmit(article)
    }

    override fun onCleared() {
        launch = null
        super.onCleared()
    }
}
