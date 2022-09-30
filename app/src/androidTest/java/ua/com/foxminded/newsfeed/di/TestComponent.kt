package ua.com.foxminded.newsfeed.di

import dagger.Component
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import javax.inject.Singleton

@Component(modules = [AppModule::class, DataTestModule::class, NetworkTestModule::class])
@Singleton
interface TestComponent : AppComponent
