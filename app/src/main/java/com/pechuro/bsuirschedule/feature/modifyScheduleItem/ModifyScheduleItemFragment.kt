package com.pechuro.bsuirschedule.feature.modifyScheduleItem

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import kotlinx.android.synthetic.main.fragment_modify_schedule_item.*

class ModifyScheduleItemFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_modify_schedule_item

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ModifyScheduleItemViewModel::class)
    }
    private val args: ModifyScheduleItemFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        modifyScheduleItemToolbar.apply {
            title = getToolbarTitle()
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

    }

    private fun getToolbarTitle(): String {
        val scheduleItem = args.item
        return if (scheduleItem == null) {
            val scheduleTypeResId = when (args.schedule) {
                is Schedule.GroupClasses, is Schedule.EmployeeClasses -> R.string.modify_schedule_item_msg_lesson
                is Schedule.GroupExams, is Schedule.EmployeeExams -> R.string.modify_schedule_item_msg_exam
            }
            val scheduleType = getString(scheduleTypeResId)
            getString(R.string.modify_schedule_item_title_add, scheduleType)
        } else {
            getString(R.string.modify_schedule_item_title_edit, scheduleItem.subject)
        }
    }
}

