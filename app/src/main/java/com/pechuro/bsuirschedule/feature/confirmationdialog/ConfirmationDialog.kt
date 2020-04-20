package com.pechuro.bsuirschedule.feature.confirmationdialog

import android.content.Context
import android.os.Bundle
import android.view.View
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_confirmation.*

class ConfirmationDialog : BaseDialogFragment() {

    companion object {
        const val TAG = "ConfirmationDialog"
    }

    class Builder {
        private var positionActionButton: ConfirmationDialogButtonData? = null
        private var negativeActionButton: ConfirmationDialogButtonData? = null
        private var title = ""

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setPositiveAction(data: ConfirmationDialogButtonData) {
            positionActionButton = data
        }

        fun setNegativeAction(data: ConfirmationDialogButtonData) {
            negativeActionButton = data
        }

        fun build() = ConfirmationDialog().apply {
            params = Params(
                    positionActionButton = positionActionButton,
                    negativeActionButton = negativeActionButton,
                    title = title
            )
        }
    }

    private data class Params(
            val positionActionButton: ConfirmationDialogButtonData?,
            val negativeActionButton: ConfirmationDialogButtonData?,
            val title: String
    )

    private lateinit var params: Params

    override val layoutId: Int = R.layout.dialog_confirmation

    override fun onAttach(context: Context) {
        // Can't restore dialog state, so dismiss there
        if (!::params.isInitialized) dismiss()
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        confirmationDialogTitle.text = params.title
    }
}