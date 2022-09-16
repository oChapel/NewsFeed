package ua.com.foxminded.newsfeed.ui.articles.news.state

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import ua.com.foxminded.newsfeed.models.dto.NewsItem
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListContract

@RunWith(MockitoJUnitRunner::class)
class NewsListScreenStateTest {

    private lateinit var view: NewsListContract.View

    @Before
    fun setUp() {
        view = mock(NewsListContract.View::class.java)
    }

    @Test
    fun test_LoadingState() {
        val list = ArrayList<NewsItem>()
        val prevState = NewsListScreenState.LoadNews(list)
        val state = NewsListScreenState.Loading()

        state.merge(prevState)
        state.visit(view)

        verify(view, times(1)).setProgress(true)
        verify(view, times(1)).showNews(list)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun test_LoadNewsState() {
        val list = ArrayList<NewsItem>()
        val state = NewsListScreenState.LoadNews(list)

        state.visit(view)

        verify(view, times(1)).setProgress(false)
        verify(view, times(1)).showNews(list)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun test_ErrorState() {
        val list = ArrayList<NewsItem>()
        val prevState = NewsListScreenState.LoadNews(list)
        val state = NewsListScreenState.Error()

        state.merge(prevState)
        state.visit(view)

        verify(view, times(1)).setProgress(false)
        verify(view, times(1)).showNews(list)
        verifyNoMoreInteractions(view)
    }
}
