package com.pechuro.bsuirschedule.ext

import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.pechuro.bsuirschedule.App

inline val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

fun Context.showKeyboard(view: View) {
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(windowToken: IBinder?) {
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

inline val Context.app: App
    get() = applicationContext as App