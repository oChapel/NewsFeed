package ua.com.foxminded.newsfeed.data

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.data.dto.NewsSchema

interface NewsRepository {

    suspend fun loadAllNews(page: Int): List<NewsSchema>

    suspend fun getNytNews(page: Int): NewsSchema

    suspend fun getCnnNews(page: Int): NewsSchema

    suspend fun getWiredNews(page: Int): NewsSchema

    suspend fun saveArticle(article: Article)

    fun getAllArticlesFromDb(): Flow<List<Article>>

    suspend fun existsInDb(guid: String): Boolean

    suspend fun deleteArticleByGuid(guid: String)

    suspend fun deleteArticle(article: Article)

    companion object {
        const val SINGLE_SOURCE_PAGE_SIZE = 10
        const val MULTIPLE_SOURCE_PAGE_SIZE = 3
    }
}
