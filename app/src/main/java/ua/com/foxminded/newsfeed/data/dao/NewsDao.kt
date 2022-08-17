package ua.com.foxminded.newsfeed.data.dao

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dto.Article

interface NewsDao {

    suspend fun insertArticle(article: Article)

    fun getAllArticlesFlow(): Flow<List<Article>>

    suspend fun existsInDb(guid: String): Boolean

    suspend fun deleteArticleByGuid(guid: String)

    suspend fun deleteArticle(article: Article)
}
