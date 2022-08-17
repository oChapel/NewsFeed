package ua.com.foxminded.newsfeed.data.network

import ua.com.foxminded.newsfeed.data.dto.NewsSchema

interface NewsNetwork {

    suspend fun getAllNews(): List<NewsSchema>

    suspend fun getNytNews(): NewsSchema

    suspend fun getCnnNews(): NewsSchema

    suspend fun getWiredNews(): NewsSchema
}