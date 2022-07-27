package ua.com.foxminded.newsfeed.data.network

import ua.com.foxminded.newsfeed.data.dto.NewsResponse
import ua.com.foxminded.newsfeed.util.Result

interface NewsNetwork {

    suspend fun getAllNews(): Result<List<NewsResponse>>

    suspend fun getNytNews(): Result<NewsResponse>

    suspend fun getCnnNews(): Result<NewsResponse>

    suspend fun getWiredNews(): Result<NewsResponse>
}