package ua.com.foxminded.newsfeed.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.model.Item

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Item)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Item>>

    @Query("SELECT EXISTS (SELECT 1 FROM articles WHERE title = :title)")
    suspend fun existsInDb(title: String): Boolean

    //TODO
    @Query("DELETE FROM articles WHERE title = :title")
    suspend fun deleteArticleByTitle(title: String)

    @Delete
    suspend fun deleteArticle(article: Item)
}
