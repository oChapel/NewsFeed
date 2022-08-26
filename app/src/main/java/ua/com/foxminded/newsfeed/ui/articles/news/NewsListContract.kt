package ua.com.foxminded.newsfeed.ui.articles.news

import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.data.dto.NewsItem
import ua.com.foxminded.newsfeed.mvi.fragments.FragmentContract
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState

class NewsListContract {

    interface ViewModel : FragmentContract.ViewModel<NewsListScreenState, NewsListScreenEffect> {
        fun reload()
        fun loadNews(page: Int)
        fun onBookmarkClicked(article: Article)
        fun onPopupClicked()
    }

    interface View : FragmentContract.View {
        fun setProgress(isVisible: Boolean)
        fun showNews(list: List<NewsItem>)
        fun showErrorDialog(error: Throwable)
        fun showToast(resId: Int)
        fun showPopupWindow()
    }

    interface Host : FragmentContract.Host {
        fun showErrorDialog(error: Throwable)
    }
}
