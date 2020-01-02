package com.pechuro.bsuirschedule.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    override val containerView: View?
        get() = itemView

    abstract fun onBind(data: AbstractViewHolderData)
}

abstract class AbstractViewHolderData