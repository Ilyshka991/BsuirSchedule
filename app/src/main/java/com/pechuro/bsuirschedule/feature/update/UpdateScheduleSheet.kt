package com.pechuro.bsuirschedule.feature.update

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.update.UpdateScheduleSheetViewModel.State
import kotlinx.android.synthetic.main.sheet_update_schedule.*

class UpdateScheduleSheet : BaseBottomSheetDialog() {

    companion object {

        const val TAG = "UpdateScheduleSheet"

        private const val BUNDLE_ARGS = "BUNDLE_ARGS"

        fun newInstance(args: UpdateScheduleSheetArgs) = UpdateScheduleSheet().apply {
            arguments = bundleOf(BUNDLE_ARGS to args)
        }
    }

    override val layoutId = R.layout.sheet_update_schedule

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(UpdateScheduleSheetViewModel::class)
    }

    private val args: UpdateScheduleSheetArgs by args(BUNDLE_ARGS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSchedules(args.schedules)
        initView()
        observeData()
    }

    private fun initView() {
        val onUpdateClickAction: (View) -> Unit = {
            viewModel.updateNextSchedule()
            AppAnalytics.report(AppAnalyticsEvent.UpdateSchedule.Updated)
        }
        updateScheduleSheetUpdateButton.setSafeClickListener(onClick = onUpdateClickAction)
        updateScheduleErrorRetryButton.setSafeClickListener(onClick = onUpdateClickAction)

        val onCancelClickAction: (View) -> Unit = {
            val notRemind=updateScheduleSheetNotRemindCheckbox.isChecked
            viewModel.cancelUpdate(notRemind)
            AppAnalytics.report(AppAnalyticsEvent.UpdateSchedule.Dismissed(notRemind))
        }
        updateScheduleSheetCancelButton.setSafeClickListener(onClick = onCancelClickAction)
        updateScheduleErrorCancelButton.setSafeClickListener(onClick = onCancelClickAction)

        updateScheduleSheetNotRemindCheckbox.setOnCheckedChangeListener { _, isChecked ->
            updateScheduleSheetUpdateButton.isEnabled = !isChecked
        }
    }

    private fun observeData() {
        viewModel.currentScheduleData.nonNull().observe(viewLifecycleOwner) { schedule ->
            onScheduleUpdated(schedule)
        }
        viewModel.state.nonNull().observe(viewLifecycleOwner) { state ->
            setState(state)
        }
    }

    private fun onScheduleUpdated(schedule: Schedule) {
        val titleRes = when (schedule) {
            is Schedule.GroupClasses, is Schedule.EmployeeClasses -> R.string.update_schedule_classes_title
            is Schedule.GroupExams, is Schedule.EmployeeExams -> R.string.update_schedule_exams_title
        }
        updateScheduleSheetTitle.text = getString(titleRes, schedule.name)
        updateScheduleSheetNotRemindCheckbox.isChecked = false
    }

    private fun setState(state: State) {
        when (state) {
            is State.Idle -> {
                updateScheduleSheetInfoParentView.setVisibleOrInvisibleWithAlpha(true)
                updateScheduleSheetProgressParentView.setVisibleWithAlpha(false)
                updateScheduleSheetErrorParentView.setVisibleWithAlpha(false)
            }
            is State.Loading -> {
                updateScheduleSheetInfoParentView.setVisibleOrInvisibleWithAlpha(false)
                updateScheduleSheetProgressParentView.setVisibleWithAlpha(true)
                updateScheduleSheetErrorParentView.setVisibleWithAlpha(false)
            }
            is State.Error -> {
                updateScheduleSheetInfoParentView.setVisibleOrInvisibleWithAlpha(false)
                updateScheduleSheetProgressParentView.setVisibleWithAlpha(false)
                updateScheduleSheetErrorParentView.setVisibleWithAlpha(true)
            }
            is State.Complete -> dismiss()
        }
    }
}