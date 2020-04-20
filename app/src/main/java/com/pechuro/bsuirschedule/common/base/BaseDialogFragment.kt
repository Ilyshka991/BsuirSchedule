package com.pechuro.bsuirschedule.common.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import kotlin.math.roundToInt

abstract class BaseDialogFragment : DialogFragment() {

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected open val widthPercent: Float = 0.8F

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
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onStart() {
        super.onStart()
        setDialogWidth()
    }

    private fun setDialogWidth() {
        val width = (resources.displayMetrics.widthPixels * widthPercent).roundToInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}