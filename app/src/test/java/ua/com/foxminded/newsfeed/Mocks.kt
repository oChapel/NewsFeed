package ua.com.foxminded.newsfeed

import org.mockito.Mockito
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.models.dto.Enclosure
import ua.com.foxminded.newsfeed.models.dto.Feed
import ua.com.foxminded.newsfeed.models.dto.NewsSchema

object Mocks {

    fun <T> any(type: Class<T>): T = Mockito.any(type)

    fun getNewsSchema(): NewsSchema {
        val feed = Feed(
            "author",
            "description",
            "image",
            "link",
            "title",
            "url"
        )
        return NewsSchema(feed, ArrayList(), "status")
    }

    fun getArticle(): Article {
        return Article(
            "author",
            listOf(),
            "content",
            "description",
            Enclosure("link"),
            "guid",
            "link",
            "pubDate",
            "thumbnail",
            "title"
        )
    }
}