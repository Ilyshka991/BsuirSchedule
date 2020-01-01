package com.pechuro.bsuirschedule.feature.main.datepickerdialog

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class DatePickerEvent : BaseEvent() {
    class OnDateChoose(val dateTag: String) : DatePickerEvent()
}
