package com.pechuro.bsuirschedule.matcher

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

abstract class RecyclerViewMatcher {

    companion object {

        fun withRecyclerView(view: RecyclerView): RecyclerViewMatcher = RecyclerViewImpl(view)

        fun withRecyclerView(@IdRes recyclerViewId: Int): RecyclerViewMatcher = RecyclerViewIdImpl(recyclerViewId)
    }

    protected abstract fun match(view: View, position: Int): Boolean

    fun atPosition(position: Int) = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("Find item at position")
        }

        override fun matchesSafely(view: View) = match(view, position)
    }

    private class RecyclerViewImpl(private val recyclerView: RecyclerView) : RecyclerViewMatcher() {
        override fun match(view: View, position: Int): Boolean {
            val childView = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    ?: throw IllegalStateException("No item at position: $position found")
            return view === childView
        }
    }

    private class RecyclerViewIdImpl(@IdRes private val recyclerViewId: Int) : RecyclerViewMatcher() {
        override fun match(view: View, position: Int): Boolean {
            val recyclerView = view.rootView.findViewById<View>(recyclerViewId) as? RecyclerView
                    ?: return false
            val childView = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    ?: throw IllegalStateException("No item at position: $position found")
            return view === childView
        }
    }
}