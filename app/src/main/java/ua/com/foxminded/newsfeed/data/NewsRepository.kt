package ua.com.foxminded.newsfeed.data

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.data.dto.NewsResponse

interface NewsRepository {

    suspend fun loadAllNews(): List<NewsResponse>

    suspend fun getNytNews(): NewsResponse

    suspend fun getCnnNews(): NewsResponse

    suspend fun getWiredNews(): NewsResponse

    suspend fun saveArticle(article: Article)

    fun getAllArticlesFromDb(): Flow<List<Article>>

    suspend fun existsInDb(guid: String): Boolean

    suspend fun deleteArticleByGuid(guid: String)

    suspend fun deleteArticle(article: Article)
}