package ua.com.foxminded.newsfeed.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object Util {

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US) //TODO

    fun getTimeSpanString(dateStr: String?): String {
        dateStr?.let {
            inputFormat.parse(dateStr)?.let {
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