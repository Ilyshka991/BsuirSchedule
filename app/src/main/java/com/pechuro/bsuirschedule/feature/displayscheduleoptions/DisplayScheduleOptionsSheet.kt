package com.pechuro.bsuirschedule.feature.displayscheduleoptions

import android.os.Bundle
import android.view.View
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.sheet_display_schedule_options.*

class DisplayScheduleOptionsSheet : BaseBottomSheetDialog() {

    override val layoutId = R.layout.sheet_display_schedule_options

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(DisplayScheduleOptionsViewModel::class)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initView()
    }

    private fun initView() {
        displayOptionsSheetTypeButton.setSafeClickListener {
            viewModel.setNextDisplayType()
        }
        displayOptionsSheetSubgroupButton.setSafeClickListener {
            viewModel.setNextSubgroupNumber()
        }
    }

    private fun observeData() {
        viewModel.displayTypeData.nonNull().observe(viewLifecycleOwner) {
            val displayTypeIdRes = when (it) {
                ScheduleDisplayType.DAYS -> R.string.display_options_sheet_msg_type_days
                ScheduleDisplayType.WEEKS -> R.string.display_options_sheet_msg_type_weeks
            }
            val displayTypeString = getString(displayTypeIdRes)
            displayOptionsSheetTypeButton.text = getString(R.string.display_options_sheet_title_type, displayTypeString)
        }
        viewModel.subgroupNumberData.nonNull().observe(viewLifecycleOwner) {
            val text = when (it) {
                SubgroupNumber.ALL -> {
                    getString(R.string.display_options_sheet_title_all_group)
                }
                else -> {
                    getString(R.string.display_options_sheet_title_subgroup, it.value)
                }
            }
            displayOptionsSheetSubgroupButton.text = text
        }
    }

}