package com.huy.kotlin.extension

import android.content.Context
import com.huy.kotlin.app.App
import com.huy.library.R
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
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private const val GMT7: String = "GMT+7"

const val SECOND = 1000

const val MINUTE = 60 * SECOND

const val HOUR = 60 * MINUTE

const val DAY = 24 * HOUR

private var hourFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa")

private var shortFormat: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")

private var longFormat: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm aa")

private var mTimeZone: TimeZone = TimeZone.getTimeZone(GMT7)

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


private fun cal(): Calendar {
    return Calendar.getInstance()
}

fun hourFormat(timeMillis: Long): String {
    return convertToDateTime(timeMillis, hourFormat)
}

fun hourFormat(dateTimeText: String): Long {
    return convertToTimeInMillis(dateTimeText, hourFormat)
}

fun shortFormat(timeMillis: Long): String {
    return convertToDateTime(timeMillis, shortFormat)
}

fun shortFormat(dateTimeText: String): Long {
    return convertToTimeInMillis(dateTimeText, shortFormat)
}

fun longFormat(timeMillis: Long): String {
    return convertToDateTime(timeMillis, longFormat)
}

fun longFormat(dateTimeText: String): Long {
    return convertToTimeInMillis(dateTimeText, longFormat)
}

fun convertToTimeInMillis(string: String, formatter: SimpleDateFormat): Long {
    return try {
        formatter.parse(string).time
    } catch (e: ParseException) {
        0
    } catch (e: InvocationTargetException) {
        0
    }
}

fun convertToDateTime(long: Long, formatter: SimpleDateFormat): String {
    return try {
        formatter.format(Date(long))
    } catch (e: ParseException) {
        "..."
    } catch (e: InvocationTargetException) {
        "..."
    }
}

fun parse(dateTimeText: String): Long? {
    return try {
        if (dateTimeText.isNullOrEmpty()) null
        else longFormat.parse(dateTimeText).time
    } catch (ignore: ParseException) {
        null
    }
}

fun parse(dateTimeText: String?, format: String): Long? {
    if (dateTimeText.isNullOrEmpty()) return null
    return try {
        SimpleDateFormat(format).parse(dateTimeText).time
    } catch (ignore: ParseException) {
        null
    }
}

fun isYesterday(longTimeMillis: Long): Boolean {
    val timeCal = Calendar.getInstance()
    timeCal.timeInMillis = longTimeMillis
    return isYesterday(timeCal)
}

fun isTomorrow(longTimeMillis: Long): Boolean {
    val timeCal = Calendar.getInstance()
    timeCal.timeInMillis = longTimeMillis
    return isTomorrow(timeCal)
}

fun isCurrentDay(timeCal: Calendar): Boolean {
    val moment = Calendar.getInstance()
    moment.timeInMillis = System.currentTimeMillis()
    return isCurrentDay(timeCal, moment)
}

fun isYesterday(timeCal: Calendar): Boolean {
    val moment = Calendar.getInstance()
    moment.timeInMillis = System.currentTimeMillis()
    return isYesterday(timeCal, moment)
}

fun isTomorrow(timeCal: Calendar): Boolean {
    val moment = Calendar.getInstance()
    moment.timeInMillis = System.currentTimeMillis()
    return isTomorrow(timeCal, moment)
}

fun isCurrentDay(inputCal: Calendar, momentCal: Calendar): Boolean {
    if (inputCal.get(Calendar.YEAR) != momentCal.get(Calendar.YEAR)) return false
    if (inputCal.get(Calendar.MONTH) + 1 != momentCal.get(Calendar.MONTH) + 1) return false
    return inputCal.get(Calendar.DAY_OF_MONTH) == momentCal.get(Calendar.DAY_OF_MONTH)
}

fun isYesterday(inputCal: Calendar, momentCal: Calendar): Boolean {
    if (inputCal.get(Calendar.YEAR) != momentCal.get(Calendar.YEAR)) return false
    if (inputCal.get(Calendar.MONTH) + 1 != momentCal.get(Calendar.MONTH) + 1) return false
    return inputCal.get(Calendar.DAY_OF_MONTH) - momentCal.get(Calendar.DAY_OF_MONTH) == -1
}

fun isTomorrow(inputCal: Calendar, momentCal: Calendar = cal()): Boolean {

    if (inputCal.get(Calendar.YEAR) != momentCal.get(Calendar.YEAR)) return false
    if (inputCal.get(Calendar.MONTH) != momentCal.get(Calendar.MONTH)) return false
    return inputCal.get(Calendar.DAY_OF_MONTH) - momentCal.get(Calendar.DAY_OF_MONTH) == 1
}

fun getMonth4M(context: Context, month: Int): String {
    val stringRes = when (month) {
        1 -> R.string.jan_4M
        2 -> R.string.feb_4M
        3 -> R.string.mar_4M
        4 -> R.string.apr_4M
        5 -> R.string.may_4M
        6 -> R.string.jun_4M
        7 -> R.string.jul_4M
        8 -> R.string.aug_4M
        9 -> R.string.sep_4M
        10 -> R.string.oct_4M
        11 -> R.string.nov_4M
        else -> R.string.dec_4M
    }
    return context.getText(stringRes).toString()
}

