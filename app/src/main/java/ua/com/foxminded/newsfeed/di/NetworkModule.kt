package ua.com.foxminded.newsfeed.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.com.foxminded.newsfeed.data.network.NewsFeedApi
import ua.com.foxminded.newsfeed.data.network.DefaultNewsNetwork
import ua.com.foxminded.newsfeed.data.network.NewsNetwork
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideNewsNetwork(api: NewsFeedApi): NewsNetwork {
        return DefaultNewsNetwork(api)
    }

    @Provides
    @Singleton
    fun provideNewsFeedApi(retrofit: Retrofit): NewsFeedApi {
        return retrofit.create(NewsFeedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .also { it.level = HttpLoggingInterceptor.Level.BODY }
    }

    companion object {
        private const val BASE_URL = "https://api.rss2json.com"
    }
}