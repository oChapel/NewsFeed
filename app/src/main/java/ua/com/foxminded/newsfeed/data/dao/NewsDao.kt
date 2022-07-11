package ua.com.foxminded.newsfeed.data.dao

import ua.com.foxminded.newsfeed.data.model.Item

interface NewsDao {

    suspend fun insertArticle(article: Item)

    suspend fun getAllArticles(): List<Item>

    suspend fun deleteArticle(article: Item)
}