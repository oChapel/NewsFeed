package ua.com.foxminded.newsfeed.ui.articles.saved

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.models.NewsRepository
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.dto.EmptyViewItem
import ua.com.foxminded.newsfeed.mvi.MviViewModel
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

class SavedNewsViewModel(
    private val repository: NewsRepository,
    private val dispatchers: DispatchersHolder
) : MviViewModel<
        SavedNewsContract.View,
        SavedNewsScreenState,
        SavedNewsScreenEffect>(), SavedNewsContract.ViewModel {

    private var launch: Job? = null
    private val articleFlow = MutableSharedFlow<Article>(extraBufferCapacity = 1)

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE && launch == null) {
            launch = viewModelScope.launch {
                repository.getSavedNews()
                    .map { list ->
                        return@map if (list.isNotEmpty()) {
                            list.sortedByDescending { it.pubDate }
                        } else {
                            ArrayList<EmptyViewItem>().apply { add(EmptyViewItem()) }
                        }
                    }
                    .collect { list -> setState(SavedNewsScreenState.ShowNews(list)) }
            }

            viewModelScope.launch {
                articleFlow
                    .map { article -> Pair(article, repository.isBookmarked(article.guid)) }
                    .onEach { pair ->
                        when (pair.second) {
                            false -> repository.saveArticle(pair.first)
                            true -> repository.deleteArticle(pair.first)
                        }
                    }
                    .flowOn(dispatchers.getIO())
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

    override fun onArticleStateChanged(article: Article) {
        articleFlow.tryEmit(article)
    }

    override fun onCleared() {
        launch = null
        super.onCleared()
    }
}
