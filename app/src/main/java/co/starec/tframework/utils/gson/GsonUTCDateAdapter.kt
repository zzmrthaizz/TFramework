package co.starec.tframework.utils.gson

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import co.starec.tframework.app.Constants
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class GsonUTCDateAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {

    private val dateFormat: DateFormat

    init {
        dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)      //This is the format I need
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))       //This is the key line which converts the date to UTC which cannot be accessed with the default serializer
    }

    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        try {
            return dateFormat.parse(jsonElement.asString)
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }

    }

    override fun serialize(date: Date, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(dateFormat.format(date))
    }
}