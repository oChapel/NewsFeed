package ua.com.foxminded.newsfeed.ui.articles.news

import android.content.Context
import android.net.wifi.WifiManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.CustomMatchers
import ua.com.foxminded.newsfeed.CustomViewActions
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.di.DaggerTestComponent
import ua.com.foxminded.newsfeed.models.dto.NewsItem
import ua.com.foxminded.newsfeed.ui.NewsActivity
import ua.com.foxminded.newsfeed.ui.articles.adapter.holders.NewsViewHolder

abstract class NewsListFragmentTest {

    lateinit var activityScenario: ActivityScenario<NewsActivity>
    lateinit var navController: NavController
    private lateinit var wifiManager: WifiManager

    abstract val threadSleepTime: Long
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    abstract fun navigateToFragment()

    abstract fun assertNewsLoaded()

    open fun setUpScenario() {
        activityScenario = ActivityScenario.launch(NewsActivity::class.java)
        activityScenario.onActivity { activity ->
            App.instance.component = DaggerTestComponent.create()
            wifiManager = activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
            navController =
                (activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        }
    }

    open fun test_CheckNewsLoaded() {
        switchWifi(true)
        navigateToFragment()

        assertSwipeToRefreshStates(threadSleepTime)
        assertNewsLoaded()
    }

    open fun test_CheckNewsLoaded_Offline() {
        switchWifi(false)
        navigateToFragment()

        assertSwipeToRefreshStates(OFFLINE_SLEEP_TIME)
        assertOfflineToastMessageAppeared()
    }

    open fun test_Refresh() {
        switchWifi(true)
        navigateToFragment()

        assertSwipeToRefreshStates(threadSleepTime)
        assertNewsLoaded()

        onView(withId(R.id.news_list_recycler_view))
            .perform(scrollToPosition<NewsViewHolder<NewsItem>>(0))
            .perform(swipeDown())

        assertSwipeToRefreshStates(threadSleepTime)
        assertNewsLoaded()
    }

    open fun test_CheckPopupWindow() {
        switchWifi(true)
        navigateToFragment()

        assertSwipeToRefreshStates(threadSleepTime)
        assertNewsLoaded()

        switchWifi(false)
        assertOfflineToastMessageAppeared()
        Thread.sleep(500L)
        switchWifi(true)

        Thread.sleep(500L)
        onView(withId(R.id.popup_root_view))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        assertSwipeToRefreshStates(threadSleepTime)
        assertNewsLoaded()
    }

    open fun test_SaveDeleteArticle() {
        switchWifi(true)
        navigateToFragment()

        assertSwipeToRefreshStates(threadSleepTime)
        assertNewsLoaded()

        onView(withId(R.id.news_list_recycler_view))
            .perform(scrollToPosition<NewsViewHolder<NewsItem>>(0))
        performOnBookmarkClick()
        Thread.sleep(300L)
        assertCorrectBookmark(true)
        performOnBookmarkClick()
        Thread.sleep(300L)
        assertCorrectBookmark(false)
    }

    open fun testNavigation_ToArticleFragment() {
        switchWifi(true)
        navigateToFragment()

        assertSwipeToRefreshStates(threadSleepTime)
        assertNewsLoaded()

        onView(withId(R.id.news_list_recycler_view))
            .perform(actionOnItemAtPosition<NewsViewHolder<NewsItem>>(0, click()))
        assertEquals(navController.currentDestination?.id, R.id.articleFragment)
    }

    protected fun switchWifi(isEnabled: Boolean) {
        val command = if (isEnabled) "svc wifi enable" else "svc wifi disable"
        if (wifiManager.isWifiEnabled != isEnabled) {
            device.executeShellCommand(command)
            if (isEnabled) Thread.sleep(5000L)
        }
    }

    protected fun assertSwipeToRefreshStates(sleepTime: Long) {
        assertSwipeToRefreshState(true)
        Thread.sleep(sleepTime)
        assertSwipeToRefreshState(false)
    }

    private fun assertSwipeToRefreshState(isRefreshing: Boolean) {
        onView(withId(R.id.news_swipe_refresh)).check(
            matches(
                if (isRefreshing) CustomMatchers.isRefreshing() else not(CustomMatchers.isRefreshing())
            )
        )
    }

    protected fun assertRecyclerViewItem(position: Int, title: String, pubDate: String) {
        val titleId: Int
        val timespanId: Int
        if (title.contains("NYT")) {
            titleId = R.id.nyt_news_title
            timespanId = R.id.nyt_news_timespan
        } else if (title.contains("CNN")) {
            titleId = R.id.cnn_news_title
            timespanId = R.id.cnn_news_timespan
        } else {
            titleId = R.id.wired_news_title
            timespanId = R.id.wired_news_timespan
        }
        onView(withId(R.id.news_list_recycler_view))
            .perform(scrollToPosition<NewsViewHolder<NewsItem>>(position))
            .check(
                matches(
                    CustomMatchers.atPosition(
                        position, hasDescendant(
                            allOf(
                                withId(titleId), withText(title)
                            )
                        )
                    )
                )
            )
            .check(
                matches(
                    CustomMatchers.atPosition(
                        position, hasDescendant(
                            allOf(
                                withId(timespanId), withText(pubDate)
                            )
                        )
                    )
                )
            )
    }

    private fun performOnBookmarkClick() {
        onView(withId(R.id.news_list_recycler_view))
            .perform(
                actionOnItemAtPosition<NewsViewHolder<NewsItem>>(
                    0, CustomViewActions.clickChildViewWithId(R.id.nyt_news_bookmark)
                )
            )
    }

    private fun assertCorrectBookmark(isSaved: Boolean) {
        val id = if (isSaved) R.drawable.ic_bookmark_saved else R.drawable.ic_bookmark
        onView(withId(R.id.news_list_recycler_view))
            .check(
                matches(
                    CustomMatchers.atPosition(
                        0, hasDescendant(
                            allOf(
                                withId(R.id.nyt_news_bookmark), CustomMatchers.withDrawableId(id)
                            )
                        )
                    )
                )
            )
    }

    private fun assertOfflineToastMessageAppeared() {
        onView(withText(R.string.offline_mode))
            .inRoot(CustomMatchers.ToastMatcher())
            .check(matches(isDisplayed()))
            .check(matches(withText(OFFLINE_TOAST_STRING)))
    }

    companion object {
        const val OFFLINE_SLEEP_TIME = 3000L
        const val OFFLINE_TOAST_STRING =
            "It seems you are currently offline. \n Please restore your internet connection."
    }
}