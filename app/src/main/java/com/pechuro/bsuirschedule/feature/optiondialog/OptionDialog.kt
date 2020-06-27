package com.pechuro.bsuirschedule.feature.optiondialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseDialogFragment
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.dialog_options.*

class OptionDialog : BaseDialogFragment() {

    companion object {
        const val TAG = "OptionDialog"
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

    override val layoutId: Int = R.layout.dialog_options

    override fun onAttach(context: Context) {
        // Can't restore dialog state, so dismiss there
        if (!::params.isInitialized) dismiss()
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        params.onDismissListener?.invoke()
    }

    private fun initViews() {
        optionsDialogTitle.text = params.title
        optionsDialogDoneButton.setSafeClickListener {
            dismiss()
        }
        val isInMultipleSelectionMode = params.multipleSelectionButtons.isNotEmpty()
        optionsDialogDoneButton.isVisible = isInMultipleSelectionMode
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
            view.isSelected = data.selected
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