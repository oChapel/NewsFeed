package ua.com.foxminded.newsfeed.data

import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun loadAllNews(): List<NewsSchema>

    suspend fun getNytNews(): NewsSchema

    suspend fun getCnnNews(): NewsSchema

    suspend fun getWiredNews(): NewsSchema

    suspend fun saveArticle(article: Article)

    fun getAllArticlesFromDb(): Flow<List<Article>>

    suspend fun existsInDb(guid: String): Boolean

    suspend fun deleteArticleByGuid(guid: String)

    suspend fun deleteArticle(article: Article)
}
