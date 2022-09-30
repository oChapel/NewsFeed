package ua.com.foxminded.newsfeed

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object CustomMatchers {

    fun isRefreshing(): Matcher<View> = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("is SwipeRefreshLayout currently refreshing")
        }

        override fun matchesSafely(item: View?) =
            (item as? SwipeRefreshLayout)?.isRefreshing ?: false

    }

    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView?): Boolean {
                val viewHolder = view?.findViewHolderForAdapterPosition(position)
                viewHolder?.let { return itemMatcher.matches(viewHolder.itemView) } ?: return false
            }
        }
    }

    fun withDrawableId(id: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.appendText("with drawable from resource id: $id")
            }

            override fun matchesSafely(view: View?): Boolean {
                val drawable: Drawable? = when(view) {
                    is ActionMenuItemView -> view.itemData?.icon
                    is ImageView -> view.drawable
                    else -> null
                }
                requireNotNull(drawable)

                val resources: Resources? = view?.context?.resources
                val expectedDrawable: Drawable? = resources?.getDrawable(id, view.context.theme)
                return expectedDrawable?.constantState?.let { it == drawable.constantState } ?: false
            }
        }
    }

    class ToastMatcher : TypeSafeMatcher<Root>() {

        override fun describeTo(description: Description?) {
            description?.appendText("is toast")
        }

        override fun matchesSafely(root: Root?): Boolean {
            val type = root?.windowLayoutParams?.get()?.type
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                val windowToken = root.decorView.windowToken
                val appToken = root.decorView.applicationWindowToken
                return windowToken == appToken
            }
            return false
        }
    }
}