package ua.com.foxminded.newsfeed.ui.articles.adapter

interface ViewHolderTypeProvider {

    fun getViewHolderType(): Int

    companion object {
        const val EMPTY_VIEW = 0
        const val NYT_ARTICLE = 1
        const val CNN_ARTICLE = 2
        const val WIRED_ARTICLE = 3
        const val UNKNOWN_ARTICLE = 4
    }
}
