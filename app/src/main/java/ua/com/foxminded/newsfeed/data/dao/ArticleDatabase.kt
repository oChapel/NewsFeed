package ua.com.foxminded.newsfeed.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.com.foxminded.newsfeed.data.Article
import ua.com.foxminded.newsfeed.data.dao.util.RoomTypeConverters

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        private var instance: ArticleDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ArticleDatabase {
            return instance ?: Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
