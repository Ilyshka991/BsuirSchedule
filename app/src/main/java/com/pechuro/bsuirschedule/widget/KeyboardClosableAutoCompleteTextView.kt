package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class KeyboardClosableAutoCompleteTextView @JvmOverloads constructor(
        context: Context,
        attr: AttributeSet? = null,
        defStyle: Int = R.attr.autoCompleteTextViewStyle
) : AppCompatAutoCompleteTextView(context, attr, defStyle) {

    var onKeyboardClose: () -> Unit = {}

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null && event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            onKeyboardClose()
            return true
        }
        return super.onKeyPreIme(keyCode, event)
    }
}
