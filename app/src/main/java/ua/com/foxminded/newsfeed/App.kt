package ua.com.foxminded.newsfeed

import android.app.Application
import ua.com.foxminded.newsfeed.di.AppComponent
import ua.com.foxminded.newsfeed.di.DaggerAppComponent

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerAppComponent.create()
    }

    companion object {
        lateinit var instance: App
    }
}
