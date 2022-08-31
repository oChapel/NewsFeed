package ua.com.foxminded.newsfeed.models.network

import ua.com.foxminded.newsfeed.models.dto.NewsSchema

interface NewsNetwork {

    suspend fun getAllNews(page: Int, pageSize: Int): List<NewsSchema>

    suspend fun getNytNews(page: Int, pageSize: Int): NewsSchema

    suspend fun getCnnNews(page: Int, pageSize: Int): NewsSchema

    suspend fun getWiredNews(page: Int, pageSize: Int): NewsSchema
}