fun getMonth3M(context: Context, month: Int): String {
    val stringRes = when (month) {
        1 -> R.string.jan_3M
        2 -> R.string.feb_3M
        3 -> R.string.mar_3M
        4 -> R.string.apr_3M
        5 -> R.string.may_3M
        6 -> R.string.jun_3M
        7 -> R.string.jul_3M
        8 -> R.string.aug_3M
        9 -> R.string.sep_3M
        10 -> R.string.oct_3M
        11 -> R.string.nov_3M
        else -> R.string.dec_3M
    }
    return context.getText(stringRes).toString()
}

/**
 * Show different in timeInMillis
 * Today: convert "[hourFormat]"
 * Yesterday: convert "[hourFormat] : Yesterday"
 * If day range day least more 8 day: "[hourFormat] [range] days"
 * More: "[shortFormat]"
 */
fun getDateTimeAgo(timeInMillis: Long): String {

    if (timeInMillis <= 0) return App.instance.getString(R.string.at_moment)

    val correctTime = timeInMillis.getCorrectTime()

    val diff = System.currentTimeMillis() - correctTime

    if (System.currentTimeMillis() - correctTime < 3 * MINUTE)
        return App.instance.getString(R.string.at_moment)

    if (diff < HOUR)
        return "${diff / MINUTE} ${App.instance.getString(R.string.minutes_ago)}"

    val now = Calendar.getInstance()
    val last = Calendar.getInstance()
    last.timeInMillis = correctTime

    if (now.get(Calendar.YEAR) != last.get(Calendar.YEAR)) return longFormat(correctTime)

    if (now.get(Calendar.MONTH) != last.get(Calendar.MONTH)) return longFormat(correctTime)

    val dayDiff = now.get(Calendar.DAY_OF_MONTH) - last.get(Calendar.DAY_OF_MONTH)

    if (dayDiff < 1)
        return hourFormat(correctTime)

    if (dayDiff < 2)
        return "${hourFormat(correctTime)} ${App.instance.getString(R.string.yesterday)}"

    if (dayDiff < 8)
        return "${hourFormat(correctTime)} $dayDiff ${App.instance.getString(R.string.days_ago)}"

    return longFormat(correctTime)
}

/**
 * Show different in timeInMillis
 * Today: convert "[hourFormat]"
 * Yesterday: convert "[hourFormat] : Yesterday"
 * More: "[shortFormat]"
 */
fun getHourOfDay(timeInMillis: Long): String {

    if (timeInMillis <= 0) return App.instance.getString(R.string.at_moment)

    val correctTime = timeInMillis.getCorrectTime()

    if (System.currentTimeMillis() - correctTime < 4 * MINUTE)
        return App.instance.getString(R.string.at_moment)

    var momentCal = Calendar.getInstance()
    var timeCal = Calendar.getInstance()
    timeCal.timeInMillis = correctTime

    if (isYesterday(timeCal, momentCal))
        return "${hourFormat(correctTime)} ${App.instance.getString(R.string.yesterday)}"

    if (isCurrentDay(timeCal, momentCal))
        return hourFormat(correctTime)

    return longFormat(correctTime)
}

fun getRangeDay(timeInMillis: Long): Int {
    val momentDate = shortFormat(System.currentTimeMillis())
    val momentMillis = shortFormat(momentDate)
    val millisDiff = timeInMillis - momentMillis
    return TimeUnit.DAYS.convert(millisDiff, TimeUnit.MILLISECONDS).toInt()
}

fun getRangeDayText(timeInMillis: Long): String {

    val rangeDay = getRangeDay(timeInMillis)
    when (rangeDay) {
        -1 -> return App.instance.getString(R.string.yesterday)
        0 -> return App.instance.getString(R.string.today)
        1 -> return App.instance.getString(R.string.tomorrow)
    }
    when {
        rangeDay > 0 -> "remaining in $rangeDay ${App.instance.getString(R.string.days)}"
        else -> "pass in ${shortFormat(timeInMillis)}"
    }
    return "..."
}

fun getRangeDayToMoment(dateTimeText: String): Int {

    val inputDateMillis = longFormat(dateTimeText)
    val currentDateString = longFormat(System.currentTimeMillis())
    val currentDateMillis = longFormat(currentDateString)
    val millisDiff = inputDateMillis - currentDateMillis
    val daysDiff = TimeUnit.DAYS.convert(millisDiff, TimeUnit.MILLISECONDS)
    return daysDiff.toInt()
}

fun getDuration(longTimeMillisAfter: Long, longTimeMillisBefore: Long): String {
    val diff = longTimeMillisAfter - longTimeMillisBefore
    if (diff <= MINUTE) return "..."
    val hour = diff / HOUR
    val min = diff % HOUR / MINUTE
    return "$hour:$min"
}

fun Long.getCorrectTime(): Long {
    return if (this < 1000000000000L) this * 1000 else this
}

fun Long.getPassTime(): Long {
    return System.currentTimeMillis() - this
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
        longFormat.format(Date(this))
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
        formatter.format(Date(this))
    } catch (e: ParseException) {
        ""
    } catch (e: InvocationTargetException) {
        ""
    }
}

fun Long.toShortDateTime(): String {
    return convertToDateTime(this, shortFormat)
}

fun Long.toLongDateTime(): String {
    return convertToDateTime(this, longFormat)
}

fun String.toShortDateTime(): Long {
    return convertToTimeInMillis(this, shortFormat)
}

fun String.toLongDateTime(): Long {
    return convertToTimeInMillis(this, longFormat)
}