package ua.com.foxminded.newsfeed.ui.articles.news.feed

import androidx.core.os.bundleOf
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListFragmentTest

@RunWith(AndroidJUnit4ClassRunner::class)
class SingleFeedFragmentTest : NewsListFragmentTest() {

    override val threadSleepTime: Long
        get() = 1500L

    override fun navigateToFragment() {
        activityScenario.onActivity {
            val bundle = bundleOf("sourceType" to SourceTypes.NYT_FEED)
            navController.navigate(R.id.singleFeedFragment, bundle)
        }
    }

    override fun assertNewsLoaded() {
        assertRecyclerViewItem(0, "NYT Title", "1 hour ago")
        assertRecyclerViewItem(1, "NYT Title", "2 hours ago")
        assertRecyclerViewItem(2, "NYT Title", "3 hours ago")
    }

    @Before
    fun setUp() {
        super.setUpScenario()
    }

    @Test
    fun test_CheckNewsLoaded_NYT() {
        super.test_CheckNewsLoaded()
    }

    @Test
    fun test_checkNewsLoaded_CNN() {
        switchWifi(true)
        activityScenario.onActivity {
            val bundle = bundleOf("sourceType" to SourceTypes.CNN_FEED)
            navController.navigate(R.id.singleFeedFragment, bundle)
        }

        assertSwipeToRefreshStates(threadSleepTime)
        assertRecyclerViewItem(0, "CNN Title", "4 hours ago")
        assertRecyclerViewItem(1, "CNN Title", "5 hours ago")
        assertRecyclerViewItem(2, "CNN Title", "6 hours ago")
    }

    @Test
    fun test_checkNewsLoaded_WIRED() {
        switchWifi(true)
        activityScenario.onActivity {
            val bundle = bundleOf("sourceType" to SourceTypes.WIRED_FEED)
            navController.navigate(R.id.singleFeedFragment, bundle)
        }

        assertSwipeToRefreshStates(threadSleepTime)
        assertRecyclerViewItem(0, "WIRED Title", "7 hours ago")
        assertRecyclerViewItem(1, "WIRED Title", "8 hours ago")
        assertRecyclerViewItem(2, "WIRED Title", "9 hours ago")
    }

    @Test
    override fun test_CheckNewsLoaded_Offline() {
        super.test_CheckNewsLoaded_Offline()
    }

    @Test
    override fun test_Refresh() {
        super.test_Refresh()
    }

    @Test
    override fun test_CheckPopupWindow() {
        super.test_CheckPopupWindow()
    }

    @Test
    override fun test_SaveDeleteArticle() {
        super.test_SaveDeleteArticle()
    }

    @Test
    override fun testNavigation_ToArticleFragment() {
        super.testNavigation_ToArticleFragment()
    }
}
