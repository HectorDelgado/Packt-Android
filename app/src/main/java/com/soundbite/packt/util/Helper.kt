package com.soundbite.packt.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class Helper {
    companion object {
        /**
         * Converts a common year, month, day into a unix timestamp.
         */
        fun dateToTimestamp(year: Int, month: Int, day: Int): Long {
            val date = "$year-${"%02d".format(month)}-${"%02d".format(day)}"
            val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
            val localDate = LocalDate.parse(date, dateTimeFormatter)

            return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        }

        fun timestampToDate(timestamp: Long) {
            val date = Date(timestamp * 1000)
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = sdf.format(date)
        }

        fun getMonthFromTimestamp(timestamp: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val year = calendar.get(Calendar.YEAR)

            return 0
        }
    }
}
