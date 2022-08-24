package ua.com.foxminded.newsfeed.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ua.com.foxminded.newsfeed.data.dto.NewsSchema

interface NewsFeedApi {

    @GET("api.json")
    suspend fun getNews(
        @Query("rss_url") url: String,
        @Query("api_key") apiKey: String,
        @Query("order_by") orderBy: String,
        @Query("count") count: Int
    ): NewsSchema

    companion object {
        const val NYT_URL = "http://www.nytimes.com/services/xml/rss/nyt/Business.xml"
        const val CNN_URL = "http://rss.cnn.com/rss/edition.rss"
        const val WIRED_URL = "http://www.wired.com/feed/rss"

        const val PARAM_PUB_DATE = "pubDate"
    }
}
