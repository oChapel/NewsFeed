package ua.com.foxminded.newsfeed.ui.articles.news.feeds

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import ua.com.foxminded.newsfeed.Mocks
import ua.com.foxminded.newsfeed.models.NewsRepository
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.dto.NewsSchema
import ua.com.foxminded.newsfeed.models.network.listener.ConnectivityStatusListener
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListViewModelTest
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AllFeedsViewModelTest : NewsListViewModelTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var model: AllFeedsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = mock(NewsRepository::class.java)
        dispatchers = mock(DispatchersHolder::class.java)
        connectivityListener = mock(ConnectivityStatusListener::class.java)
        model = AllFeedsViewModel(
            repository,
            dispatchers,
            connectivityListener
        )

        stateObserver = mock(Observer::class.java) as Observer<NewsListScreenState>
        effectObserver = mock(Observer::class.java) as Observer<NewsListScreenEffect>

        model.getStateObservable().observeForever(stateObserver)
        model.getEffectObservable().observeForever(effectObserver)

        stateCaptor = ArgumentCaptor.forClass(NewsListScreenState::class.java)
        actionCaptor = ArgumentCaptor.forClass(NewsListScreenEffect::class.java)

        `when`(dispatchers.getIO()).thenReturn(Dispatchers.Unconfined)
        `when`(dispatchers.getMain()).thenReturn(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        model.getStateObservable().removeObserver(stateObserver)
        model.getEffectObservable().removeObserver(effectObserver)
        Dispatchers.resetMain()
    }

    private suspend fun setUpOnlineInteractions(list: List<NewsSchema>) {
        `when`(connectivityListener.getStatusFlow()).thenReturn(
            flow { emit(ConnectivityStatusListener.STATUS_AVAILABLE) }
        )
        doNothing().`when`(connectivityListener).checkIfNetworkAvailable()
        `when`(repository.loadAllNews(anyInt())).thenReturn(list)
        `when`(repository.getSavedNews()).thenReturn(flow { emit(ArrayList()) })
    }

    @Test
    fun test_LoadedFromRemote() = runTest {
        val list = listOf(Mocks.getNewsSchema())
        setUpOnlineInteractions(list)

        model.onStateChanged(Lifecycle.Event.ON_CREATE)

        verifyNewsLoaded(1)
        verify(repository, times(1)).loadAllNews(0)
        verify(repository, times(1)).saveNews(list[0].items)
        verify(repository, times(1)).getSavedNews()
        verifyConnectivityListener()
        verifyNoMore()
    }

    @Test
    fun test_LoadedFromLocal() = runTest {
        `when`(connectivityListener.getStatusFlow()).thenReturn(
            flow { emit(ConnectivityStatusListener.STATUS_UNAVAILABLE) }
        )
        doNothing().`when`(connectivityListener).checkIfNetworkAvailable()
        `when`(repository.getAllCachedNews(anyInt())).thenReturn(listOf(mock(Article::class.java)))
        `when`(repository.getSavedNews()).thenReturn(flow { emit(ArrayList()) })

        model.onStateChanged(Lifecycle.Event.ON_CREATE)

        verifyNewsLoaded(1)
        verifyOfflineAction()
        verify(repository, times(1)).getAllCachedNews(0)
        verify(repository, times(1)).getSavedNews()
        verifyConnectivityListener()
        verifyNoMore()
    }

    @Test
    fun test_ThrowError() = runTest {
        `when`(connectivityListener.getStatusFlow()).thenReturn(
            flow { emit(ConnectivityStatusListener.STATUS_AVAILABLE) }
        )
        doNothing().`when`(connectivityListener).checkIfNetworkAvailable()
        `when`(repository.loadAllNews(anyInt())).thenThrow(RuntimeException("some error"))
        `when`(repository.getSavedNews()).thenReturn(flow { emit(ArrayList()) })

        model.onStateChanged(Lifecycle.Event.ON_CREATE)

        verifyError()
        verify(repository, times(1)).loadAllNews(0)
        verify(repository, times(1)).getSavedNews()
        verifyConnectivityListener()
        verifyNoMore()
    }

    @Test
    fun test_Reload() = runTest {
        val list = listOf(Mocks.getNewsSchema())
        setUpOnlineInteractions(list)

        model.onStateChanged(Lifecycle.Event.ON_CREATE)
        model.reload()

        verifyNewsLoaded(2)
        verify(repository, times(2)).loadAllNews(0)
        verify(repository, times(2)).saveNews(list[0].items)
        verify(repository, times(1)).getSavedNews()
        verifyConnectivityListener()
        verifyNoMore()
    }

    @Test
    fun test_LoadNews() = runTest {
        val list = listOf(Mocks.getNewsSchema())
        setUpOnlineInteractions(list)

        model.onStateChanged(Lifecycle.Event.ON_CREATE)
        model.loadNews(1)
        model.loadNews(2)

        verifyNewsLoaded(3)
        verify(repository, times(1)).loadAllNews(0)
        verify(repository, times(1)).loadAllNews(1)
        verify(repository, times(1)).loadAllNews(2)
        verify(repository, times(3)).saveNews(list[0].items)
        verify(repository, times(1)).getSavedNews()
        verifyConnectivityListener()
        verifyNoMore()
    }

    @Test
    fun test_OnBookmarkClicked_ArticleSaved() = runTest {
        val list = listOf(Mocks.getNewsSchema())
        setUpOnlineInteractions(list)
        `when`(repository.isBookmarked(anyString())).thenReturn(false)
        `when`(repository.saveArticle(Mocks.any(Article::class.java))).thenReturn(Unit)
        val article = Mocks.getArticle()

        model.onStateChanged(Lifecycle.Event.ON_CREATE)
        model.onBookmarkClicked(article)

        verifyNewsLoaded(1)
        verify(repository, times(1)).loadAllNews(0)
        verify(repository, times(1)).saveNews(list[0].items)
        verify(repository, times(1)).getSavedNews()
        verify(repository, times(1)).isBookmarked(article.guid)
        verify(repository, times(1)).saveArticle(article.copy(isBookmarked = true))
        verifyConnectivityListener()
        verifyNoMore()
    }

    @Test
    fun test_OnBookmarkClicked_ArticleDeleted() = runTest {
        val list = listOf(Mocks.getNewsSchema())
        setUpOnlineInteractions(list)
        `when`(repository.isBookmarked(anyString())).thenReturn(true)
        `when`(repository.deleteArticle(Mocks.any(Article::class.java))).thenReturn(Unit)
        val article = Mocks.getArticle()

        model.onStateChanged(Lifecycle.Event.ON_CREATE)
        model.onBookmarkClicked(article)

        verifyNewsLoaded(1)
        verify(repository, times(1)).loadAllNews(0)
        verify(repository, times(1)).saveNews(list[0].items)
        verify(repository, times(1)).getSavedNews()
        verify(repository, times(1)).isBookmarked(article.guid)
        verify(repository, times(1)).deleteArticle(article)
        verifyConnectivityListener()
        verifyNoMore()
    }

    @Test
    fun test_OnPopUpClicked() = runTest {
        val list = listOf(Mocks.getNewsSchema())
        setUpOnlineInteractions(list)

        model.onStateChanged(Lifecycle.Event.ON_CREATE)
        model.onPopupClicked()

        verifyNewsLoaded(2)
        verify(repository, times(2)).loadAllNews(0)
        verify(repository, times(2)).saveNews(list[0].items)
        verify(repository, times(1)).getSavedNews()
        verifyConnectivityListener()
        verifyNoMore()
    }
}
