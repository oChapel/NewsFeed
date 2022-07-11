package ua.com.foxminded.newsfeed.data

import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.data.model.NewsResponse
import ua.com.foxminded.newsfeed.util.Result

interface NewsRepository {

    suspend fun getNytNews(): Result<NewsResponse>

    suspend fun getCnnNews(): Result<NewsResponse>

    suspend fun getFinancialTimesNews(): Result<NewsResponse>

    suspend fun saveArticle(article: Item)

    suspend fun getAllArticlesFromDb(): List<Item>

    suspend fun deleteArticle(article: Item)
}