package com.pechuro.bsuirschedule.feature.flow

import androidx.annotation.DrawableRes
import com.pechuro.bsuirschedule.R

enum class FabActionState(@DrawableRes val iconRes: Int) {
    ADD_SCHEDULE(R.drawable.ic_add),
    DISPLAY_SCHEDULE_BACK(R.drawable.ic_fab_back);
}