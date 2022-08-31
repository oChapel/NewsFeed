package ua.com.foxminded.newsfeed.models.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.models.dto.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(list: List<Article>)

    @Query("SELECT * FROM articles ORDER BY pubDate DESC LIMIT :limit")
    suspend fun getAllCachedNews(limit: Int): List<Article>

    @Query("SELECT * FROM articles WHERE link LIKE '%' || :domain || '%' ORDER BY pubDate DESC LIMIT :limit")
    suspend fun getCachedNewsBySource(limit: Int, domain: String): List<Article>

    @Query("SELECT * FROM articles WHERE isBookmarked = 1")
    fun getSavedNewsFlow(): Flow<List<Article>>

    @Query("SELECT EXISTS (SELECT 1 FROM articles WHERE guid = :guid AND isBookmarked = 1)")
    suspend fun isBookmarked(guid: String): Boolean

    @Delete
    suspend fun deleteArticle(article: Article)
}
