package ua.com.foxminded.newsfeed.data

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dao.NewsDao
import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.data.model.NewsResponse
import ua.com.foxminded.newsfeed.data.network.NewsNetwork
import ua.com.foxminded.newsfeed.util.Result

class DefaultNewsRepository(
    private val localDataSource: NewsDao,
    private val remoteDataSource: NewsNetwork
) : NewsRepository {

    override suspend fun loadAllNews(): Result<List<NewsResponse>> {
        return remoteDataSource.getAllNews()
    }

    override suspend fun getNytNews(): Result<NewsResponse> {
        return remoteDataSource.getNytNews()
    }

    override suspend fun getCnnNews(): Result<NewsResponse> {
        return remoteDataSource.getCnnNews()
    }

    override suspend fun getWiredNews(): Result<NewsResponse> {
        return remoteDataSource.getWiredNews()
    }

    override suspend fun saveArticle(article: Item) {
        localDataSource.insertArticle(article)
    }

    override fun getAllArticlesFromDb(): Flow<List<Item>> {
        return localDataSource.getAllArticles()
    }

    override suspend fun existsInDb(title: String): Boolean {
        return localDataSource.existsInDb(title)
    }

    override suspend fun deleteArticleByTitle(title: String) {
        localDataSource.deleteArticleByTitle(title)
    }

    override suspend fun deleteArticle(article: Item) {
        localDataSource.deleteArticle(article)
    }
}
