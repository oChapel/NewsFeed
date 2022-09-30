package ua.com.foxminded.newsfeed

import org.junit.runner.RunWith
import org.junit.runners.Suite
import ua.com.foxminded.newsfeed.ui.articles.news.feed.SingleFeedFragmentTest
import ua.com.foxminded.newsfeed.ui.articles.news.feeds.AllFeedsFragmentTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AllFeedsFragmentTest::class,
    SingleFeedFragmentTest::class
)
class AndroidTestSuiteClass
