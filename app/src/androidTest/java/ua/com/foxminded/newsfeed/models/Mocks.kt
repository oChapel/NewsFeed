package ua.com.foxminded.newsfeed.models

import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.dto.Enclosure
import java.text.SimpleDateFormat
import java.util.*

object Mocks {

    private const val MINUTES_IN_HOUR = 60
    private const val SECONDS_IN_MINUTE = 60
    private const val MILLIS_IN_SECOND = 1000
    private const val MILLIS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLIS_IN_SECOND

    const val NYT_TYPE = "NYT"
    const val CNN_TYPE = "CNN"
    const val WIRED_TYPE = "WIRED"

    const val NYT_LINK = "https://www.nytimes.com/"
    const val CNN_LINK = "https://edition.cnn.com/"
    const val WIRED_LINK = "https://www.wired.com/"

    fun getArticle(
        articleType: String,
        link: String,
        publishedHoursAgo: Int,
        isBookmarked: Boolean
    ): Article = Article(
        "author",
        listOf(),
        "content",
        "$articleType Description",
        Enclosure(),
        "$articleType guid_$publishedHoursAgo",
        link,
        getPubDate(publishedHoursAgo),
        "thumbnail",
        "$articleType Title",
        isBookmarked
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
}