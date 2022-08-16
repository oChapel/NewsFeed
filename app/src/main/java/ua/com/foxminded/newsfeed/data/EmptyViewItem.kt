package ua.com.foxminded.newsfeed.data

class EmptyViewItem : NewsItem {
    override fun getViewHolderType(): Int = NewsItem.EMPTY_VIEW
}
