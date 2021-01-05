package com.pechuro.bsuirschedule.local.sharedprefs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocalHintDisplayState(
        val scheduleHintShown: Boolean,
        val examHintShown: Boolean
)