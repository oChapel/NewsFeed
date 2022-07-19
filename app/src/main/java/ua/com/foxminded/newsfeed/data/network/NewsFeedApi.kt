package ua.com.foxminded.newsfeed.data.network

import retrofit2.Response
import retrofit2.http.GET
import ua.com.foxminded.newsfeed.data.model.NewsResponse

interface NewsFeedApi {

    @GET("/v1/api.json?rss_url=https%3A%2F%2Frss.nytimes.com%2Fservices%2Fxml%2Frss%2Fnyt%2FBusiness.xml")
    suspend fun getNytNews(): Response<NewsResponse>

    @GET("/v1/api.json?rss_url=http%3A%2F%2Ffeeds.wired.com%2Fwired%2Findex")
    suspend fun getWiredNews(): Response<NewsResponse>

    @GET("/v1/api.json?rss_url=http%3A%2F%2Frss.cnn.com%2Frss%2Fcnn_topstories.rss")
    suspend fun getCnnNews(): Response<NewsResponse>
}