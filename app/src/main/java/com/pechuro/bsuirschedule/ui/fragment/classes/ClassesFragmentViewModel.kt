package com.pechuro.bsuirschedule.ui.fragment.classes

import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ClassesFragmentViewModel @Inject constructor() : BaseViewModel() {

    fun getTabDate(days: Int): Triple<String, Int, String> {
        val dateFormat = SimpleDateFormat("EEE, d MMM",
                Locale.getDefault())
        val dateFormatRu = SimpleDateFormat("EEEE",
                Locale("ru"))

        val calendar = Calendar.getInstance()

        calendar.addDays(days)
        val day = dateFormat.format(calendar.time)
        val week = calendar.getCurrentWeek()
        val dayRu = dateFormatRu.format(calendar.time)
        return Triple(day, week, dayRu)
    }

    fun getWeekday(days: Int): Pair<String, String> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.addDays(days)
        val dateFormat = SimpleDateFormat("EEEE",
                Locale.getDefault())
        val dateFormatRu = SimpleDateFormat("EEEE",
                Locale("ru"))
        return Pair(dateFormat.format(calendar.time), dateFormatRu.format(calendar.time))
    }

    private fun Calendar.getCurrentWeek(): Int {
        val currentDate = this
        var year = this.get(Calendar.YEAR)

        if (this.get(Calendar.MONTH) < 8) {
            year--
        }

        val firstDay = Calendar.getInstance()
        firstDay.set(year, Calendar.SEPTEMBER, 1, 0, 0, 0)

        val difference = (currentDate.timeInMillis - firstDay.timeInMillis) / 1000 / 60 / 60 / 24
        var day = firstDay.get(GregorianCalendar.DAY_OF_WEEK)

        day -= 2
        if (day == -1) {
            day = 6
        }
        return ((difference + day).toInt() / 7) % 4 + 1
    }

    private fun Calendar.addDays(days: Int): Long {
        this.add(Calendar.DATE, days)
        return this.time.time
    }
}
