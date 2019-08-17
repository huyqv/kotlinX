package com.huy.library.util.time

import com.huy.library.Library
import com.huy.library.R
import java.lang.reflect.InvocationTargetException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtil {

    fun hourFormat(timeMillis: Long): String {
        return convert(timeMillis, HOUR_FORMAT)
    }

    fun hourFormat(dateTimeText: String): Long {
        return convert(dateTimeText, HOUR_FORMAT)
    }

    fun shortFormat(timeMillis: Long): String {
        return convert(timeMillis, SHORT_FORMAT)
    }

    fun shortFormat(dateTimeText: String): Long {
        return convert(dateTimeText, SHORT_FORMAT)
    }

    fun longFormat(timeMillis: Long): String {
        return convert(timeMillis, LONG_FORMAT)
    }

    fun longFormat(dateTimeText: String): Long {
        return convert(dateTimeText, LONG_FORMAT)
    }

    fun convert(string: String, formatter: SimpleDateFormat): Long {
        return try {
            formatter.parse(string).time
        } catch (e: ParseException) {
            0
        } catch (e: InvocationTargetException) {
            0
        }
    }

    fun convert(long: Long, formatter: SimpleDateFormat): String {
        return try {
            formatter.format(Date(long.correctTime()))
        } catch (e: ParseException) {
            "..."
        } catch (e: InvocationTargetException) {
            "..."
        }
    }

    fun parse(dateTimeText: String): Long? {
        return try {
            if (dateTimeText.isNullOrEmpty()) null
            else LONG_FORMAT.parse(dateTimeText).time
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
        timeCal.timeInMillis = longTimeMillis.correctTime()
        return isYesterday(timeCal)
    }

    fun isTomorrow(longTimeMillis: Long): Boolean {
        val timeCal = Calendar.getInstance()
        timeCal.timeInMillis = longTimeMillis.correctTime()
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

    fun getMonth4M(month: Int): String {
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
        return Library.app.getString(stringRes).toString()
    }

    fun getMonth3M(month: Int): String {
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
        return Library.app.getString(stringRes).toString()
    }

    /**
     * Show different in timeInMillis
     * Today: convert "[HOUR_FORMAT]"
     * Yesterday: convert "[HOUR_FORMAT] : Yesterday"
     * If day range day least more 8 day: "[HOUR_FORMAT] [range] days"
     * More: "[SHORT_FORMAT]"
     */
    fun getDateTimeAgo(timeInMillis: Long): String {

        if (timeInMillis <= 0) return Library.app.getString(R.string.at_moment)

        val correctTime = timeInMillis.correctTime()

        val diff = System.currentTimeMillis() - correctTime

        if (System.currentTimeMillis() - correctTime < 3 * MINUTE)
            return Library.app.getString(R.string.at_moment)

        if (diff < HOUR)
            return "${diff / MINUTE} ${Library.app.getString(R.string.minutes_ago)}"

        val now = Calendar.getInstance()
        val last = Calendar.getInstance()
        last.timeInMillis = correctTime

        if (now.get(Calendar.YEAR) != last.get(Calendar.YEAR)) return longFormat(correctTime)

        if (now.get(Calendar.MONTH) != last.get(Calendar.MONTH)) return longFormat(correctTime)

        val dayDiff = now.get(Calendar.DAY_OF_MONTH) - last.get(Calendar.DAY_OF_MONTH)

        if (dayDiff < 1)
            return hourFormat(correctTime)

        if (dayDiff < 2)
            return "${hourFormat(correctTime)} ${Library.app.getString(R.string.yesterday)}"

        if (dayDiff < 8)
            return "${hourFormat(correctTime)} $dayDiff ${Library.app.getString(R.string.days_ago)}"

        return longFormat(correctTime)
    }

    /**
     * Show different in timeInMillis
     * Today: convert "[HOUR_FORMAT]"
     * Yesterday: convert "[HOUR_FORMAT] : Yesterday"
     * More: "[SHORT_FORMAT]"
     */
    fun getHourOfDay(timeInMillis: Long): String {

        if (timeInMillis <= 0) return Library.app.getString(R.string.at_moment)

        val correctTime = timeInMillis.correctTime()

        if (System.currentTimeMillis() - correctTime < 4 * MINUTE)
            return Library.app.getString(R.string.at_moment)

        var momentCal = Calendar.getInstance()
        var timeCal = Calendar.getInstance()
        timeCal.timeInMillis = correctTime

        if (isYesterday(timeCal, momentCal))
            return "${hourFormat(correctTime)} ${Library.app.getString(R.string.yesterday)}"

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
            -1 -> return Library.app.getString(R.string.yesterday)
            0 -> return Library.app.getString(R.string.today)
            1 -> return Library.app.getString(R.string.tomorrow)
        }
        when {
            rangeDay > 0 -> "remaining in $rangeDay ${Library.app.getString(R.string.days)}"
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

}