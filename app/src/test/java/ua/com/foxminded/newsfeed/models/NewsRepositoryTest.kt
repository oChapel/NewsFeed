package ua.com.foxminded.newsfeed.models

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import ua.com.foxminded.newsfeed.Mocks
import ua.com.foxminded.newsfeed.models.dao.NewsDao
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.dto.Feed
import ua.com.foxminded.newsfeed.models.dto.NewsSchema
import ua.com.foxminded.newsfeed.models.network.NewsNetwork

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class NewsRepositoryTest {

    private lateinit var localDataSource: NewsDao
    private lateinit var remoteDataSource: NewsNetwork
    private lateinit var repository: NewsRepository

    private fun verifyNoMore() {
        verifyNoMoreInteractions(localDataSource, remoteDataSource)
    }

    @Before
    fun setUp() {
        localDataSource = mock(NewsDao::class.java)
        remoteDataSource = mock(NewsNetwork::class.java)
        repository = DefaultNewsRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun test_LoadAllNews() = runTest {
        val list = ArrayList<NewsSchema>()
        `when`(remoteDataSource.getAllNews(anyInt(), anyInt())).thenReturn(list)

        val news = repository.loadAllNews(0)

        assertEquals(list, news)
        verify(remoteDataSource, times(1)).getAllNews(0, NewsRepository.MULTIPLE_SOURCE_PAGE_SIZE)
        verifyNoMore()
    }

    @Test
    fun test_LoadNytNews() = runTest {
        val newsSchema = Mocks.getNewsSchema()
        `when`(remoteDataSource.getNytNews(anyInt(), anyInt())).thenReturn(newsSchema)

        val news = repository.loadNytNews(0)

        assertEquals(newsSchema, news)
        verify(remoteDataSource, times(1)).getNytNews(0, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
        verifyNoMore()
    }

    @Test
    fun test_LoadCnnNews() = runTest {
        val newsSchema = Mocks.getNewsSchema()
        `when`(remoteDataSource.getCnnNews(anyInt(), anyInt())).thenReturn(newsSchema)

        val news = repository.loadCnnNews(0)

        assertEquals(newsSchema, news)
        verify(remoteDataSource, times(1)).getCnnNews(0, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
        verifyNoMore()
    }

    @Test
    fun test_LoadWiredNews() = runTest {
        val newsSchema = Mocks.getNewsSchema()
        `when`(remoteDataSource.getWiredNews(anyInt(), anyInt())).thenReturn(newsSchema)

        val news = repository.loadWiredNews(0)

        assertEquals(newsSchema, news)
        verify(remoteDataSource, times(1)).getWiredNews(0, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
        verifyNoMore()
    }

    @Test
    fun test_SaveArticle() = runTest {
        `when`(localDataSource.insertArticle(Mocks.any(Article::class.java))).thenReturn(Unit)
        repository.saveArticle(mock(Article::class.java))

        verify(localDataSource, times(1)).insertArticle(Mocks.any(Article::class.java))
        verifyNoMore()
    }

    @Test
    fun test_SaveNews() = runTest {
        `when`(localDataSource.insertNews(anyList())).thenReturn(Unit)
        repository.saveNews(ArrayList())

        verify(localDataSource, times(1)).insertNews(anyList())
        verifyNoMore()
    }

    @Test
    fun test_GetAllCachedNews() = runTest {
        val list = ArrayList<Article>()
        `when`(localDataSource.getAllCachedNews(anyInt(), anyInt())).thenReturn(list)

        val news = repository.getAllCachedNews(0)

        assertEquals(list, news)
        verify(localDataSource, times(1)).getAllCachedNews(
            0,
            NewsRepository.SINGLE_SOURCE_PAGE_SIZE
        )
        verifyNoMore()
    }

    @Test
    fun test_GetCachedNewsBySource() = runTest {
        val list = ArrayList<Article>()
        `when`(localDataSource.getCachedNewsBySource(anyInt(), anyInt(), anyString())).thenReturn(list)

        val news = repository.getCachedNewsBySource(0, Article.NYT_DOMAIN)

        assertEquals(list, news)
        verify(localDataSource, times(1)).getCachedNewsBySource(
            0,
            NewsRepository.SINGLE_SOURCE_PAGE_SIZE,
            Article.NYT_DOMAIN
        )
        verifyNoMore()
    }

    @Test
    fun test_GetSavedNews() = runTest {
        val list = ArrayList<Article>()
        `when`(localDataSource.getSavedNewsFlow()).thenReturn( flow { emit(list) })

        val news = repository.getSavedNews().first()

        assertEquals(list, news)
        verify(localDataSource, times(1)).getSavedNewsFlow()
        verifyNoMore()
    }

    @Test
    fun test_IsBookmarked() = runTest {
        `when`(localDataSource.isBookmarked(anyString())).thenReturn(true)

        assertTrue(repository.isBookmarked("guid"))
        verify(localDataSource, times(1)).isBookmarked("guid")
        verifyNoMore()
    }

    @Test
    fun test_deleteArticle() = runTest {
        `when`(localDataSource.deleteArticle(Mocks.any(Article::class.java))).thenReturn(Unit)
        repository.deleteArticle(mock(Article::class.java))

        verify(localDataSource, times(1)).deleteArticle(Mocks.any(Article::class.java))
        verifyNoMore()
    }
}