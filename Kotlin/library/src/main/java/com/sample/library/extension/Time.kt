package com.sample.library.extension

import java.lang.reflect.InvocationTargetException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val GMT7: String = "GMT+7"

var HOUR_FORMAT: SimpleDateFormat = SimpleDateFormat("hh:mm aa")

var SHORT_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")

var LONG_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm aa")

var TIME_ZONE: TimeZone = TimeZone.getTimeZone(GMT7)

val SECOND: Long get() = 1000

val MIN: Long get() = 60 * SECOND

val HOUR: Long get() = 60 * MIN

val DAY: Long get() = 24 * HOUR

val MONTH: Long get() = 31 * DAY

val YEAR: Long get() = 365 * DAY

val nowInMillis: Long get() = System.currentTimeMillis()

val calendar: Calendar get() = Calendar.getInstance()

val nowInSecond: Long get() = System.currentTimeMillis() / SECOND

fun nowFormat(format: String): String {
    return nowInMillis.timeFormat(format)
}

fun nowFormat(sdf: SimpleDateFormat): String {
    return nowInMillis.timeFormat(sdf)
}

// if give up time in second convert to time in millis
fun Long.correctTime(): Long {
    return if (this < 1000000000000L) this * 1000 else this
}

fun Long.timeFormat(formatter: SimpleDateFormat): String {
    return try {
        formatter.format(Date(this.correctTime()))
    } catch (e: ParseException) {
        ""
    } catch (e: InvocationTargetException) {
        ""
    }
}

fun Long.timeFormat(formatter: String): String {
    return timeFormat(SimpleDateFormat(formatter))
}

/**
 * [String] time convert
 */
fun String?.timeFormat(formatter: String): Long? {
    return timeFormat(SimpleDateFormat(formatter))
}

fun String?.timeFormat(formatter1: SimpleDateFormat, formatter2: SimpleDateFormat): String? {
    this ?: return null
    return try {
        val date = this.dateFormat(formatter1)
        formatter2.format(date)
    } catch (e: ParseException) {
        null
    } catch (e: InvocationTargetException) {
        null
    }
}

fun String?.timeFormat(formatter: SimpleDateFormat): Long? {
    this ?: return null
    return try {
        formatter.parse(this)?.time ?: 0
    } catch (e: ParseException) {
        null
    } catch (e: InvocationTargetException) {
        null
    }
}

fun String?.dateFormat(formatter: SimpleDateFormat): Date {
    this ?: return Date()
    return try {
        formatter.parse(this) ?: Date()
    } catch (e: ParseException) {
        return Date()
    } catch (e: InvocationTargetException) {
        return Date()
    }
}

fun Date?.dateFormat(formatter: SimpleDateFormat): String? {
    this ?: return null
    return try {
        formatter.format(this)
    } catch (e: ParseException) {
        return null
    } catch (e: InvocationTargetException) {
        return null
    }
}

val Long.secondText: String
    get() {
        val seconds = this / 1000
        return "%02d:%02d".format(seconds / 60, seconds % 60)
    }

val Long.toSecond: Long
    get() {
        return this / 1000
    }

/**
 * [Calendar] time convert
 */
fun Calendar.timeFormat(formatter: SimpleDateFormat): String {
    return try {
        formatter.format(this.time)
    } catch (e: ParseException) {
        ""
    } catch (e: InvocationTargetException) {
        ""
    }
}

fun Calendar.timeFormat(formatter: String): String {
    return timeFormat(SimpleDateFormat(formatter))
}

fun Calendar.isCurrentDay(momentCal: Calendar = calendar): Boolean {
    if (this.get(Calendar.YEAR) != momentCal.get(Calendar.YEAR)) return false
    if (this.get(Calendar.MONTH) + 1 != momentCal.get(Calendar.MONTH) + 1) return false
    return this.get(Calendar.DAY_OF_MONTH) == momentCal.get(Calendar.DAY_OF_MONTH)
}

fun Calendar.isYesterday(momentCal: Calendar = calendar): Boolean {
    if (this.get(Calendar.YEAR) != momentCal.get(Calendar.YEAR)) return false
    if (this.get(Calendar.MONTH) + 1 != momentCal.get(Calendar.MONTH) + 1) return false
    return this.get(Calendar.DAY_OF_MONTH) - momentCal.get(Calendar.DAY_OF_MONTH) == -1
}

