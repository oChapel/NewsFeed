package ua.com.foxminded.newsfeed.data.dao

import androidx.room.*
import ua.com.foxminded.newsfeed.data.model.Item

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Item)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<Item>

    @Delete
    suspend fun deleteArticle(article: Item)
}
