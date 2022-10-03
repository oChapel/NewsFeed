package ua.com.foxminded.newsfeed.ui.articles.saved

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.CustomMatchers
import ua.com.foxminded.newsfeed.CustomViewActions
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.di.DaggerTestComponent
import ua.com.foxminded.newsfeed.models.dto.NewsItem
import ua.com.foxminded.newsfeed.ui.NewsActivity
import ua.com.foxminded.newsfeed.ui.articles.adapter.holders.NewsViewHolder

@RunWith(AndroidJUnit4ClassRunner::class)
class SavedNewsFragmentTest {

    private lateinit var navController: NavController

    @Before
    fun setUp() {
        val activityScenario = ActivityScenario.launch(NewsActivity::class.java)
        activityScenario.onActivity { activity ->
            App.instance.component = DaggerTestComponent.create()
            navController =
                (activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
            navController.navigate(R.id.savedNewsFragment)
        }
    }

    @Test
    fun test_SavedNewsLoaded() {
        assertRecyclerViewItems()
    }

    @Test
    fun test_DeleteArticle() {
        assertRecyclerViewItems()

        onView(withId(R.id.saved_news_recycler_view))
            .perform(actionOnItemAtPosition<NewsViewHolder<NewsItem>>(0, swipeLeft()))
        Thread.sleep(300L)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.article_deleted)))
        onView(withText(R.string.undo)).perform(click())

        assertRecyclerViewItems()

        onView(withId(R.id.saved_news_recycler_view))
            .perform(actionOnItemAtPosition<NewsViewHolder<NewsItem>>(1, swipeRight()))
        Thread.sleep(300L)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.article_deleted)))
        onView(withText(R.string.undo)).perform(click())

        assertRecyclerViewItems()

        onView(withId(R.id.saved_news_recycler_view))
            .perform(
                actionOnItemAtPosition<NewsViewHolder<NewsItem>>(
                    2, CustomViewActions.clickChildViewWithId(R.id.wired_news_bookmark)
                )
            )
        Thread.sleep(300L)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.article_deleted)))
        onView(withText(R.string.undo)).perform(click())

        assertRecyclerViewItems()
    }

    @Test
    fun testNavigation() {
        assertRecyclerViewItems()
        onView(withId(R.id.saved_news_recycler_view))
            .perform(actionOnItemAtPosition<NewsViewHolder<NewsItem>>(0, click()))
        assertEquals(navController.currentDestination?.id, R.id.articleFragment)
    }

    private fun assertRecyclerViewItems() {
        assertRecyclerViewItem(0, "NYT Title", "10 hours ago")
        assertRecyclerViewItem(1, "CNN Title", "11 hours ago")
        assertRecyclerViewItem(2, "WIRED Title", "12 hours ago")
        assertCorrectBookmarks()
    }

    private fun assertRecyclerViewItem(position: Int, title: String, pubDate: String) {
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
        onView(withId(R.id.saved_news_recycler_view))
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

    private fun assertCorrectBookmarks() {
        val positions = arrayOf(0, 1, 2)
        for (position in positions) {
            val id = when (position) {
                0 -> R.id.nyt_news_bookmark
                1 -> R.id.cnn_news_bookmark
                else -> R.id.wired_news_bookmark
            }
            onView(withId(R.id.saved_news_recycler_view))
                .check(
                    matches(
                        CustomMatchers.atPosition(
                            position, hasDescendant(
                                allOf(
                                    withId(id), CustomMatchers.withDrawableId(R.drawable.ic_bookmark_saved)
                                )
                            )
                        )
                    )
                )
        }
    }
}
