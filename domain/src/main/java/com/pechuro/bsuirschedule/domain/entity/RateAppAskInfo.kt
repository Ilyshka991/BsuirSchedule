package com.pechuro.bsuirschedule.domain.entity

data class RateAppAskInfo(
    val installDate: LocalDate = LocalDate.current(),
    val shouldAsk: Boolean = true,
    val askLaterDate: LocalDate? = null
) {

    companion object {
        const val REQUIRED_DAYS_LEFT = 5
        const val ASK_LATER_DAYS_COUNT = 2
    }
}