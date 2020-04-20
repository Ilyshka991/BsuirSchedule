package com.pechuro.bsuirschedule.common

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes

data class FragmentAnimationsResHolder(
        @AnimRes
        @AnimatorRes
        val enter: Int = -1,
        @AnimRes
        @AnimatorRes
        val exit: Int = -1,
        @AnimRes
        @AnimatorRes
        val popEnter: Int = -1,
        @AnimRes
        @AnimatorRes
        val popExit: Int = -1
)