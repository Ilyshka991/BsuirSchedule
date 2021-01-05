package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import com.bsuir.pechuro.bsuirschedule.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.view_auto_validate_input_text.view.*
import com.google.android.material.R as materialR

class AutoValidateInputText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = materialR.attr.textInputStyle
) : TextInputLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_REGEX_PATTERN = ".*"
    }

    private val validationRegex: Regex
    private val errorText: String

    init {
        inflate(context, R.layout.view_auto_validate_input_text, this)
        background = null

        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.AutoValidateInputText)
        val validationPattern = styledAttrs.getString(R.styleable.AutoValidateInputText_validation_expr)
                ?: DEFAULT_REGEX_PATTERN
        validationRegex = validationPattern.toRegex()
        errorText = styledAttrs.getString(R.styleable.AutoValidateInputText_error_text) ?: ""
        styledAttrs.recycle()

        autoValidateEditText.addTextChangedListener {
            val currentText = it?.toString() ?: ""
            validate(currentText)
        }
    }

    fun getText() = autoValidateEditText.text?.toString() ?: ""

    private fun validate(text: String) {
        val isValid = validationRegex.matches(text)
        error = if (isValid) null else errorText
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState).apply {
            editTextState = autoValidateEditText.onSaveInstanceState()
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        autoValidateEditText.onRestoreInstanceState(state.editTextState)
    }

    private class SavedState : BaseSavedState {
        var editTextState: Parcelable? = null

        constructor(superState: Parcelable?) : super(superState)

        private constructor(source: Parcel?) : super(source) {
            editTextState = source?.readParcelable(TextInputEditText::class.java.classLoader)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeParcelable(editTextState, 0)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel) = SavedState(parcel)

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}