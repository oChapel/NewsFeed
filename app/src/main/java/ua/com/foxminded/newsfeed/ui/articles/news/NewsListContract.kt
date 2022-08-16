package ua.com.foxminded.newsfeed.ui.articles.news

import ua.com.foxminded.newsfeed.data.Article
import ua.com.foxminded.newsfeed.data.NewsItem
import ua.com.foxminded.newsfeed.mvi.fragments.FragmentContract
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState

class NewsListContract {

    interface ViewModel : FragmentContract.ViewModel<NewsListScreenState, NewsListScreenEffect> {
        fun loadNews()
        fun onBookmarkClicked(article: Article)
    }

    interface View : FragmentContract.View {
        fun setProgress(isVisible: Boolean)
        fun showNews(list: List<NewsItem>)
        fun showErrorDialog(error: Throwable)
    }

    interface Host : FragmentContract.Host {
        fun showErrorDialog(error: Throwable)
    }
}
