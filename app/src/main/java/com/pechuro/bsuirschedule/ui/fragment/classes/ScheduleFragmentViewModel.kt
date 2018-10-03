package com.pechuro.bsuirschedule.ui.fragment.classes

import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.utils.addDays
import com.pechuro.bsuirschedule.utils.getCurrentWeek
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ScheduleFragmentViewModel @Inject constructor() : BaseViewModel() {

    fun getTabDate(days: Int): Pair<String, Int> {
        val dateFormat = SimpleDateFormat("EEE, d MMM",
                Locale.getDefault())
        val calendar = Calendar.getInstance()

        val time = calendar.addDays(days)
        val day = dateFormat.format(time)
        val week = calendar.getCurrentWeek()

        return Pair(day, week)
    }
}
