package com.pechuro.bsuirschedule.action

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.viewpager.widget.ViewPager
import org.hamcrest.CoreMatchers.allOf

object ViewPagerAction {

    fun checkPosition(position: Int) = object : ViewAction {

        override fun getConstraints() = allOf(isAssignableFrom(ViewPager::class.java), isDisplayed())

        override fun getDescription() = "Check ViewPager position: $position"

        override fun perform(uiController: UiController, view: View?) {
            val viewPager = view as ViewPager
            viewPager.currentItem = position
            uiController.loopMainThreadUntilIdle()
        }
    }
}