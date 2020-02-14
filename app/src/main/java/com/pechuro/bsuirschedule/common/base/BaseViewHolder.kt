package com.pechuro.bsuirschedule.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<T>(
        override val containerView: View,
        val isSwipeAllowed: Boolean = false
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract fun onBind(data: T)
}