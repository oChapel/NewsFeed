package ua.com.foxminded.newsfeed.data.network

import ua.com.foxminded.newsfeed.data.model.NewsResponse
import ua.com.foxminded.newsfeed.util.Result

interface NewsNetwork {
    suspend fun getNytNews(): Result<NewsResponse>
    suspend fun getCnnNews(): Result<NewsResponse>
    suspend fun getFinancialTimesNews(): Result<NewsResponse>
}