fun Calendar.isTomorrow(momentCal: Calendar = calendar): Boolean {
    if (this.get(Calendar.YEAR) != momentCal.get(Calendar.YEAR)) return false
    if (this.get(Calendar.MONTH) != momentCal.get(Calendar.MONTH)) return false
    return this.get(Calendar.DAY_OF_MONTH) - momentCal.get(Calendar.DAY_OF_MONTH) == 1
}

val Calendar.year: Int get() = this.get(Calendar.YEAR)

val Calendar.month: Int get() = this.get(Calendar.MONTH) + 1

val Calendar.day: Int get() = this.get(Calendar.DAY_OF_MONTH)

val Calendar.maxDayOfMonth: Int get() = this.getActualMaximum(Calendar.DAY_OF_MONTH)

/**
 *
 */
fun getDay2(day: Int): String {
    return when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        else -> "Sunday"
    }
}

fun getDay2D(day: Int): String {
    return when (day) {
        1 -> "Mo"
        2 -> "Tu"
        3 -> "We"
        4 -> "Th"
        5 -> "Fr"
        6 -> "Sa"
        else -> "Su"
    }
}

fun getMonth(month: Int): String {
    return when (month) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        else -> "December"
    }
}

fun getMonth3M(month: Int): String {
    return when (month) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        else -> "Dec"
    }
}

fun getDateTimeAgo(timeInMillis: Long): String {
    if (timeInMillis <= 0) {
        return "moment"
    }
    val diff = System.currentTimeMillis() - timeInMillis
    if (nowInMillis - timeInMillis < 3 * MIN) {
        return "moment"
    }
    if (diff < HOUR) {
        return "${diff / MIN} minutes ago"
    }

    val calendar = Calendar.getInstance()
    val last = Calendar.getInstance()
    last.timeInMillis = timeInMillis

    if (calendar.get(Calendar.YEAR) != last.get(Calendar.YEAR)) {
        return timeInMillis.timeFormat(LONG_FORMAT)
    }
    if (calendar.get(Calendar.MONTH) != last.get(Calendar.MONTH)) {
        return timeInMillis.timeFormat(LONG_FORMAT)
    }

    val dayDiff = calendar.get(Calendar.DAY_OF_MONTH) - last.get(Calendar.DAY_OF_MONTH)
    if (dayDiff < 1) {
        return timeInMillis.timeFormat(HOUR_FORMAT)
    }
    if (dayDiff < 2) {
        return "${timeInMillis.timeFormat(HOUR_FORMAT)} yesterday"
    }
    if (dayDiff < 8) {
        return "${timeInMillis.timeFormat(HOUR_FORMAT)} $dayDiff days ago"
    }
    return timeInMillis.timeFormat(LONG_FORMAT)
}

fun getHourOfDay(timeInMillis: Long): String {

    if (timeInMillis <= 0) {
        return "moment"
    }
    if (nowInMillis - timeInMillis < 4 * MIN) {
        return "moment"
    }
    var timeCal = Calendar.getInstance()
    timeCal.timeInMillis = timeInMillis

    if (timeCal.isYesterday()) {
        return "${timeInMillis.timeFormat(HOUR_FORMAT)} yesterday"
    }
    if (timeCal.isCurrentDay()) {
        return timeInMillis.timeFormat(HOUR_FORMAT)
    }
    return timeInMillis.timeFormat(LONG_FORMAT)
}

fun getRangeDay(timeInMillis: Long): Int {
    val momentDate = nowInMillis.timeFormat(SHORT_FORMAT)
    val momentMillis = momentDate.timeFormat(SHORT_FORMAT) ?: return -1
    val millisDiff = timeInMillis - momentMillis
    return TimeUnit.DAYS.convert(millisDiff, TimeUnit.MILLISECONDS).toInt()
}

fun getDuration(timeMillisAfter: Long, timeMillisBefore: Long): String {
    val diff = timeMillisAfter - timeMillisBefore
    if (diff <= MIN) return "..."
    val hour = diff / HOUR
    val min = diff % HOUR / MIN
    return "$hour:$min"
}


