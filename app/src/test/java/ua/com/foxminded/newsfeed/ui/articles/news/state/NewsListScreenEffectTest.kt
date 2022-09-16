package ua.com.foxminded.newsfeed.ui.articles.news.state

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListContract

@RunWith(MockitoJUnitRunner::class)
class NewsListScreenEffectTest {

    private lateinit var view: NewsListContract.View

    @Before
    fun setUp() {
        view = mock(NewsListContract.View::class.java)
    }

    @Test
    fun test_ShowErrorAction() {
        val error = Throwable()
        val action = NewsListScreenEffect.ShowError(error)

        action.handle(view)

        verify(view, times(1)).showErrorDialog(error)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun test_ShowToastAction() {
        val action = NewsListScreenEffect.ShowToast(0)

        action.handle(view)

        verify(view, times(1)).showToast(0)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun test_ShowPopupWindowAction() {
        val action = NewsListScreenEffect.ShowPopupWindow()

        action.handle(view)

        verify(view, times(1)).showPopupWindow()
        verifyNoMoreInteractions(view)
    }
}
