package ua.com.foxminded.newsfeed.models.dao.util

import androidx.room.TypeConverter
import ua.com.foxminded.newsfeed.models.dto.Enclosure

class RoomTypeConverters {

    @TypeConverter
    fun fromEnclosure(enclosure: Enclosure): String {
        return enclosure.link
    }

    @TypeConverter
    fun toEnclosure(link: String): Enclosure {
        return Enclosure(link, null)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        var value = ""
        for (category in list) {
            value += "$category, "
        }
        return value
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",")
    }
}
