package com.pechuro.bsuirschedule.feature.optiondialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
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
        private var onDismissListener: (() -> Unit)? = null
        private var title = ""

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setOnDismissListener(listener: () -> Unit) = apply {
            onDismissListener = listener
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
                    title = title,
                    onDismissListener = onDismissListener
            )
        }
    }

    private data class Params(
            val singleSelectionButtons: List<OptionDialogButtonData>,
            val singleSelectionlistener: OptionButtonClickListener?,
            val multipleSelectionButtons: List<OptionDialogCheckableButtonData>,
            val multipleSelectionlistener: OptionCheckableButtonClickListener?,
            val title: String,
            val onDismissListener: (() -> Unit)?
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        params.onDismissListener?.invoke()
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
        addCheckableActionButtons()
    }

    private fun addActionButtons() {
        params.singleSelectionButtons.forEachIndexed { index, data ->
            val view = layoutInflater.inflate(
                    R.layout.item_option_dialog_button,
                    optionsDialogActionButtonContainer,
                    false
            ) as MaterialButton
            view.text = data.text
            view.icon = data.icon
            view.setSafeClickListener {
                params.singleSelectionlistener?.onClick(index)
                dismiss()
            }
            optionsDialogActionButtonContainer.addView(view)
        }
    }

    private fun addCheckableActionButtons() {
        params.multipleSelectionButtons.forEachIndexed { index, data ->
            val view = layoutInflater.inflate(
                    R.layout.item_option_dialog_checkable_button,
                    optionsDialogActionButtonContainer,
                    false
            ) as MaterialCheckBox
            view.text = data.text
            view.isChecked = data.checked
            view.setSafeClickListener {
                val checkBox = it as MaterialCheckBox
                params.multipleSelectionlistener?.onClick(index, checkBox.isChecked)
            }
            optionsDialogActionButtonContainer.addView(view)
        }
    }
}