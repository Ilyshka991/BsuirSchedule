package com.pechuro.bsuirschedule.utils

import android.view.View
import android.view.View.*
import androidx.databinding.BindingAdapter

object BindingUtils {

    @BindingAdapter("progressBar_visibility")
    @JvmStatic
    fun setProgressBarVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) VISIBLE else GONE
    }

    @BindingAdapter("view_visibility")
    @JvmStatic
    fun setVewVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) VISIBLE else INVISIBLE
    }
}