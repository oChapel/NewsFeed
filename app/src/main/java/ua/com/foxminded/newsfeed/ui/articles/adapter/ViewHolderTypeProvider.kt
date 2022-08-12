package ua.com.foxminded.newsfeed.ui.articles.adapter

import ua.com.foxminded.newsfeed.data.Article

interface ViewHolderTypeProvider {

    fun Article.getViewHolderType(): Int {
        return when {
            link == "" -> EMPTY_VIEW
            link.contains(Article.NYT_DOMAIN, ignoreCase = true) -> NYT_ARTICLE
            link.contains(Article.CNN_DOMAIN, ignoreCase = true) -> CNN_ARTICLE
            link.contains(Article.WIRED_DOMAIN, ignoreCase = true) -> WIRED_ARTICLE
            else -> UNKNOWN_ARTICLE
        }
    }

    companion object {
        const val EMPTY_VIEW = 0
        const val NYT_ARTICLE = 1
        const val CNN_ARTICLE = 2
        const val WIRED_ARTICLE = 3
        const val UNKNOWN_ARTICLE = 4
    }
}
