package com.pechuro.bsuirschedule.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Should be called on your view destroy
 */
fun RecyclerView.clearAdapter() {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) {
            // no-op
        }

        override fun onViewDetachedFromWindow(v: View?) {
            adapter = null
        }
    })
}
