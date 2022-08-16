package ua.com.foxminded.newsfeed

import android.app.Application
import ua.com.foxminded.newsfeed.di.AppComponent

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}
