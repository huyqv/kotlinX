package com.example.library.extension

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.math.BigDecimal
import kotlin.reflect.KClass



/**
 * Parse [JsonObject]/[JsonArray]/[String] to Kotlin Object/List<Object>
 */
private val convertFactory: Gson by lazy {
    Gson()
}

fun <T> JsonObject?.parse(cls: Class<T>): T? {
    return this?.toString()?.parse(cls)
}

fun <T> JsonArray?.parse(cls: Class<Array<T>>): List<T>? {
    return this?.toString()?.parse(cls)
}

fun <T : Any> JsonObject?.parse(cls: KClass<T>): T? {
    return this?.toString()?.parse(cls)
}

fun <T : Any> JsonArray?.parse(cls: KClass<Array<T>>): List<T>? {
    return this?.toString()?.parse(cls)
}

fun <T> String?.parse(cls: Class<T>): T? {
    this ?: return null
    if (isNullOrEmpty()) {
        return null
    }
    return try {
        return convertFactory.fromJson(this, cls)
    } catch (ignore: Exception) {
        null
    }
}

fun <T> String?.parse(cls: Class<Array<T>>): List<T>? {
    this ?: return null
    if (isNullOrEmpty()) {
        return null
    }
    return try {
        return convertFactory.fromJson(StringReader(this), cls).toList()
    } catch (ignore: Exception) {
        null
    }
}

fun <T : Any> String?.parse(cls: KClass<T>): T? {
    return this?.parse(cls.java)
}

fun <T : Any> String?.parse(cls: KClass<Array<T>>): List<T>? {
    return this?.parse(cls.java)
}

fun <T> T.toJsonObject(): JsonObject? {
    return try {
        val element = convertFactory.toJsonTree(this, object : TypeToken<T>() {}.type)
        return element.asJsonObject
    } catch (ignore: Exception) {
        null
    }
}

fun <T> Collection<T?>?.toJsonArray(): JsonArray? {
    if (this.isNullOrEmpty()) return null
    val jsonArray = JsonArray()
    this.forEach {
        when (it) {
            is JsonObject -> {
                jsonArray.add(it)
            }
            is String -> {
                jsonArray.add(it as String)
            }
            is Number -> {
                jsonArray.add(it as Number)
            }
            else -> it.toJsonObject()?.also { obj ->
                jsonArray.add(obj)
            }
        }
    }
    if (jsonArray.isEmpty()) return null
    return jsonArray
}


/**
 * [String] to [JsonObject]/[JsonArray]
 */
fun String?.toObject(): JsonObject? {
    return parse(JsonObject::class.java)
}

fun String?.toArray(): JsonArray? {
    return parse(JsonArray::class.java)
}


/**
 * [JsonElement] to [JsonObject]/[JsonArray]
 */
fun JsonElement?.toObject(): JsonObject? {
    this ?: return null
    if (this.isJsonNull) return null
    if (!this.isJsonObject) return null
    return this.asJsonObject
}

fun JsonElement?.toArray(): JsonArray? {
    this ?: return null
    if (isJsonNull) return null
    if (!isJsonArray) return null
    val arr = asJsonArray
    if (arr.size() == 0) return null
    return arr
}


/**
 * [JsonArray]
 */
fun JsonArray?.getObject(index: Int): JsonObject? {
    this ?: return null
    if (index !in 0 until this.size()) return null
    if (this[index].isJsonNull) return null
    if (!this[index].isJsonObject) return null
    return this[index].asJsonObject
}

fun JsonArray?.getArray(index: Int): JsonArray? {
    this ?: return null
    if (index !in 0 until this.size()) return null
    if (this[index].isJsonNull) return null
    if (!this[index].isJsonArray) return null
    return this[index].asJsonArray
}

fun JsonArray?.getElement(index: Int): JsonElement? {
    this ?: return null
    if (index !in 0 until this.size()) return null
    if (this[index].isJsonNull) return null
    return this[index]
}

fun JsonArray?.addObject(obj: JsonObject?) {
    this ?: return
    obj ?: return
    if (obj.isJsonNull) return
    this.add(obj)
}

fun JsonArray?.addArray(array: JsonArray?) {
    this ?: return
    array ?: return
    if (array.size() == 0) return
    this.addAll(array)
}

fun JsonArray?.isEmpty(): Boolean {
    this ?: return true
    return this.size() == 0
}

fun JsonArray?.notEmpty(): Boolean {
    this ?: return false
    return this.size() != 0
}


