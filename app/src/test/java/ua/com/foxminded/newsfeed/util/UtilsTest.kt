package ua.com.foxminded.newsfeed.util

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun test_GetTimeSpanCount() {
        /*val date = "2000-01-01 01:00:00"
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }

        Calendar.getInstance().time = format.parse(date) as Date*/

        assertEquals("1 hour ago", Utils.getTimeSpanString("2000-01-01 00:00:00"))
    }
}
