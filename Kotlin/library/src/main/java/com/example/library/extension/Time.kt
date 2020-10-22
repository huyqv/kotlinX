package com.example.library.extension

import java.lang.reflect.InvocationTargetException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/07
 * @Description: ...
 * None Right Reserved
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

val lastDayOfCurrentMonth: Int get() = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

val lastDayOfNextMonth: Int
    get() {
        return calendar.also {
            it.add(Calendar.MONTH, 1)
            it.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        }.get(Calendar.DAY_OF_MONTH)
    }

val lastDayOfPreviousMonth: Int
    get() {
        return calendar.also {
            it.add(Calendar.MONTH, -1)
            it.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        }.get(Calendar.DAY_OF_MONTH)
    }

val currentTime: Date
    get() = Calendar.getInstance().time

val calendar: Calendar get() = Calendar.getInstance()

val now: Long get() = System.currentTimeMillis()

/**
 * [Long] time convert
 */
fun now(formatter: String): String {
    return now.timeFormat(formatter)
}

fun now(formatter: SimpleDateFormat): String {
    return now.timeFormat(formatter)
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

fun Long.isYesterday(): Boolean {
    var timeCal = Calendar.getInstance().also { it.timeInMillis = this }
    var nowCal = Calendar.getInstance()
    if (timeCal.get(Calendar.YEAR) != nowCal.get(Calendar.YEAR)) return false
    if (timeCal.get(Calendar.MONTH) != nowCal.get(Calendar.MONTH)) return false
    return timeCal.get(Calendar.DAY_OF_MONTH) < nowCal.get(Calendar.DAY_OF_MONTH)
}

fun Long.isTomorrow(): Boolean {
    var timeCal = Calendar.getInstance().also { it.timeInMillis = this }
    var nowCal = Calendar.getInstance()
    if (timeCal.get(Calendar.YEAR) != nowCal.get(Calendar.YEAR)) return false
    if (timeCal.get(Calendar.MONTH) != nowCal.get(Calendar.MONTH)) return false
    return timeCal.get(Calendar.DAY_OF_MONTH) > nowCal.get(Calendar.DAY_OF_MONTH)
}

fun Long.isCurrentDay(): Boolean {
    var timeCal = Calendar.getInstance().also { it.timeInMillis = this }
    var nowCal = Calendar.getInstance()
    if (timeCal.get(Calendar.YEAR) != nowCal.get(Calendar.YEAR)) return false
    if (timeCal.get(Calendar.MONTH) != nowCal.get(Calendar.MONTH)) return false
    return timeCal.get(Calendar.DAY_OF_MONTH) == nowCal.get(Calendar.DAY_OF_MONTH)
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

fun Long.getPassMinutes(): Long {
    return getPassTime() / MINUTE
}

fun Long.getPassHour(): Long {
    return getPassTime() / HOUR
}

fun Long.getPassDay(): Long {
    return getPassTime() / DAY
}

fun Long.correctTime(): Long {
    return if (this < 1000000000000L) this * 1000 else this
}


/**
 * [String] time convert
 */
fun String?.timeFormat(formatter: String): Long? {
    return timeFormat(SimpleDateFormat(formatter))
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
    val correctTime = timeInMillis.correctTime()
    val diff = System.currentTimeMillis() - correctTime
    if (now - correctTime < 3 * MINUTE) {
        return "moment"
    }
    if (diff < HOUR) {
        return "${diff / MINUTE} minutes ago"
    }

    val calendar = Calendar.getInstance()
    val last = Calendar.getInstance()
    last.timeInMillis = correctTime

    if (calendar.get(Calendar.YEAR) != last.get(Calendar.YEAR)) {
        return correctTime.timeFormat(LONG_FORMAT)
    }
    if (calendar.get(Calendar.MONTH) != last.get(Calendar.MONTH)) {
        return correctTime.timeFormat(LONG_FORMAT)
    }

    val dayDiff = calendar.get(Calendar.DAY_OF_MONTH) - last.get(Calendar.DAY_OF_MONTH)
    if (dayDiff < 1) {
        return correctTime.timeFormat(HOUR_FORMAT)
    }
    if (dayDiff < 2) {
        return "${correctTime.timeFormat(HOUR_FORMAT)} yesterday"
    }
    if (dayDiff < 8) {
        return "${correctTime.timeFormat(HOUR_FORMAT)} $dayDiff days ago"
    }
    return correctTime.timeFormat(LONG_FORMAT)
}

fun getHourOfDay(timeInMillis: Long): String {

    if (timeInMillis <= 0) {
        return "moment"
    }
    val correctTime = timeInMillis.correctTime()
    if (now - correctTime < 4 * MINUTE) {
        return "moment"
    }
    var momentCal = Calendar.getInstance()
    var timeCal = Calendar.getInstance()
    timeCal.timeInMillis = correctTime

    if (timeCal.isYesterday()) {
        return "${correctTime.timeFormat(HOUR_FORMAT)} yesterday"
    }
    if (timeCal.isCurrentDay(momentCal)) {
        return correctTime.timeFormat(HOUR_FORMAT)
    }
    return correctTime.timeFormat(LONG_FORMAT)
}

fun getRangeDay(timeInMillis: Long): Int {
    val momentDate = now.timeFormat(SHORT_FORMAT)
    val momentMillis = momentDate.timeFormat(SHORT_FORMAT) ?: return -1
    val millisDiff = timeInMillis - momentMillis
    return TimeUnit.DAYS.convert(millisDiff, TimeUnit.MILLISECONDS).toInt()
}

fun getDuration(timeMillisAfter: Long, timeMillisBefore: Long): String {
    val diff = timeMillisAfter - timeMillisBefore
    if (diff <= MINUTE) return "..."
    val hour = diff / HOUR
    val min = diff % HOUR / MINUTE
    return "$hour:$min"
}

