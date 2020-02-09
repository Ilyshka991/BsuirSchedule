package com.pechuro.bsuirschedule

import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.pechuro.bsuirschedule.feature.MainActivity
import com.pechuro.bsuirschedule.runner.EspressoTestApp
import com.pechuro.bsuirschedule.util.app
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@LargeTest
abstract class AbstractScreenTest {

    companion object {
        private const val IDLING_RESOURCE_TIMEOUT_MS = 30 * 1000L
    }

    @get:Rule
    val activityRule by lazy(LazyThreadSafetyMode.NONE) {
        ActivityTestRule(MainActivity::class.java)
    }

    protected val app: EspressoTestApp
        get() = activityRule.app

    @Before
    fun registerIdlingResource() {
        IdlingPolicies.setIdlingResourceTimeout(IDLING_RESOURCE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        IdlingRegistry.getInstance().register(app.idlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(app.idlingResource)
    }
}