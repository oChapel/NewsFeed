package ua.com.foxminded.newsfeed.data

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dao.NewsDao
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.data.dto.NewsSchema
import ua.com.foxminded.newsfeed.data.network.NewsNetwork

class DefaultNewsRepository(
    private val localDataSource: NewsDao,
    private val remoteDataSource: NewsNetwork
) : NewsRepository {

    override suspend fun loadAllNews(page: Int): List<NewsSchema> {
        return remoteDataSource.getAllNews(page, NewsRepository.MULTIPLE_SOURCE_PAGE_SIZE)
    }

    override suspend fun loadNytNews(page: Int): NewsSchema {
        return remoteDataSource.getNytNews(page, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
    }

    override suspend fun loadCnnNews(page: Int): NewsSchema {
        return remoteDataSource.getCnnNews(page, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
    }

    override suspend fun loadWiredNews(page: Int): NewsSchema {
        return remoteDataSource.getWiredNews(page, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
    }

    override suspend fun saveArticle(article: Article) {
        localDataSource.insertArticle(article)
    }

    override suspend fun saveNews(list: List<Article>) {
        localDataSource.insertNews(list)
    }

    override suspend fun getAllCachedNews(page: Int): List<Article> {
        return localDataSource.getAllCachedNews(page, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
    }

    override suspend fun getCachedNewsBySource(page: Int, domain: String): List<Article> {
        return localDataSource.getCachedNewsBySource(
            page,
            NewsRepository.SINGLE_SOURCE_PAGE_SIZE,
            domain
        )
    }

    override fun getSavedNews(): Flow<List<Article>> {
        return localDataSource.getSavedNewsFlow()
    }

    override suspend fun isBookmarked(guid: String): Boolean {
        return localDataSource.isBookmarked(guid)
    }

    override suspend fun deleteArticle(article: Article) {
        localDataSource.deleteArticle(article)
    }
}
