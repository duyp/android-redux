package com.duyp.architecture.clean.redux.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GithubDateDeserializer : JsonDeserializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        element: JsonElement,
        arg1: Type,
        arg2: JsonDeserializationContext
    ): Date? {
        val date = element.asString
        val formatter = SimpleDateFormat(ApiConstants.DATE_TIME_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            formatter.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}