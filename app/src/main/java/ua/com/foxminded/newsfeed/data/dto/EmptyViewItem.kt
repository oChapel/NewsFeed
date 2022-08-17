package ua.com.foxminded.newsfeed.data.dto

class EmptyViewItem : NewsItem {
    override fun getViewHolderType(): Int = NewsItem.EMPTY_VIEW
}
