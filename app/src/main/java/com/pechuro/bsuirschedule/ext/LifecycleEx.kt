package com.pechuro.bsuirschedule.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

fun Fragment.whenStateAtLeast(state: Lifecycle.State, block: () -> Unit) {
    if (view != null && viewLifecycleOwner.lifecycle.currentState.isAtLeast(state)) {
        block()
    }
}