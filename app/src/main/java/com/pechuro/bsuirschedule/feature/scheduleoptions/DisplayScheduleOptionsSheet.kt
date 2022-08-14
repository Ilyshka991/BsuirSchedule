package com.pechuro.bsuirschedule.feature.scheduleoptions

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.ext.args
import com.pechuro.bsuirschedule.ext.nonNull

import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.sheet_display_schedule_options.*

class DisplayScheduleOptionsSheet : BaseBottomSheetDialog() {

    companion object {

        const val TAG = "DisplayScheduleOptionsSheet"

        private const val BUNDLE_SCHEDULE = "BUNDLE_SCHEDULE"

        fun newInstance(schedule: Schedule) = DisplayScheduleOptionsSheet().apply {
            arguments = bundleOf(BUNDLE_SCHEDULE to schedule)
        }
    }

    override val layoutId = R.layout.sheet_display_schedule_options

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(DisplayScheduleOptionsViewModel::class)
    }

    private val schedule: Schedule by args(BUNDLE_SCHEDULE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initView()
    }

    private fun initView() {
        if (schedule is Schedule.EmployeeClasses) {
            displayOptionsSheetSubgroupButton.isVisible = false
        }
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
            displayOptionsSheetTypeButton.text =
                getString(R.string.display_options_sheet_title_type, displayTypeString)
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