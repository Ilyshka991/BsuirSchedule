package com.pechuro.bsuirschedule.action

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.CoreMatchers.allOf

object RecyclerViewAction {

    fun scrollToPosition(position: Int) = object : ViewAction {

        override fun getConstraints() =
            allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())

        override fun getDescription() = "Scroll RecyclerView to position: $position"

        override fun perform(uiController: UiController, view: View?) {
            val recyclerView = view as RecyclerView
            recyclerView.scrollToPosition(position)
            uiController.loopMainThreadUntilIdle()
        }
    }
}