package com.pechuro.bsuirschedule.util

import android.app.Activity
import androidx.test.rule.ActivityTestRule
import com.pechuro.bsuirschedule.runner.EspressoTestApp

val <T : Activity> ActivityTestRule<T>.app: EspressoTestApp
    get() = activity.application as EspressoTestApp