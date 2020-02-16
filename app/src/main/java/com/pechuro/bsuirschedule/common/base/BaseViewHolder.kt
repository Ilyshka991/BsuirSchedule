package com.pechuro.bsuirschedule.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<T>(
        override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    var isSwipeAllowed: Boolean = false
        protected set

    abstract fun onBind(data: T)
}