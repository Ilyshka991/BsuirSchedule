package com.pechuro.bsuirschedule.ui.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pechuro.bsuirschedule.R


class BottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(): BottomSheetFragment =
                BottomSheetFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
}
