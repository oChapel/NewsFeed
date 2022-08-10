package ua.com.foxminded.newsfeed.data.network

import ua.com.foxminded.newsfeed.data.NewsSchema

class DefaultNewsNetwork(private val newsFeedApi: NewsFeedApi) : NewsNetwork {

    override suspend fun getAllNews(): List<NewsSchema> {
        return ArrayList<NewsSchema>().apply {
            add(getNytNews())
            add(getCnnNews())
            add(getWiredNews())
        }
    }

    override suspend fun getNytNews() = newsFeedApi.getNytNews()

    override suspend fun getCnnNews() = newsFeedApi.getCnnNews()

    override suspend fun getWiredNews() = newsFeedApi.getWiredNews()
}
