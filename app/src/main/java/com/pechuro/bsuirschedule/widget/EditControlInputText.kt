package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.ext.showKeyboard
import com.pechuro.bsuirschedule.ext.textString
import kotlinx.android.synthetic.main.view_edit_control_text_input.view.*

class EditControlInputText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.textViewStyle
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var onNewTextSubmitted: (text: String) -> Unit = {}

    private var previousText = ""

    init {
        inflate(context, R.layout.view_edit_control_text_input, this)
        editControlTextInput.isEnabled = false
        editControlTextLayout.setEndIconOnClickListener {
            switchEditMode()
        }
        editControlTextDoneButton.setSafeClickListener {
            val newText = editControlTextInput.textString
            onNewTextSubmitted(newText)
            switchEditMode()
        }
        editControlTextCancelButton.setSafeClickListener {
            editControlTextInput.setText(previousText)
            switchEditMode()
        }
    }

    fun setText(text: String) {
        editControlTextInput.setText(text)
        editControlTextInput.setSelection(text.length)
    }

    private fun switchEditMode() {
        val isInEditMode = !editControlTextInput.isEnabled
        editControlTextLayout.isEndIconVisible = !isInEditMode
        editControlTextInput.isEnabled = isInEditMode
        if (isInEditMode) {
            previousText = editControlTextInput.textString
            editControlTextInput.requestFocus()
            editControlTextInput.setSelection(editControlTextInput.length())
            context.showKeyboard(editControlTextInput)
        }
        editControlTextActionButtonGroup.isVisible = isInEditMode
    }
}