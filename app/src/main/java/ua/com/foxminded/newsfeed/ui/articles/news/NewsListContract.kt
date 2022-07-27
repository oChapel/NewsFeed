package ua.com.foxminded.newsfeed.ui.articles.news

import ua.com.foxminded.newsfeed.data.dto.Item
import ua.com.foxminded.newsfeed.mvi.fragments.FragmentContract
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenState

class NewsListContract {

    interface ViewModel : FragmentContract.ViewModel<NewsListScreenState, NewsListScreenEffect> {
        fun loadNews()
        fun onBookmarkClicked(article: Item)
    }

    interface View : FragmentContract.View {
        fun setProgress(isVisible: Boolean)
        fun showNews(list: List<Item>)
        fun showToast(stringId: Int)
        fun onItemChanged(article: Item, isArticleInDb: Boolean)
    }

    interface Host : FragmentContract.Host
}
