package ua.com.foxminded.newsfeed

import android.app.Application
import ua.com.foxminded.newsfeed.di.AppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
        lateinit var component: AppComponent
    }
}
