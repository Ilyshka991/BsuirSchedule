package com.pechuro.bsuirschedule.assertion

import android.widget.ImageView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue

object ImageViewAssertion {

    fun isDrawableExistOrNot(isExist: Boolean) = ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        check(view is ImageView) { "The asserted view is not ImageView" }
        assertThat(
            "ImageView drawable",
            view.drawable,
            if (isExist) notNullValue() else nullValue()
        )
    }
}