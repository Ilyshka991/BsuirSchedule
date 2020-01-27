package com.pechuro.bsuirschedule.assertion

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo

object RecyclerViewAssertion {

    fun sizeAtLeast(atLeastSize: Int) = ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        check(view is RecyclerView) { "The asserted view is not RecyclerView" }
        val adapter =
            view.adapter ?: throw IllegalStateException("No adapter is assigned to RecyclerView")
        assertThat(
            "RecyclerView item count",
            adapter.itemCount,
            greaterThanOrEqualTo(atLeastSize)
        )
    }

    fun sizeEqual(expected: Int) = ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        check(view is RecyclerView) { "The asserted view is not RecyclerView" }
        val adapter = view.adapter ?: throw IllegalStateException("No adapter is assigned to RecyclerView")
        assertThat(
            "RecyclerView item count",
            adapter.itemCount,
            equalTo(expected)
        )
    }
}

