package ua.com.foxminded.newsfeed.data.network

import ua.com.foxminded.newsfeed.BuildConfig
import ua.com.foxminded.newsfeed.data.dto.NewsSchema

class DefaultNewsNetwork(
    private val newsFeedApi: NewsFeedApi
) : NewsNetwork {

    override suspend fun getAllNews(page: Int, pageSize: Int): List<NewsSchema> {
        return ArrayList<NewsSchema>().apply {
            add(getNytNews(page, pageSize))
            add(getCnnNews(page, pageSize))
            add(getWiredNews(page, pageSize))
        }
    }

    override suspend fun getNytNews(page: Int, pageSize: Int) = newsFeedApi.getNews(
        NewsFeedApi.NYT_URL,
        BuildConfig.API_KEY,
        (page + 1) * pageSize
    )

    override suspend fun getCnnNews(page: Int, pageSize: Int) = newsFeedApi.getNews(
        NewsFeedApi.CNN_URL,
        BuildConfig.API_KEY,
        (page + 1) * pageSize
    )

    override suspend fun getWiredNews(page: Int, pageSize: Int) = newsFeedApi.getNews(
        NewsFeedApi.WIRED_URL,
        BuildConfig.API_KEY,
        (page + 1) * pageSize
    )
}
