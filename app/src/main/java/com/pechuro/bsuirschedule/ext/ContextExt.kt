package com.pechuro.bsuirschedule.ext

import android.app.AlarmManager
import android.content.Context
import android.os.IBinder
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.pechuro.bsuirschedule.App

inline val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

inline val Context.alarmManager: AlarmManager
    get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

fun Context.showKeyboard(view: View) {
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(windowToken: IBinder?) {
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

inline val Context.app: App
    get() = applicationContext as App

fun Context.dimen(@DimenRes idRes: Int) = resources.getDimension(idRes)

fun Context.dimenPx(@DimenRes idRes: Int) = resources.getDimensionPixelOffset(idRes)

fun Context.drawable(@DrawableRes idRes: Int) = ContextCompat.getDrawable(this, idRes)

fun Context.color(@ColorRes idRes: Int) = ContextCompat.getColor(this, idRes)

fun Context.colorFromAttr(@AttrRes idRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(idRes, typedValue, true)
    return typedValue.data
}