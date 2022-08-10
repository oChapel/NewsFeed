package ua.com.foxminded.newsfeed.ui.articles.saved.state

import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.mvi.states.AbstractEffect
import ua.com.foxminded.newsfeed.ui.articles.saved.SavedNewsContract

sealed class SavedNewsScreenEffect : AbstractEffect<SavedNewsContract.View>() {

    class ShowErrorToast(private val stringId: Int) : SavedNewsScreenEffect() {
        override fun handle(screen: SavedNewsContract.View) {
            screen.showToast(stringId)
        }
    }

    class ShowUndoSnackBar(private val article: Article) : SavedNewsScreenEffect() {
        override fun handle(screen: SavedNewsContract.View) {
            screen.showUndoSnackBar(article)
        }
    }
}
