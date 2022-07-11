package ua.com.foxminded.newsfeed.data.model

data class NewsResponse(
    val feed: Feed,
    val items: List<Item>,
    val status: String
)