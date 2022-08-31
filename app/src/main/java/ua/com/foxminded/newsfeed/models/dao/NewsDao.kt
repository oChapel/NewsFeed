package ua.com.foxminded.newsfeed.models.dao

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.models.dto.Article

interface NewsDao {

    suspend fun insertArticle(article: Article)

    suspend fun insertNews(list: List<Article>)

    suspend fun getAllCachedNews(page: Int, pageSize: Int): List<Article>

    suspend fun getCachedNewsBySource(page: Int, pageSize: Int, domain: String): List<Article>

    fun getSavedNewsFlow(): Flow<List<Article>>

    suspend fun isBookmarked(guid: String): Boolean

    suspend fun deleteArticle(article: Article)
}
