package ua.com.foxminded.newsfeed.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val author: String,
    val categories: List<String>,
    val content: String,
    val description: String,
    val enclosure: Enclosure,
    val guid: String,
    val link: String,
    val pubDate: String,
    val thumbnail: String,
    val title: String
) : Serializable