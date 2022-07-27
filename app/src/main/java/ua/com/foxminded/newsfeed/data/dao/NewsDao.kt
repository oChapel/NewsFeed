package ua.com.foxminded.newsfeed.data.dao

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.model.Item

interface NewsDao {

    suspend fun insertArticle(article: Item)

    fun getAllArticles(): Flow<List<Item>>

    suspend fun existsInDb(title: String): Boolean

    //TODO
    suspend fun deleteArticleByTitle(title: String)

    suspend fun deleteArticle(article: Item)
}