/**
 * [JsonObject] properties setter
 */
fun JsonObject.put(key: String, value: String?): JsonObject {
    if (null != value) addProperty(key, value)
    return this
}

fun JsonObject.put(key: String, value: Boolean?): JsonObject {
    if (null != value) addProperty(key, value)
    return this
}

fun JsonObject.put(key: String, value: Number?): JsonObject {
    if (null != value) addProperty(key, value)
    return this
}

fun JsonObject.put(key: String, value: JsonElement?): JsonObject {
    if (null != value) add(key, value)
    return this
}

fun <T> JsonObject.put(key: String, value: List<T?>?): JsonObject {
    if (null != value) add(key, value.toJsonArray())
    return this
}


/**
 * [JsonObject] properties getter
 */
fun JsonObject?.obj(string: String): JsonObject? {
    this ?: return null
    if (!has(string)) return null
    if (!get(string).isJsonObject) return null
    return get(string).asJsonObject
}

fun JsonObject?.array(key: String): JsonArray? {
    this ?: return null
    if (!has(key)) return null
    if (get(key).isJsonNull) return null
    if (!get(key).isJsonArray) return null
    val arr = get(key).asJsonArray
    if (arr.size() == 0) return null
    return arr
}

fun JsonObject?.str(key: String, default: String? = null): String? {
    this ?: return default
    if (!has(key)) return default
    if (get(key).isJsonNull) return default
    val s = get(key)?.asString
    return s ?: default
}

fun JsonObject?.int(key: String, default: Int = -1): Int {
    try {
        this ?: return default
        if (!has(key)) return default
        if (get(key).isJsonNull) return default
        return get(key)?.asInt ?: get(key)?.asString?.toIntOrNull()!!
    } catch (e: Exception) {
        return this!!.get(key)?.asString?.toDoubleOrNull()?.toInt() ?: 0
    }
}

fun JsonObject?.long(key: String, default: Long = -1): Long {
    try {
        this ?: return default
        if (!has(key)) return default
        if (get(key).isJsonNull) return default
        return get(key)?.asLong ?: get(key)?.asString?.toLongOrNull()!!
    } catch (e: Exception) {
        return this!!.get(key)?.asString?.toDoubleOrNull()?.toLong() ?: 0
    }
}

fun JsonObject?.decimal(key: String, default: BigDecimal = BigDecimal.ZERO): BigDecimal {
    try {
        this ?: return default
        if (!has(key)) return default
        if (get(key).isJsonNull) return default
        return get(key)?.asBigDecimal ?: get(key)?.asString?.toBigDecimalOrNull()
        ?: 0.toBigDecimal()
    } catch (e: Exception) {
        return 0.toBigDecimal()
    }
}

fun JsonObject?.bool(key: String, default: Boolean = false): Boolean {
    this ?: return default
    if (!has(key)) return default
    if (get(key).isJsonNull) return default
    return get(key)?.asBoolean ?: default
}

fun JsonObject?.float(key: String, default: Float = 0f): Float {
    try {
        this ?: return default
        if (!has(key)) return default
        if (get(key).isJsonNull) return default
        return get(key)?.asFloat ?: get(key)?.asString?.toFloatOrNull() ?: 0f
    } catch (e: Exception) {
        return 0f
    }
}

fun JsonObject?.double(key: String, default: Double = 0.0): Double {
    try {
        this ?: return default
        if (!has(key)) return default
        if (get(key).isJsonNull) return default
        return get(key)?.asDouble ?: get(key)?.asString?.toDoubleOrNull() ?: 0.0
    } catch (e: Exception) {
        return 0.0
    }
}

fun JsonObject?.list(key: String): List<JsonObject>? {
    val s = array(key).toString()
    return try {
        return convertFactory.fromJson(StringReader(s), Array<JsonObject>::class.java).toList()
    } catch (ignore: Exception) {
        null
    }
}

fun JsonObject?.listString(key: String): List<String>? {
    val s = array(key).toString()
    return try {
        return convertFactory.fromJson(StringReader(s), Array<String>::class.java).toList()
    } catch (ignore: Exception) {
        null
    }
}


/**
 * Beauty json
 */
fun String?.stringyJson(): String {
    this ?: return "null"
    return try {
        JSONObject(this).toString(2)
    } catch (e: JSONException) {
        "null"
    }
}

fun JsonObject?.stringyJson(): String {
    this ?: return "null"
    return try {
        JSONObject(this.toString()).toString(2)
    } catch (e: JSONException) {
        "null"
    }
}