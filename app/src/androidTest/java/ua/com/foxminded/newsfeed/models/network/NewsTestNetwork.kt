package ua.com.foxminded.newsfeed.models.network

import kotlinx.coroutines.delay
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.dto.Enclosure
import ua.com.foxminded.newsfeed.models.dto.Feed
import ua.com.foxminded.newsfeed.models.dto.NewsSchema
import java.text.SimpleDateFormat
import java.util.*

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
            getArticle(NYT_TYPE, NYT_LINK, 1),
            getArticle(NYT_TYPE, NYT_LINK, 2),
            getArticle(NYT_TYPE, NYT_LINK, 3)
        )
        return NewsSchema(feed, list, "OK")
    }

    override suspend fun getCnnNews(page: Int, pageSize: Int): NewsSchema {
        delay(1000L)
        val list = listOf(
            getArticle(CNN_TYPE, CNN_LINK, 4),
            getArticle(CNN_TYPE, CNN_LINK, 5),
            getArticle(CNN_TYPE, CNN_LINK, 6)
        )
        return NewsSchema(feed, list, "OK")
    }

    override suspend fun getWiredNews(page: Int, pageSize: Int): NewsSchema {
        delay(1000L)
        val list = listOf(
            getArticle(WIRED_TYPE, WIRED_LINK, 7),
            getArticle(WIRED_TYPE, WIRED_LINK, 8),
            getArticle(WIRED_TYPE, WIRED_LINK, 9)
        )
        return NewsSchema(feed, list, "OK")
    }

    private fun getArticle(
        articleType: String,
        link: String,
        publishedHoursAgo: Int
    ): Article = Article(
        "author",
        listOf(),
        "content",
        "$articleType Description",
        Enclosure(),
        "$articleType guid",
        link,
        getPubDate(publishedHoursAgo),
        "thumbnail",
        "$articleType Title"
    )

    private fun getPubDate(hoursAgo: Int): String {
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }
        val calendar = Calendar.getInstance()
        val millisAgo = hoursAgo * MILLIS_IN_HOUR
        calendar.timeInMillis = System.currentTimeMillis() - millisAgo
        return sd.format(calendar.time)
    }

    companion object {
        private const val NYT_TYPE = "NYT"
        private const val CNN_TYPE = "CNN"
        private const val WIRED_TYPE = "WIRED"

        private const val NYT_LINK = "https://www.nytimes.com/"
        private const val CNN_LINK = "https://edition.cnn.com/"
        private const val WIRED_LINK = "https://www.wired.com/"

        private const val MINUTES_IN_HOUR = 60
        private const val SECONDS_IN_MINUTE = 60
        private const val MILLIS_IN_SECOND = 1000
        private const val MILLIS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLIS_IN_SECOND
    }
}