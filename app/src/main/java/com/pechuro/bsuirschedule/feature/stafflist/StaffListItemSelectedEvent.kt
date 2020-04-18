package com.pechuro.bsuirschedule.feature.stafflist

import com.pechuro.bsuirschedule.common.BaseEvent

data class StaffListItemSelectedEvent(val info: StaffItemInformation) : BaseEvent()