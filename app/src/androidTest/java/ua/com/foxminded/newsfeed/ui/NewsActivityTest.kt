package ua.com.foxminded.newsfeed.ui

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.di.DaggerTestComponent

@RunWith(AndroidJUnit4ClassRunner::class)
class NewsActivityTest {

    private lateinit var activityScenario: ActivityScenario<NewsActivity>
    private lateinit var navController: NavController

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(NewsActivity::class.java)
        activityScenario.onActivity { activity ->
            App.instance.component = DaggerTestComponent.create()
            navController =
                (activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        }
    }

    @Test
    fun test_BottomNav() {
        assertEquals(R.id.allFeedsFragment, navController.currentDestination?.id)

        onView(allOf(withId(R.id.groupsFragment))).perform(click())
        assertEquals(R.id.groupsFragment, navController.currentDestination?.id)

        onView(withId(R.id.savedNewsFragment)).perform(click())
        assertEquals(R.id.savedNewsFragment, navController.currentDestination?.id)

        onView(withId(R.id.allFeedsFragment)).perform(click())
        assertEquals(R.id.allFeedsFragment, navController.currentDestination?.id)
    }
}
