package com.pechuro.bsuirschedule.feature.optiondialog

import android.graphics.drawable.Drawable

data class OptionDialogButtonData(
        val text: String,
        val icon: Drawable? = null
)

data class OptionDialogCheckableButtonData(
        val text: String,
        val checked: Boolean
)