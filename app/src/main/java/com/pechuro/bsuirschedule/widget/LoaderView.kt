package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.ext.colorFromAttr
import kotlinx.android.synthetic.main.view_loader.view.*


class LoaderView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_loader, this)

        val foregroundColor = context.colorFromAttr(R.attr.textDefaultColor)
        val foregroundColorFilter = PorterDuffColorFilter(foregroundColor, PorterDuff.Mode.SRC_ATOP)
        loaderAnimationView.addValueCallback(
                KeyPath("foreground", "**"),
                LottieProperty.COLOR_FILTER
        ) { foregroundColorFilter }

        val backgroundColor = context.colorFromAttr(R.attr.rippleColor)
        val backgroundColorFilter = PorterDuffColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP)
        loaderAnimationView.addValueCallback(
                KeyPath("background", "**"),
                LottieProperty.COLOR_FILTER
        ) { backgroundColorFilter }

        context.obtainStyledAttributes(attrs, R.styleable.LoaderView).use {
            val message = it.getString(R.styleable.LoaderView_message) ?: ""
            loaderMessageText.text = message
            loaderMessageText.isVisible = message.isNotEmpty()
        }
    }
}