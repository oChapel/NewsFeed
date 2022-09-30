package ua.com.foxminded.newsfeed.di

import dagger.Module
import dagger.Provides
import ua.com.foxminded.newsfeed.models.DefaultNewsRepository
import ua.com.foxminded.newsfeed.models.NewsRepository
import ua.com.foxminded.newsfeed.models.dao.NewsDao
import ua.com.foxminded.newsfeed.models.dao.NewsTestDao
import ua.com.foxminded.newsfeed.models.network.NewsNetwork
import javax.inject.Singleton

@Module
class DataTestModule {

    @Provides
    @Singleton
    fun provideTestRepository(
        localDataSource: NewsDao,
        remoteDataSource: NewsNetwork
    ): NewsRepository {
        return DefaultNewsRepository(localDataSource, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideTestDao(): NewsDao {
        return NewsTestDao()
    }
}