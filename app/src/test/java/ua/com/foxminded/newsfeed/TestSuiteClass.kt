package ua.com.foxminded.newsfeed

import org.junit.runner.RunWith
import org.junit.runners.Suite
import ua.com.foxminded.newsfeed.models.NewsRepositoryTest
import ua.com.foxminded.newsfeed.ui.articles.news.feed.SingleFeedViewModelTest
import ua.com.foxminded.newsfeed.ui.articles.news.feeds.AllFeedsViewModelTest
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenEffectTest
import ua.com.foxminded.newsfeed.ui.articles.news.state.NewsListScreenStateTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
    NewsRepositoryTest::class,
    SingleFeedViewModelTest::class,
    AllFeedsViewModelTest::class,
    NewsListScreenStateTest::class,
    NewsListScreenEffectTest::class
)
class TestSuiteClass