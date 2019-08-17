package com.huy.library.util.time

import java.lang.reflect.InvocationTargetException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/07
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
const val GMT7: String = "GMT+7"

const val SECOND = 1000

const val MINUTE = 60 * SECOND

const val HOUR = 60 * MINUTE

const val DAY = 24 * HOUR

var HOUR_FORMAT: SimpleDateFormat = SimpleDateFormat("hh:mm aa")

var SHORT_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")

var LONG_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm aa")

var TIME_ZONE: TimeZone = TimeZone.getTimeZone(GMT7)

val lastDayOfCurrentMonth: Int
    get() = cal().getActualMaximum(Calendar.DAY_OF_MONTH)

val lastDayOfNextMonth: Int
    get() {
        val cal = cal()
        cal.add(Calendar.MONTH, 1)
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        return cal.get(Calendar.DAY_OF_MONTH)
    }

val lastDayOfPreviousMonth: Int
    get() {
        val cal = cal()
        cal.add(Calendar.MONTH, -1)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        return cal.get(Calendar.DAY_OF_MONTH)
    }

val currentTime: Date
    get() = Calendar.getInstance().time

val currentTimeMillis: Long
    get() = System.currentTimeMillis()

fun cal(): Calendar {
    return Calendar.getInstance()
}

fun Long.getPassTime(): Long {
    return System.currentTimeMillis() - correctTime()
}

fun Long.isPassTime(): Boolean {
    return getPassTime() > 4 * MINUTE
}

fun Long.isMomentTime(): Boolean {
    return getPassTime() in (-4 * MINUTE)..(4 * MINUTE)
}

fun Long.isFutureTime(): Boolean {
    return -getPassTime() > 4 * MINUTE
}

fun Long.isAHourAgo(): Boolean {
    return getPassTime() in 0..HOUR
}

fun Long.isADayAgo(): Boolean {
    return getPassTime() in 0..DAY
}

fun Long.isCurrentDay(): Boolean {

    var timeCal = Calendar.getInstance()
    timeCal.timeInMillis = this

    var currentCal = Calendar.getInstance()

    if (timeCal.get(Calendar.YEAR) != currentCal.get(Calendar.YEAR))
        return false
    if (timeCal.get(Calendar.MONTH) != currentCal.get(Calendar.MONTH))
        return false

    return timeCal.get(Calendar.DAY_OF_MONTH) == currentCal.get(Calendar.DAY_OF_MONTH)
}

fun Long.getPassMinutes(): Long {
    return getPassTime() / MINUTE
}

fun Long.getPassHour(): Long {
    return getPassTime() / HOUR
}

fun Long.getPassDay(): Long {
    return getPassTime() / DAY
}

fun Long.formatTime(): String {
    return try {
        LONG_FORMAT.format(Date(correctTime()))
    } catch (e: ParseException) {
        "..."
    } catch (e: InvocationTargetException) {
        "..."
    }
}

fun Long.formatTime(regex: String, timeZone: String = "GMT+7"): String {

    val sdf = SimpleDateFormat(regex)
    sdf.timeZone = TimeZone.getTimeZone(timeZone)
    return sdf.format(Date(this))
}

fun Long.formatTime(formatter: SimpleDateFormat): String {
    return try {
        formatter.format(Date(correctTime()))
    } catch (e: ParseException) {
        ""
    } catch (e: InvocationTargetException) {
        ""
    }
}

fun Long.toShortDateTime(): String {
    return TimeUtil.convert(this, SHORT_FORMAT)
}

fun Long.toLongDateTime(): String {
    return TimeUtil.convert(this, LONG_FORMAT)
}

fun String.toShortDateTime(): Long {
    return TimeUtil.convert(this, SHORT_FORMAT)
}

fun String.toLongDateTime(): Long {
    return TimeUtil.convert(this, LONG_FORMAT)
}

fun Long.correctTime(): Long {
    return if (this < 1000000000000L) this * 1000 else this
}

