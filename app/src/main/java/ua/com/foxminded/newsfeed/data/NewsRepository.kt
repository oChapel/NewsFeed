package ua.com.foxminded.newsfeed.data

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dto.Item
import ua.com.foxminded.newsfeed.data.dto.NewsResponse
import ua.com.foxminded.newsfeed.util.Result

interface NewsRepository {

    suspend fun loadAllNews(): Result<List<NewsResponse>>

    suspend fun getNytNews(): Result<NewsResponse>

    suspend fun getCnnNews(): Result<NewsResponse>

    suspend fun getWiredNews(): Result<NewsResponse>

    suspend fun saveArticle(article: Item)

    fun getAllArticlesFromDb(): Flow<List<Item>>

    suspend fun existsInDb(title: String): Boolean

    suspend fun deleteArticleByTitle(title: String)

    suspend fun deleteArticle(article: Item)
}