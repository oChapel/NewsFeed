package ua.com.foxminded.newsfeed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.data.NewsRepository
import ua.com.foxminded.newsfeed.di.AppComponent
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListViewModel
import ua.com.foxminded.newsfeed.ui.articles.saved.SavedNewsViewModel
import javax.inject.Inject

class NewsViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    private val appComponent: AppComponent = App.component

    @Inject
    lateinit var repository: NewsRepository

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        appComponent.inject(this)
        if (modelClass.isAssignableFrom(NewsListViewModel::class.java)) {
            return NewsListViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SavedNewsViewModel::class.java)) {
            return SavedNewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}