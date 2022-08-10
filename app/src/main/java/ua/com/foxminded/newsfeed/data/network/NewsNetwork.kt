package ua.com.foxminded.newsfeed.data.network

import ua.com.foxminded.newsfeed.data.dto.NewsResponse

interface NewsNetwork {

    suspend fun getAllNews(): List<NewsResponse>

    suspend fun getNytNews(): NewsResponse

    suspend fun getCnnNews(): NewsResponse

    suspend fun getWiredNews(): NewsResponse
}