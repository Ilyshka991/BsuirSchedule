package com.pechuro.bsuirschedule.ui.utils

import android.view.View
import android.view.View.*
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun setVisibilityGone(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) VISIBLE else GONE
}

@BindingAdapter("visibility_invisible")
fun setVisibilityInvisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) VISIBLE else INVISIBLE
}