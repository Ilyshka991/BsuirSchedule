package com.pechuro.bsuirschedule

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented dialog_fragment_add, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedAddDialogFragment {
    @Test
    fun useAppContext() {
        // Context of the app under dialog_fragment_add.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.pechuro.bsuirschedule", appContext.packageName)
    }
}
