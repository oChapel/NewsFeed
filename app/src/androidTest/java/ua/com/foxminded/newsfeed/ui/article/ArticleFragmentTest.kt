package ua.com.foxminded.newsfeed.ui.article

import android.content.Context
import android.net.wifi.WifiManager
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.models.Mocks
import ua.com.foxminded.newsfeed.ui.NewsActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class ArticleFragmentTest {

    private lateinit var activityScenario: ActivityScenario<NewsActivity>
    private lateinit var navController: NavController
    private lateinit var wifiManager: WifiManager

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(NewsActivity::class.java)
        activityScenario.onActivity { activity ->
            wifiManager = activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
            navController =
                (activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        }
    }

    @Test
    fun test_SiteLoaded() {
        activityScenario.onActivity {
            val article = Mocks.getArticle(
                Mocks.NYT_TYPE, TEST_URL, 1, false
            )
            val bundle = bundleOf("article" to article)
            navController.navigate(R.id.articleFragment, bundle)
        }

        onView(withId(R.id.article_progress_bar)).check(matches(isDisplayed()))
        Thread.sleep(3000L)
        onView(withId(R.id.article_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.web_view)).check(matches(isDisplayed()))
        onView(withId(R.id.article_error_screen)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_OnError_TryAgainButtonClick() {
        activityScenario.onActivity {
            val article = Mocks.getArticle(
                Mocks.NYT_TYPE, TEST_URL, 1, false
            )
            val bundle = bundleOf("article" to article)
            switchWifi(false)
            navController.navigate(R.id.articleFragment, bundle)
        }

        onView(withId(R.id.article_progress_bar)).check(matches(isDisplayed()))
        Thread.sleep(2000L)
        onView(withId(R.id.article_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.web_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.article_error_screen)).check(matches(isDisplayed()))

        switchWifi(true)
        onView(withId(R.id.article_error_try_again_btn)).perform(click())
        onView(withId(R.id.article_progress_bar)).check(matches(isDisplayed()))
        Thread.sleep(4000L)
        onView(withId(R.id.article_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.web_view)).check(matches(isDisplayed()))
        onView(withId(R.id.article_error_screen)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_OnError_OkButtonClick() {
        activityScenario.onActivity {
            val article = Mocks.getArticle(
                Mocks.NYT_TYPE, TEST_URL, 1, false
            )
            val bundle = bundleOf("article" to article)
            switchWifi(false)
            navController.navigate(R.id.articleFragment, bundle)
        }

        onView(withId(R.id.article_progress_bar)).check(matches(isDisplayed()))
        Thread.sleep(2000L)
        onView(withId(R.id.article_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.web_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.article_error_screen)).check(matches(isDisplayed()))

        onView(withId(R.id.article_error_ok_btn)).perform(click())
        assertEquals(R.id.allFeedsFragment, navController.currentDestination?.id)
    }

    private fun switchWifi(isEnabled: Boolean) {
        val command = if (isEnabled) "svc wifi enable" else "svc wifi disable"
        if (wifiManager.isWifiEnabled != isEnabled) {
            device.executeShellCommand(command)
            if (isEnabled) Thread.sleep(5000L)
        }
    }

    companion object {
        private const val TEST_URL = "https://developer.android.com/"
    }
}
