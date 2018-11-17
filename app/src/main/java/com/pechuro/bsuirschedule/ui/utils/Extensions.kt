package com.pechuro.bsuirschedule.ui.utils

import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation

fun Schedule.getInfo() = ScheduleInformation(id, name, type)