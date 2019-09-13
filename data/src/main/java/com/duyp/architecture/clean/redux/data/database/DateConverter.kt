package com.duyp.architecture.clean.redux.data

import androidx.room.TypeConverter
import java.text.ParseException
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return try {
            if (value == null) null else Date(value)
        } catch (e: ParseException) {
            null
        }
    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return value?.time ?: 0
    }
}