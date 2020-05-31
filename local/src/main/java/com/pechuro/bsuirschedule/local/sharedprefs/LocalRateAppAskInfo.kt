package com.pechuro.bsuirschedule.local.sharedprefs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocalRateAppAskInfo(
        val installDate: Long,
        val shouldAsk: Boolean,
        val askLaterDate: Long?
)