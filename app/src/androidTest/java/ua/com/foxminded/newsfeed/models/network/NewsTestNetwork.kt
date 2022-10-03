package ua.com.foxminded.newsfeed.models.network

import kotlinx.coroutines.delay
import ua.com.foxminded.newsfeed.models.Mocks
import ua.com.foxminded.newsfeed.models.dto.Feed
import ua.com.foxminded.newsfeed.models.dto.NewsSchema

class NewsTestNetwork : NewsNetwork {

    private val feed = Feed(
        "author",
        "description",
        "image",
        "link",
        "title",
        "url"
    )

    override suspend fun getAllNews(page: Int, pageSize: Int): List<NewsSchema> {
        return listOf(
            getNytNews(page, pageSize),
            getCnnNews(page, pageSize),
            getWiredNews(page, pageSize)
        )
    }

    override suspend fun getNytNews(page: Int, pageSize: Int): NewsSchema {
        delay(1000L)
        val list = listOf(
            Mocks.getArticle(Mocks.NYT_TYPE, Mocks.NYT_LINK, 1, false),
            Mocks.getArticle(Mocks.NYT_TYPE, Mocks.NYT_LINK, 2, false),
            Mocks.getArticle(Mocks.NYT_TYPE, Mocks.NYT_LINK, 3, false)
        )
        return NewsSchema(feed, list, "OK")
    }

    override suspend fun getCnnNews(page: Int, pageSize: Int): NewsSchema {
        delay(1000L)
        val list = listOf(
            Mocks.getArticle(Mocks.CNN_TYPE, Mocks.CNN_LINK, 4, false),
            Mocks.getArticle(Mocks.CNN_TYPE, Mocks.CNN_LINK, 5, false),
            Mocks.getArticle(Mocks.CNN_TYPE, Mocks.CNN_LINK, 6, false)
        )
        return NewsSchema(feed, list, "OK")
    }

    override suspend fun getWiredNews(page: Int, pageSize: Int): NewsSchema {
        delay(1000L)
        val list = listOf(
            Mocks.getArticle(Mocks.WIRED_TYPE, Mocks.WIRED_LINK, 7, false),
            Mocks.getArticle(Mocks.WIRED_TYPE, Mocks.WIRED_LINK, 8, false),
            Mocks.getArticle(Mocks.WIRED_TYPE, Mocks.WIRED_LINK, 9, false)
        )
        return NewsSchema(feed, list, "OK")
    }
}
