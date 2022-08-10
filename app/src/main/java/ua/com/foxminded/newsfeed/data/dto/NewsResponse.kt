package ua.com.foxminded.newsfeed.data.dto

data class NewsResponse(
    val feed: Feed,
    val items: List<Article>,
    val status: String
)
