package ua.com.foxminded.newsfeed.ui.articles.news.feeds

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListFragmentTest

@RunWith(AndroidJUnit4ClassRunner::class)
class AllFeedsFragmentTest : NewsListFragmentTest() {

    override val threadSleepTime: Long
        get() = 3500L

    override fun navigateToFragment() {
        activityScenario.onActivity {
            navController.navigate(R.id.allFeedsFragment)
        }
    }

    override fun assertNewsLoaded() {
        assertRecyclerViewItem(0, "NYT Title", "1 hour ago")
        assertRecyclerViewItem(1, "NYT Title", "2 hours ago")
        assertRecyclerViewItem(2, "NYT Title", "3 hours ago")
        assertRecyclerViewItem(3, "CNN Title", "4 hours ago")
        assertRecyclerViewItem(4, "CNN Title", "5 hours ago")
        assertRecyclerViewItem(5, "CNN Title", "6 hours ago")
        assertRecyclerViewItem(6, "WIRED Title", "7 hours ago")
        assertRecyclerViewItem(7, "WIRED Title", "8 hours ago")
        assertRecyclerViewItem(8, "WIRED Title", "9 hours ago")
    }

    @Before
    fun setUp() {
        super.setUpScenario()
    }

    @Test
    override fun test_CheckNewsLoaded() {
        super.test_CheckNewsLoaded()
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