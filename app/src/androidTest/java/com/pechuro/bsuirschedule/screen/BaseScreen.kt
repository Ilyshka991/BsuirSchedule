package com.pechuro.bsuirschedule.screen

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.pechuro.bsuirschedule.assertion.ImageViewAssertion.isDrawableExistOrNot
import com.pechuro.bsuirschedule.assertion.RecyclerViewAssertion.sizeAtLeast
import com.pechuro.bsuirschedule.assertion.RecyclerViewAssertion.sizeEqual
import junit.framework.AssertionFailedError
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

abstract class BaseScreen {

    protected val context: Context = ApplicationProvider.getApplicationContext()

    fun clickOnView(@IdRes id: Int) {
        onView(withId(id))
                .perform(click())
    }

    fun clickOnView(viewMatcher: Matcher<View>) {
        onView(viewMatcher)
                .perform(click())
    }

    fun checkVisibility(@IdRes viewId: Int, expected: Boolean) {
        checkVisibility(withId(viewId), expected)
    }

    fun checkVisibility(viewMatcher: Matcher<View>, expected: Boolean) {
        onView(viewMatcher)
                .check(matches(if (expected) isDisplayed() else not(isDisplayed())))
    }

    fun checkImageViewDrawableExist(@IdRes id: Int, expected: Boolean) {
        onView(withId(id))
                .check(isDrawableExistOrNot(expected))
    }

    fun checkListNotEmpty(@IdRes id: Int) {
        checkListNotEmpty(withId(id))
    }

    fun checkListNotEmpty(viewMatcher: Matcher<View>) {
        onView(viewMatcher).check(sizeAtLeast(1))
    }

    fun checkListSizeAtLeast(@IdRes id: Int, size: Int) {
        onView(withId(id)).check(sizeAtLeast(size))
    }

    fun checkListSizeAtLeast(viewMatcher: Matcher<View>, size: Int) {
        onView(viewMatcher).check(sizeAtLeast(size))
    }

    fun checkListSize(@IdRes id: Int, expected: Int) {
        onView(withId(id)).check(sizeEqual(expected))
    }

    fun isVisible(@IdRes viewId: Int) = try {
        onView(withId(viewId)).check(matches(isDisplayed()))
        true
    } catch (e: AssertionFailedError) {
        false
    } catch (e: NoMatchingViewException) {
        false
    }
}