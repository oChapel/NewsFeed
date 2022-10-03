package ua.com.foxminded.newsfeed.ui.groups

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.di.DaggerTestComponent
import ua.com.foxminded.newsfeed.ui.NewsActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class GroupsFragmentTest {

    private lateinit var navController: NavController
    private lateinit var activityScenario: ActivityScenario<NewsActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(NewsActivity::class.java)
        activityScenario.onActivity { activity ->
            App.instance.component = DaggerTestComponent.create()
            navController =
                (activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
            navController.navigate(R.id.groupsFragment)
        }
    }

    @Test
    fun testNavigation_ToNytFeedFragment() {
        onView(withId(R.id.group_nyt)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.singleFeedFragment)
        activityScenario.onActivity { activity ->
            assertEquals("The New York Times", activity.supportActionBar?.title)
        }
    }

    @Test
    fun testNavigation_ToCnnFeedFragment() {
        onView(withId(R.id.group_cnn)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.singleFeedFragment)
        activityScenario.onActivity { activity ->
            assertEquals("CNN", activity.supportActionBar?.title)
        }
    }

    @Test
    fun testNavigation_ToWiredFeedFragment() {
        onView(withId(R.id.group_wired)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.singleFeedFragment)
        activityScenario.onActivity { activity ->
            assertEquals("WIRED", activity.supportActionBar?.title)
        }
    }
}