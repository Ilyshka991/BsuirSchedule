package com.pechuro.bsuirschedule.ui.fragment.classes

import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.utils.addDays
import com.pechuro.bsuirschedule.utils.getCurrentWeek
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
}
