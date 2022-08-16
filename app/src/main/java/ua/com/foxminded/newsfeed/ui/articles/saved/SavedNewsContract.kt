package ua.com.foxminded.newsfeed.ui.articles.saved

import ua.com.foxminded.newsfeed.data.Article
import ua.com.foxminded.newsfeed.data.NewsItem
import ua.com.foxminded.newsfeed.mvi.fragments.FragmentContract
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenState

class SavedNewsContract {

    interface ViewModel : FragmentContract.ViewModel<SavedNewsScreenState, SavedNewsScreenEffect> {
        fun onArticleStateChanged(article: Article)
    }

    interface View : FragmentContract.View {
        fun showNews(list: List<NewsItem>)
        fun showToast(stringId: Int)
        fun showUndoSnackBar(article: Article)
    }

    interface Host : FragmentContract.Host
}
