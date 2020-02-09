package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import com.pechuro.bsuirschedule.R
import kotlinx.android.synthetic.main.view_loader.view.*

class LoaderView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_loader, this)

        context.obtainStyledAttributes(attrs, R.styleable.LoaderView).use {
            val message = it.getString(R.styleable.LoaderView_message) ?: ""
            loaderMessageText.text = message
        }
    }
}