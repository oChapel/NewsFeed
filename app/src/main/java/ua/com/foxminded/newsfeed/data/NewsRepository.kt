package ua.com.foxminded.newsfeed.data

import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.data.model.NewsResponse
import ua.com.foxminded.newsfeed.util.Result

interface NewsRepository {

    suspend fun loadAllNews(): Result<List<NewsResponse>>

    suspend fun getNytNews(): Result<NewsResponse>

    suspend fun getCnnNews(): Result<NewsResponse>

    suspend fun getWiredNews(): Result<NewsResponse>

    suspend fun saveArticle(article: Item)

    suspend fun getAllArticlesFromDb(): List<Item>

    suspend fun deleteArticle(article: Item)
}