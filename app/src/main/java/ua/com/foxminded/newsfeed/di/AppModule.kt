package ua.com.foxminded.newsfeed.di

import dagger.Module
import dagger.Provides
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.model.network.ConnectivityStatusListener
import ua.com.foxminded.newsfeed.model.network.DefaultConnectivityStatusListener
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolderImpl

@Module
class AppModule {

    @Provides
    fun provideDispatchersHolder() : DispatchersHolder {
        return DispatchersHolderImpl()
    }

    @Provides
    fun provideConnectivityStatusListener() : ConnectivityStatusListener {
        return DefaultConnectivityStatusListener(App.instance)
    }
}
