package ua.com.foxminded.newsfeed.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticlesFlow(): Flow<List<Article>>

    @Query("SELECT EXISTS (SELECT 1 FROM articles WHERE guid = :guid)")
    suspend fun existsInDb(guid: String): Boolean

    @Query("DELETE FROM articles WHERE guid = :guid")
    suspend fun deleteArticleByGuid(guid: String)

    @Delete
    suspend fun deleteArticle(article: Article)
}
