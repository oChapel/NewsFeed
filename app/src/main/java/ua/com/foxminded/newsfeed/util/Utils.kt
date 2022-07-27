package ua.com.foxminded.newsfeed.util

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    @SuppressLint("SimpleDateFormat")
    fun getTimeSpanString(dateStr: String?): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }
        dateStr?.let {
            format.parse(dateStr)?.let {
                return DateUtils.getRelativeTimeSpanString(
                    it.time,
                    Calendar.getInstance().timeInMillis,
                    DateUtils.MINUTE_IN_MILLIS
                ).toString()
            }
        }
        return ""
    }
}