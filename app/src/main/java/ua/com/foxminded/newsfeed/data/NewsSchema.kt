package ua.com.foxminded.newsfeed.data

import androidx.room.Entity
import androidx.room.Ignore
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
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val author: String = "",
    val categories: List<String> = listOf(),
    val content: String = "",
    val description: String = "",
    val enclosure: Enclosure = Enclosure(),
    val guid: String = "",
    val link: String = "",
    val pubDate: String = "",
    val thumbnail: String = "",
    val title: String = ""
) : Serializable {
    @Ignore
    var isSaved: Boolean = false

    companion object {
        const val NYT_DOMAIN = "nytimes.com"
        const val CNN_DOMAIN = "cnn.com"
        const val WIRED_DOMAIN = "wired.com"
    }
}
