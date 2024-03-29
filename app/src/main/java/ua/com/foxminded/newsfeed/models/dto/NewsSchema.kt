package ua.com.foxminded.newsfeed.models.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


data class NewsSchema(
    val feed: Feed,
    val items: List<Article>,
    val status: String
)


data class Feed(
    val author: String,
    val description: String,
    val image: String,
    val link: String,
    val title: String,
    val url: String
)


data class Enclosure(
    val link: String = "",
    val type: String? = null,
    val thumbnail: String? = null
)


@Entity(tableName = "articles")
data class Article(
    val author: String,
    val categories: List<String>,
    val content: String,
    val description: String,
    val enclosure: Enclosure,
    @PrimaryKey
    val guid: String,
    val link: String,
    val pubDate: String?,
    val thumbnail: String,
    val title: String,
    val isBookmarked: Boolean = false
) : Serializable, NewsItem {


    companion object {
        const val NYT_DOMAIN = "nytimes.com"
        const val CNN_DOMAIN = "cnn.com"
        const val WIRED_DOMAIN = "wired.com"
    }

    fun hasImageLink(): Boolean = enclosure.link.isNotEmpty()

    override fun getViewHolderType(): Int {
        return when {
            link.contains(NYT_DOMAIN, ignoreCase = true) -> NewsItem.NYT_ARTICLE
            link.contains(CNN_DOMAIN, ignoreCase = true) -> NewsItem.CNN_ARTICLE
            link.contains(WIRED_DOMAIN, ignoreCase = true) -> NewsItem.WIRED_ARTICLE
            else -> NewsItem.UNKNOWN_ARTICLE
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Article

        if (author != other.author) return false
        if (categories != other.categories) return false
        if (content != other.content) return false
        if (description != other.description) return false
        if (enclosure != other.enclosure) return false
        if (guid != other.guid) return false
        if (link != other.link) return false
        if (pubDate != other.pubDate) return false
        if (thumbnail != other.thumbnail) return false
        if (title != other.title) return false
        if (isBookmarked != other.isBookmarked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = author.hashCode()
        result = 31 * result + categories.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + enclosure.hashCode()
        result = 31 * result + guid.hashCode()
        result = 31 * result + link.hashCode()
        result = 31 * result + (pubDate?.hashCode() ?: 0)
        result = 31 * result + thumbnail.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + isBookmarked.hashCode()
        return result
    }
}
