package com.huy.library.extension

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.StringReader

abstract class Converter<T> {
    abstract fun convert(): T?
}

fun <T> List<Converter<T>>.convert(): List<T>? {

    val list: MutableList<T> = mutableListOf()

    for (e: Converter<T> in this) {
        val entity = e.convert() ?: continue
        list.add(entity)
    }

    if (list.isEmpty()) return null

    return list
}

fun <R : Converter<E>, E> JsonObject.transform(cls: Class<R>): E? {
    return parse(cls)?.convert()
}

fun <R : Converter<E>, E> JsonArray.transform(cls: Class<Array<R>>): List<E>? {
    return parse(cls)?.convert()
}

fun <T> readJsonAsset(fileName: String, cls: Class<T>): T? {
    return readString(fileName).parse(cls)
}

fun <T> readJsonAsset(fileName: String, cls: Class<Array<T>>): List<T>? {
    return readString(fileName).parse(cls)
}


/**
 * Parse [JsonObject]/[JsonArray]/[String] to Kotlin Object/List<Object>
 */
private val gson = Gson()

fun <T> JsonObject?.parse(cls: Class<T>): T? {
    this ?: return null
    return this.toString().parse(cls)
}

fun <T> JsonArray?.parse(cls: Class<Array<T>>): List<T>? {
    this ?: return null
    return this.toString().parse(cls)
}

fun <T> String?.parse(cls: Class<T>): T? {
    if (isNullOrEmpty()) {
        return null
    }
    return try {
        return gson.fromJson(this, cls)
    } catch (ignore: Exception) {
        null
    }
}

fun <T> String?.parse(cls: Class<Array<T>>): List<T>? {
    if (isNullOrEmpty()) {
        return null
    }
    return try {
        return gson.fromJson(StringReader(this), cls).toList()
    } catch (ignore: Exception) {
        null
    }
}

fun <T> jsonObject(obj: T): JsonObject? {
    return try {
        val element = gson.toJsonTree(obj, object : TypeToken<T>() {}.type)
        return element.asJsonObject
    } catch (ignore: Exception) {
        null
    }
}

fun <T> Collection<T>?.jsonArray(): JsonArray? {
    if (this.isNullOrEmpty()) return null
    return try {
        val element = gson.toJsonTree(this, object : TypeToken<Collection<T>>() {}.type)
        return element.asJsonArray
    } catch (ignore: Exception) {
        null
    }
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
 * [JsonObject]
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

fun JsonObject?.string(key: String, default: String = ""): String {
    this ?: return default
    if (!has(key)) return default
    if (get(key).isJsonNull) return default
    return get(key)?.asString ?: default
}

fun JsonObject?.int(key: String, default: Int = 0): Int {
    this ?: return default
    if (!has(key)) return default
    if (get(key).isJsonNull) return default
    return get(key)?.asInt ?: default
}

fun JsonObject?.byte(key: String, default: Byte = 0): Byte {
    this ?: return default
    if (!has(key)) return default
    if (get(key).isJsonNull) return default
    return if (get(key)?.asBoolean == true) 1 else default
}

fun JsonObject?.bool(key: String, default: Boolean = false): Boolean {
    this ?: return default
    if (!has(key)) return default
    if (get(key).isJsonNull) return default
    return get(key)?.asBoolean ?: default
}









