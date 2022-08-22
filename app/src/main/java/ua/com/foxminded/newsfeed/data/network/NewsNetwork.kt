package ua.com.foxminded.newsfeed.data.network

import ua.com.foxminded.newsfeed.data.dto.NewsSchema

interface NewsNetwork {

    suspend fun getAllNews(page: Int, pageSize: Int): List<NewsSchema>

    suspend fun getNytNews(page: Int, pageSize: Int): NewsSchema

    suspend fun getCnnNews(page: Int, pageSize: Int): NewsSchema

    suspend fun getWiredNews(page: Int, pageSize: Int): NewsSchema
}