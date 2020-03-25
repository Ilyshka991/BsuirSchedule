package com.pechuro.bsuirschedule.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * Should be called on your view destroy.
 * @see <a href="https://stackoverflow.com/questions/35520946/leak-canary-recyclerview-leaking-madapter">StackOverflow</a>
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

/**
 * Should be called on your view destroy.
 * @see <a href="https://stackoverflow.com/questions/35520946/leak-canary-recyclerview-leaking-madapter">StackOverflow</a>
 */
fun ViewPager2.clearAdapter() {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) {
            // no-op
        }

        override fun onViewDetachedFromWindow(v: View?) {
            adapter = null
        }
    })
}