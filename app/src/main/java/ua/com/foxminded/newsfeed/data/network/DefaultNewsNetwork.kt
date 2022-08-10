package ua.com.foxminded.newsfeed.data.network

import ua.com.foxminded.newsfeed.data.dto.NewsResponse

class DefaultNewsNetwork(private val newsFeedApi: NewsFeedApi) : NewsNetwork {

    //TODO handle error?

    override suspend fun getAllNews(): List<NewsResponse> {
        return ArrayList<NewsResponse>().apply {
            add(getNytNews())
            add(getCnnNews())
            add(getWiredNews())
        }
    }

    override suspend fun getNytNews(): NewsResponse {
        return newsFeedApi.getNytNews()
    }

    override suspend fun getCnnNews(): NewsResponse {
        return newsFeedApi.getCnnNews()
    }

    override suspend fun getWiredNews(): NewsResponse {
        return newsFeedApi.getWiredNews()
    }
}
