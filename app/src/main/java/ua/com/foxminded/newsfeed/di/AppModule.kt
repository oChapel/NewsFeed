package ua.com.foxminded.newsfeed.di

import dagger.Module
import dagger.Provides
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.models.network.listener.ConnectivityStatusListener
import ua.com.foxminded.newsfeed.models.network.listener.DefaultConnectivityStatusListener
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolderImpl

@Module
class AppModule {

    @Provides
    fun provideDispatchersHolder(): DispatchersHolder {
        return DispatchersHolderImpl()
    }

    @Provides
    fun provideConnectivityStatusListener(): ConnectivityStatusListener {
        return DefaultConnectivityStatusListener(App.instance)
    }
}
