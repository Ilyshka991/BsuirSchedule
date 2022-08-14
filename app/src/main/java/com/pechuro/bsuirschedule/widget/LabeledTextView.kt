package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.content.res.use
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.ext.dimenPx
import kotlinx.android.synthetic.main.view_labeled_text.view.*

class LabeledTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = HORIZONTAL
        inflate(context, R.layout.view_labeled_text, this)
        context.obtainStyledAttributes(attrs, R.styleable.LabeledTextView).use {
            val label = it.getString(R.styleable.LabeledTextView_label_text) ?: ""
            setLabel(label)
            val message = it.getString(R.styleable.LabeledTextView_message_text) ?: ""
            setMessage(message)

            val defaultTextSize = context.dimenPx(R.dimen.labeled_text_default_size)
            val textSize =
                it.getDimensionPixelSize(R.styleable.LabeledTextView_text_size, defaultTextSize)
            labeledTextViewLabel.setTextSize(COMPLEX_UNIT_PX, textSize.toFloat())
            labeledTextViewMessage.setTextSize(COMPLEX_UNIT_PX, textSize.toFloat())
        }
    }

    fun setLabel(label: String) {
        labeledTextViewLabel.text = label
    }

    fun setLabel(@StringRes labelRes: Int) {
        labeledTextViewLabel.setText(labelRes)
    }

    fun setMessage(message: String) {
        labeledTextViewMessage.text = message
    }

    fun setMessage(@StringRes messageRes: Int) {
        labeledTextViewMessage.setText(messageRes)
    }
}