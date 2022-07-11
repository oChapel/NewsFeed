package ua.com.foxminded.newsfeed.ui.news_list

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.data.NewsRepository
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
    private var launch: Job? = null

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE && launch == null) {
            launch = viewModelScope.launch {
                newsFlow
                    .onEach { setState(NewsListScreenState.Loading()) }
                    .flowOn(Dispatchers.Main)
                    .map { repository.getNytNews() }
                    .flowOn(Dispatchers.IO)
                    .catch { error ->
                        error.printStackTrace()
                        setEffect(NewsListScreenEffect.ShowToast(R.string.failed_to_load_news))
                    }
                    .collect{ result ->
                        if (result.isSuccessful) {
                            setState(NewsListScreenState.LoadNews((result as Result.Success).data.items))
                        } else {
                            setEffect(NewsListScreenEffect.ShowToast(R.string.failed_to_load_news))
                        }
                    }
            }
        }
    }

    override fun reload() {
        newsFlow.tryEmit(0)
    }

    override fun onCleared() {
        launch = null
        super.onCleared()
    }
}