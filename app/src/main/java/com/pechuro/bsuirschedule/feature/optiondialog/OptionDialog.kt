package com.pechuro.bsuirschedule.feature.optiondialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.dialog_options.*
import kotlin.math.roundToInt

class OptionDialog : DialogFragment() {

    companion object {
        const val TAG = "OptionDialog"

        private const val WIDTH_PERCENT = 0.8
    }

    interface OptionButtonClickListener {

        fun onClick(position: Int)
    }

    interface OptionCheckableButtonClickListener {

        fun onClick(position: Int, checked: Boolean)
    }

    class Builder {
        private var singleSelectionButtons: List<OptionDialogButtonData> = emptyList()
        private var singleSelectionlistener: OptionButtonClickListener? = null
        private var multipleSelectionButtons: List<OptionDialogCheckableButtonData> = emptyList()
        private var multipleSelectionlistener: OptionCheckableButtonClickListener? = null
        private var title = ""

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setCheckableActions(
                items: List<OptionDialogCheckableButtonData>,
                listener: OptionCheckableButtonClickListener
        ) = apply {
            multipleSelectionButtons = items
            multipleSelectionlistener = listener
            singleSelectionButtons = emptyList()
            singleSelectionlistener = null
        }

        fun setActions(
                items: List<OptionDialogButtonData>,
                listener: OptionButtonClickListener
        ) = apply {
            multipleSelectionButtons = emptyList()
            multipleSelectionlistener = null
            singleSelectionButtons = items
            singleSelectionlistener = listener
        }


        fun build() = OptionDialog().apply {
            params = Params(
                    singleSelectionButtons = singleSelectionButtons,
                    singleSelectionlistener = singleSelectionlistener,
                    multipleSelectionButtons = multipleSelectionButtons,
                    multipleSelectionlistener = multipleSelectionlistener,
                    title = title
            )
        }
    }

    private data class Params(
            val singleSelectionButtons: List<OptionDialogButtonData>,
            val singleSelectionlistener: OptionButtonClickListener?,
            val multipleSelectionButtons: List<OptionDialogCheckableButtonData>,
            val multipleSelectionlistener: OptionCheckableButtonClickListener?,
            val title: String
    )

    private lateinit var params: Params

    override fun onAttach(context: Context) {
        // Can't restore dialog state, so dismiss there
        if (!::params.isInitialized) dismiss()
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_options, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onStart() {
        super.onStart()
        setDialogWidth()
    }

    private fun setDialogWidth() {
        val width = (resources.displayMetrics.widthPixels * WIDTH_PERCENT).roundToInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun initViews() {
        optionsDialogTitle.text = params.title
        optionsDialogDoneButton.setSafeClickListener {
            dismiss()
        }
        optionsDialogDoneButton.isVisible = params.multipleSelectionButtons.isNotEmpty()
        addActionButtons()
    }

    private fun addActionButtons() {
        params.singleSelectionButtons.forEachIndexed { index, data ->
            val view = layoutInflater.inflate(
                    R.layout.item_option_dialog_button,
                    optionsDialogActionButtonContainer,
                    false
            ) as TextView
            view.text = data.text
            view.setSafeClickListener {
                params.singleSelectionlistener?.onClick(index)
                dismiss()
            }
            optionsDialogActionButtonContainer.addView(view)
        }
    }
}