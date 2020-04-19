package com.pechuro.bsuirschedule.feature.modifyitem

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.display.fragment.SCHEDULE_ITEM_DATE_FORMAT_PATTERN
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialog
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialogButtonData
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_modify_schedule_item.*
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class ModifyScheduleItemFragmentArgs(
        val schedule: Schedule,
        val items: List<ScheduleItem>
) : Parcelable

class ModifyScheduleItemFragment : BaseFragment() {

    interface ActionCallback {

        fun onModifyItemRequestAuditories()

        fun onModifyItemRequestEmployees()

        fun onModifyItemRequestGroups()
    }

    companion object {

        const val TAG = "ModifyScheduleItemFragment"

        private const val BUNDLE_ARGS = "BUNDLE_ARGS"

        fun newInstance(args: ModifyScheduleItemFragmentArgs) = ModifyScheduleItemFragment().apply {
            arguments = bundleOf(BUNDLE_ARGS to args)
        }
    }

    override val layoutId: Int = R.layout.fragment_modify_schedule_item

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ModifyScheduleItemViewModel::class)
    }

    private val args: ModifyScheduleItemFragmentArgs by args(BUNDLE_ARGS)

    private var actionCallback: ActionCallback? = null

    private val dateFormatter = SimpleDateFormat(SCHEDULE_ITEM_DATE_FORMAT_PATTERN, Locale.getDefault())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(args.schedule, args.items)
        initView()
        observeData()
    }

    override fun onStop() {
        super.onStop()
        context?.hideKeyboard(view?.findFocus()?.windowToken)
    }

    override fun onDetach() {
        super.onDetach()
        actionCallback = null
    }

    fun addEmployee(employee: Employee) {
        viewModel.dataProvider.addEmloyee(employee)
    }

    fun addGroup(group: Group) {
        viewModel.dataProvider.addGroup(group)
    }

    fun addAuditory(auditory: Auditory) {
        viewModel.dataProvider.addAuditory(auditory)
    }

    private fun initView() {
        modifyScheduleItemToolbar.title = getToolbarTitle()
        setParamsVisibility(args.schedule)
        setListeners()
    }

    private fun setParamsVisibility(schedule: Schedule) {
        val isClassesSchedule = schedule is Schedule.GroupClasses || schedule is Schedule.EmployeeClasses
        modifyScheduleItemPriority.isVisible = isClassesSchedule
        modifyScheduleItemWeekday.isVisible = isClassesSchedule
        modifyScheduleItemWeekNumbers.isVisible = isClassesSchedule
        modifyScheduleItemDate.isVisible = !isClassesSchedule

        val isGroupSchedule = schedule is Schedule.GroupClasses || schedule is Schedule.GroupExams
        modifyScheduleItemGroupsLabel.isVisible = !isGroupSchedule
        modifyScheduleItemGroupsChips.isVisible = !isGroupSchedule
        modifyScheduleItemEmployeesLabel.isVisible = isGroupSchedule
        modifyScheduleItemEmployeesChips.isVisible = isGroupSchedule
    }

    private fun setListeners() {
        modifyScheduleItemToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        modifyScheduleItemDoneButton.setSafeClickListener {
            viewModel.saveChanges()
        }
        modifyScheduleItemSubjectText.addTextListener {
            viewModel.dataProvider.subjectData.value = it
        }
        modifyScheduleItemNoteText.addTextListener {
            viewModel.dataProvider.noteData.value = it
        }
        modifyScheduleItemType.setSafeClickListener {
            openLessonTypeDialog()
        }
    }

    private fun observeData() {
        viewModel.state.nonNull().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModifyScheduleItemViewModel.State.Saving -> {
                    modifyScheduleItemDoneButton.isEnabled = false
                }
                is ModifyScheduleItemViewModel.State.Complete -> {
                    activity?.onBackPressed()
                }
            }
        }
        with(viewModel.dataProvider) {
            subjectData.nonNull().observe(viewLifecycleOwner) { subject ->
                if (modifyScheduleItemSubjectText.text?.toString() != subject) {
                    modifyScheduleItemSubjectText.setText(subject)
                }
            }
            noteData.nonNull().observe(viewLifecycleOwner) { note ->
                if (modifyScheduleItemNoteText.text?.toString() != note) {
                    modifyScheduleItemNoteText.setText(note)
                }
            }
            lessonTypeData.nonNull().observe(viewLifecycleOwner) { lessonType ->
                modifyScheduleItemType.setMessage(lessonType)
            }
            subgroupNumberData.nonNull().observe(viewLifecycleOwner) { subgroupNumber ->
                modifyScheduleItemSubgroupNumber.setMessage(subgroupNumber.formattedStringRes)
            }
            startTimeData.nonNull().observe(viewLifecycleOwner) { startTime ->
                modifyScheduleItemStartTime.setMessage(startTime)
            }
            endTimeData.nonNull().observe(viewLifecycleOwner) { endTime ->
                modifyScheduleItemEndTime.setMessage(endTime)
            }
            priorityData.nonNull().observe(viewLifecycleOwner) { priority ->
                modifyScheduleItemPriority.setMessage(priority.formattedStringRes)
            }
            weekDayData.nonNull().observe(viewLifecycleOwner) { weekDay ->
                modifyScheduleItemWeekday.setMessage(weekDay.getFormattedString(resources))
            }
            weekNumberData.nonNull().observe(viewLifecycleOwner) { weekNumbers ->
                val formattedWeekNumbers = weekNumbers.map { it.index + 1 }.sorted().joinToString { it.toString() }
                modifyScheduleItemWeekNumbers.setMessage(formattedWeekNumbers)
            }
            dateData.nonNull().observe(viewLifecycleOwner) { date ->
                modifyScheduleItemDate.setMessage(dateFormatter.format(date))
            }
            auditoriesData.nonNull().observe(viewLifecycleOwner) {
                fillAuditories(it)
            }
            employeesData.nonNull().observe(viewLifecycleOwner) {
                fillEmployees(it)
            }
            studentGroupsData.nonNull().observe(viewLifecycleOwner) {
                fillStudentGroups(it)
            }
        }
    }

    private fun getToolbarTitle(): String {
        val scheduleItem = args.items.firstOrNull()
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

    private fun fillEmployees(employees: Iterable<Employee>) {
        modifyScheduleItemEmployeesChips.removeAllViews()
        employees
                .map { employee ->
                    createChip(employee.abbreviation) { viewModel.dataProvider.removeEmployee(employee) }
                }
                .plus(createAddChip { actionCallback?.onModifyItemRequestEmployees() })
                .forEach {
                    modifyScheduleItemEmployeesChips.addView(it)
                }

    }

    private fun fillStudentGroups(groups: Iterable<Group>) {
        modifyScheduleItemGroupsChips.removeAllViews()
        groups
                .map { group ->
                    createChip(group.number) { viewModel.dataProvider.removeGroup(group) }
                }
                .plus(createAddChip { actionCallback?.onModifyItemRequestGroups() })
                .forEach {
                    modifyScheduleItemGroupsChips.addView(it)
                }

    }

    private fun fillAuditories(auditories: Iterable<Auditory>) {
        modifyScheduleItemAuditoriesChips.removeAllViews()
        auditories
                .map { auditory ->
                    val text = "${auditory.name}-${auditory.building.name}"
                    createChip(text) { viewModel.dataProvider.removeAuditory(auditory) }
                }
                .plus(createAddChip { actionCallback?.onModifyItemRequestAuditories() })
                .forEach {
                    modifyScheduleItemAuditoriesChips.addView(it)
                }

    }

    private inline fun createChip(text: CharSequence, crossinline onDelete: () -> Unit): Chip {
        val chip = layoutInflater.inflate(
                R.layout.item_modify_schedule_chip,
                modifyScheduleItemEmployeesChips,
                false
        ) as Chip
        chip.text = text
        chip.setOnCloseIconClickListener {
            onDelete()
        }
        return chip
    }

    private inline fun createAddChip(crossinline onClick: () -> Unit): Chip {
        val chip = layoutInflater.inflate(
                R.layout.item_modify_schedule_add_chip,
                modifyScheduleItemEmployeesChips,
                false
        ) as Chip
        chip.setSafeClickListener {
            onClick()
        }
        return chip
    }

    private fun getAvailableLessonTypes(): Array<String> {
        val schedule = args.schedule
        val isClassesSchedule = schedule is Schedule.GroupClasses || schedule is Schedule.EmployeeClasses
        val arrayResId = if (isClassesSchedule) R.array.lesson_types else R.array.exam_types
        return requireContext().resources.getStringArray(arrayResId)
    }

    private fun openLessonTypeDialog() {
        val availableTypes = getAvailableLessonTypes()
        val options = availableTypes.map { type ->
            OptionDialogButtonData(text = type)
        }
        val listener = object : OptionDialog.OptionButtonClickListener {
            override fun onClick(position: Int) {
                viewModel.dataProvider.lessonTypeData.value = availableTypes[position]
            }
        }
        val title = getString(R.string.modify_schedule_item_title_select_type)
        OptionDialog.Builder()
                .setTitle(title)
                .setActions(options, listener)
                .build()
                .show(childFragmentManager, OptionDialog.TAG)
    }
}