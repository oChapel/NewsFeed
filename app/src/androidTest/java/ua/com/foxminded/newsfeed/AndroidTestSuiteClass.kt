package ua.com.foxminded.newsfeed

import org.junit.runner.RunWith
import org.junit.runners.Suite
import ua.com.foxminded.newsfeed.ui.NewsActivityTest
import ua.com.foxminded.newsfeed.ui.article.ArticleFragmentTest
import ua.com.foxminded.newsfeed.ui.articles.news.feed.SingleFeedFragmentTest
import ua.com.foxminded.newsfeed.ui.articles.news.feeds.AllFeedsFragmentTest
import ua.com.foxminded.newsfeed.ui.articles.saved.SavedNewsFragmentTest
import ua.com.foxminded.newsfeed.ui.groups.GroupsFragmentTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
    NewsActivityTest::class,
    AllFeedsFragmentTest::class,
    SingleFeedFragmentTest::class,
    SavedNewsFragmentTest::class,
    GroupsFragmentTest::class,
    ArticleFragmentTest::class
)
class AndroidTestSuiteClass
