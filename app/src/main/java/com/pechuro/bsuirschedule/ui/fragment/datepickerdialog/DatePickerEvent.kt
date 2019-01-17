package com.pechuro.bsuirschedule.ui.fragment.datepickerdialog

import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class DatePickerEvent : BaseEvent() {
    class OnDateChoose(val dateTag: String) : DatePickerEvent()
}
