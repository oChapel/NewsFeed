package ua.com.foxminded.newsfeed.models

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.dto.NewsSchema

interface NewsRepository {

    suspend fun loadAllNews(page: Int): List<NewsSchema>

    suspend fun loadNytNews(page: Int): NewsSchema

    suspend fun loadCnnNews(page: Int): NewsSchema

    suspend fun loadWiredNews(page: Int): NewsSchema

    suspend fun saveArticle(article: Article)

    suspend fun saveNews(list: List<Article>)

    suspend fun getAllCachedNews(page: Int): List<Article>

    suspend fun getCachedNewsBySource(page: Int, domain: String): List<Article>

    fun getSavedNews(): Flow<List<Article>>

    suspend fun isBookmarked(guid: String): Boolean

    suspend fun deleteArticle(article: Article)

    companion object {
        const val SINGLE_SOURCE_PAGE_SIZE = 10
        const val MULTIPLE_SOURCE_PAGE_SIZE = 3
    }
}
