package com.pechuro.bsuirschedule.feature.datepicker

import com.pechuro.bsuirschedule.common.BaseEvent
import java.util.*

data class ScheduleDatePickedEvent(val date: Date) : BaseEvent()