package ua.com.foxminded.newsfeed.ui.articles.saved

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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
import ua.com.foxminded.newsfeed.models.dto.EmptyViewItem
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenState
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SavedNewsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var model: SavedNewsViewModel
    private lateinit var repository: NewsRepository
    private lateinit var dispatchers: DispatchersHolder

    private lateinit var stateObserver: Observer<SavedNewsScreenState>
    private lateinit var effectObserver: Observer<SavedNewsScreenEffect>

    private lateinit var stateCaptor: ArgumentCaptor<SavedNewsScreenState>
    private lateinit var actionCaptor: ArgumentCaptor<SavedNewsScreenEffect>

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = mock(NewsRepository::class.java)
        dispatchers = mock(DispatchersHolder::class.java)
        model = SavedNewsViewModel(repository, dispatchers)

        stateObserver = mock(Observer::class.java) as Observer<SavedNewsScreenState>
        effectObserver = mock(Observer::class.java) as Observer<SavedNewsScreenEffect>

        model.getStateObservable().observeForever(stateObserver)
        model.getEffectObservable().observeForever(effectObserver)

        stateCaptor = ArgumentCaptor.forClass(SavedNewsScreenState::class.java)
        actionCaptor = ArgumentCaptor.forClass(SavedNewsScreenEffect::class.java)

        `when`(dispatchers.getIO()).thenReturn(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        model.getStateObservable().removeObserver(stateObserver)
        model.getEffectObservable().removeObserver(effectObserver)
        Dispatchers.resetMain()
    }

    private fun verifyNoMore() {
        verifyNoMoreInteractions(stateObserver, effectObserver)
        verifyNoMoreInteractions(repository)
    }

    private fun verifyEmptyListState() {
        verify(stateObserver, times(1)).onChanged(stateCaptor.capture())
        var setCounter = 0
        for (state in stateCaptor.allValues) {
            if (state is SavedNewsScreenState.ShowNews) {
                setCounter++
                assertTrue(state.list[0] is EmptyViewItem)
            }
        }
        assertEquals(1, setCounter)
    }

    private fun verifyNotEmptyListState(article: Article) {
        verify(stateObserver, times(1)).onChanged(stateCaptor.capture())
        var setCounter = 0
        for (state in stateCaptor.allValues) {
            if (state is SavedNewsScreenState.ShowNews) {
                setCounter++
                assertTrue(state.list[0] == article)
            }
        }
        assertEquals(1, setCounter)
    }

    @Test
    fun test_LoadNews_EmptyList() {
        `when`(repository.getSavedNews()).thenReturn(flow { emit(listOf()) })

        model.onStateChanged(Lifecycle.Event.ON_CREATE)

        verifyEmptyListState()
        verify(repository, times(1)).getSavedNews()
        verifyNoMore()
    }

    @Test
    fun test_LoadNews_NotEmptyList() {
        val article = Mocks.getArticle()
        `when`(repository.getSavedNews()).thenReturn(flow { emit(listOf(article)) })

        model.onStateChanged(Lifecycle.Event.ON_CREATE)

        verifyNotEmptyListState(article)
        verify(repository, times(1)).getSavedNews()
        verifyNoMore()
    }

    @Test
    fun test_OnArticleStateChanged_SaveArticle() = runTest {
        val article = Mocks.getArticle()
        `when`(repository.getSavedNews()).thenReturn(flow { emit(listOf()) })
        `when`(repository.isBookmarked(anyString())).thenReturn(false)
        `when`(repository.saveArticle(Mocks.any(Article::class.java))).thenReturn(Unit)

        model.onStateChanged(Lifecycle.Event.ON_CREATE)
        model.onArticleStateChanged(article)

        verifyEmptyListState()
        verify(repository, times(1)).getSavedNews()
        verify(repository, times(1)).isBookmarked(article.guid)
        verify(repository, times(1)).saveArticle(article)
        verifyNoMore()
    }

    @Test
    fun test_OnArticleStateChanged_DeleteArticle() = runTest {
        val article = Mocks.getArticle().copy(isBookmarked = true)
        `when`(repository.getSavedNews()).thenReturn(flow { emit(listOf(article)) })
        `when`(repository.isBookmarked(anyString())).thenReturn(true)
        `when`(repository.deleteArticle(Mocks.any(Article::class.java))).thenReturn(Unit)

        model.onStateChanged(Lifecycle.Event.ON_CREATE)
        model.onArticleStateChanged(article)

        verify(effectObserver, times(1)).onChanged(actionCaptor.capture())
        var undoCounter = 0
        for (action in actionCaptor.allValues) {
            if (action is SavedNewsScreenEffect.ShowUndoSnackBar) undoCounter++
        }
        assertEquals(1, undoCounter)

        verifyNotEmptyListState(article)
        verify(repository, times(1)).getSavedNews()
        verify(repository, times(1)).isBookmarked(article.guid)
        verify(repository, times(1)).deleteArticle(article)
        verifyNoMore()
    }

    @Test
    fun test_OnArticleStateChanged_ThrowError() = runTest {
        val article = Mocks.getArticle()
        `when`(repository.getSavedNews()).thenReturn(flow { emit(listOf()) })
        `when`(repository.isBookmarked(anyString())).thenThrow(RuntimeException("some error"))

        model.onStateChanged(Lifecycle.Event.ON_CREATE)
        model.onArticleStateChanged(article)

        verify(effectObserver, times(1)).onChanged(actionCaptor.capture())
        var errorCounter = 0
        for (action in actionCaptor.allValues) {
            if (action is SavedNewsScreenEffect.ShowErrorToast) errorCounter++
        }
        assertEquals(1, errorCounter)

        verifyEmptyListState()
        verify(repository, times(1)).getSavedNews()
        verify(repository, times(1)).isBookmarked(article.guid)
        verifyNoMore()
    }
}
