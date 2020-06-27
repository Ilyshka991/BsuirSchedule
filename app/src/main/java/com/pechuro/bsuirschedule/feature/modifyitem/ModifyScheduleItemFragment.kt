package com.pechuro.bsuirschedule.feature.modifyitem

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.confirmationdialog.ConfirmationDialog
import com.pechuro.bsuirschedule.feature.confirmationdialog.ConfirmationDialogButtonData
import com.pechuro.bsuirschedule.feature.modifyitem.ModifyScheduleItemViewModel.State.Complete
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialog
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialogButtonData
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialogCheckableButtonData
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_modify_schedule_item.*

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(args.schedule, args.items, getAvailableLessonTypes())
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

    override fun handleBackPressed() = when {
        viewModel.state.requireValue == Complete -> false
        viewModel.dataProvider.hasChanges() -> {
            showExitDialog()
            true
        }
        else -> super.handleBackPressed()
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
            viewModel.dataProvider.setSubject(it)
        }
        modifyScheduleItemNoteText.addTextListener {
            viewModel.dataProvider.setNote(it)
        }
        modifyScheduleItemType.setSafeClickListener {
            selectLessonType()
        }
        modifyScheduleItemPriority.setSafeClickListener {
            selectPriority()
        }
        modifyScheduleItemSubgroupNumber.setSafeClickListener {
            selectSubgroupNumber()
        }
        modifyScheduleItemWeekday.setSafeClickListener {
            selectWeekDay()
        }
        modifyScheduleItemWeekNumbers.setSafeClickListener {
            selectWeekNumber()
        }
        modifyScheduleItemStartTime.setSafeClickListener {
            val current = viewModel.dataProvider.startTimeData.requireValue
            pickTime(current) {
                viewModel.dataProvider.setStartTime(it)
            }
        }
        modifyScheduleItemEndTime.setSafeClickListener {
            val current = viewModel.dataProvider.endTimeData.requireValue
            pickTime(current) {
                viewModel.dataProvider.setEndTime(it)
            }
        }
        modifyScheduleItemDate.setSafeClickListener {
            val current = viewModel.dataProvider.dateData.requireValue
            pickDate(current) {
                viewModel.dataProvider.setDate(it)
            }
        }
    }

    private fun observeData() {
        viewModel.state.nonNull().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModifyScheduleItemViewModel.State.Saving -> {
                    modifyScheduleItemDoneButton.isEnabled = false
                }
                is Complete -> {
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
                val formatted = getString(subgroupNumber.formattedStringRes).toLowerCase()
                modifyScheduleItemSubgroupNumber.setMessage(formatted)
            }
            startTimeData.nonNull().observe(viewLifecycleOwner) { startTime ->
                modifyScheduleItemStartTime.setMessage(startTime.formattedString)
            }
            endTimeData.nonNull().observe(viewLifecycleOwner) { endTime ->
                modifyScheduleItemEndTime.setMessage(endTime.formattedString)
            }
            priorityData.nonNull().observe(viewLifecycleOwner) { priority ->
                val formatted = getString(priority.formattedStringRes).toLowerCase()
                modifyScheduleItemPriority.setMessage(formatted)
            }
            weekDayData.nonNull().observe(viewLifecycleOwner) { weekDay ->
                modifyScheduleItemWeekday.setMessage(weekDay.getFormattedString(resources))
            }
            weekNumberData.nonNull().observe(viewLifecycleOwner) { weekNumbers ->
                val formattedWeekNumbers = weekNumbers.joinToString { it.formattedString }
                modifyScheduleItemWeekNumbers.setMessage(formattedWeekNumbers)
            }
            dateData.nonNull().observe(viewLifecycleOwner) { date ->
                modifyScheduleItemDate.setMessage(date.formattedString)
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

    private fun selectLessonType() {
        val availableTypes = getAvailableLessonTypes()
        val selectedType = viewModel.dataProvider.lessonTypeData.requireValue
        val options = availableTypes.map { type ->
            OptionDialogButtonData(
                    text = type,
                    selected = type == selectedType
            )
        }
        val listener = object : OptionDialog.OptionButtonClickListener {
            override fun onClick(position: Int) {
                viewModel.dataProvider.setLessonType(availableTypes[position])
            }
        }
        val title = getString(R.string.modify_schedule_item_title_select_type)
        OptionDialog.Builder()
                .setTitle(title)
                .setActions(options, listener)
                .build()
                .show(childFragmentManager, OptionDialog.TAG)
    }

    private fun selectPriority() {
        val availablePriorities = LessonPriority.values()
        val selectedPriority = viewModel.dataProvider.priorityData.requireValue
        val options = availablePriorities.map { prioriry ->
            val drawable = ShapeDrawable(OvalShape()).apply {
                paint.color = requireContext().color(prioriry.formattedColorRes)
            }
            OptionDialogButtonData(
                    text = getString(prioriry.formattedStringRes),
                    icon = drawable,
                    selected = prioriry == selectedPriority
            )
        }
        val listener = object : OptionDialog.OptionButtonClickListener {
            override fun onClick(position: Int) {
                viewModel.dataProvider.setPriority(availablePriorities[position])
            }
        }
        val title = getString(R.string.modify_schedule_item_title_select_priority)
        OptionDialog.Builder()
                .setTitle(title)
                .setActions(options, listener)
                .build()
                .show(childFragmentManager, OptionDialog.TAG)
    }

    private fun selectSubgroupNumber() {
        val availableNumbers = SubgroupNumber.values()
        val selectedSubgroupNumber = viewModel.dataProvider.subgroupNumberData.requireValue
        val options = availableNumbers.map { number ->
            OptionDialogButtonData(
                    text = getString(number.formattedStringRes),
                    selected = number == selectedSubgroupNumber
            )
        }
        val listener = object : OptionDialog.OptionButtonClickListener {
            override fun onClick(position: Int) {
                viewModel.dataProvider.setSubgroupNumber(availableNumbers[position])
            }
        }
        val title = getString(R.string.modify_schedule_item_title_select_subgroup)
        OptionDialog.Builder()
                .setTitle(title)
                .setActions(options, listener)
                .build()
                .show(childFragmentManager, OptionDialog.TAG)
    }

    private fun selectWeekDay() {
        val availableWeekDays = WeekDay.values()
        val selectedWeekDay = viewModel.dataProvider.weekDayData.requireValue
        val options = availableWeekDays.map { day ->
            OptionDialogButtonData(
                    text = day.getFormattedString(resources),
                    selected = day == selectedWeekDay
            )
        }
        val listener = object : OptionDialog.OptionButtonClickListener {
            override fun onClick(position: Int) {
                viewModel.dataProvider.setWeekDay(availableWeekDays[position])
            }
        }
        val title = getString(R.string.modify_schedule_item_title_select_weekday)
        OptionDialog.Builder()
                .setTitle(title)
                .setActions(options, listener)
                .build()
                .show(childFragmentManager, OptionDialog.TAG)
    }

    private fun selectWeekNumber() {
        val availableWeekNumbers = WeekNumber.values()
        val selectedWeeks = viewModel.dataProvider.weekNumberData.requireValue
        val options = availableWeekNumbers.map { number ->
            OptionDialogCheckableButtonData(
                    text = number.formattedString,
                    checked = number in selectedWeeks
            )
        }
        val listener = object : OptionDialog.OptionCheckableButtonClickListener {
            override fun onClick(position: Int, checked: Boolean) {
                val changedWeek = availableWeekNumbers[position]
                if (checked) {
                    viewModel.dataProvider.addWeekNumber(changedWeek)
                } else {
                    viewModel.dataProvider.removeWeekNumber(changedWeek)
                }
            }
        }
        val title = getString(R.string.modify_schedule_item_title_select_weeknumber)
        OptionDialog.Builder()
                .setTitle(title)
                .setCheckableActions(options, listener)
                .setOnDismissListener {
                    viewModel.dataProvider.checkWeekNumbers()
                }
                .build()
                .show(childFragmentManager, OptionDialog.TAG)

    }

    private fun showExitDialog() {
        ConfirmationDialog
                .Builder()
                .setTitle(getString(R.string.modify_schedule_item_title_discard_changes))
                .setPositiveAction(ConfirmationDialogButtonData(
                        text = getString(R.string.action_discard),
                        onClick = {
                            viewModel.close()
                        }
                ))
                .setNegativeAction(ConfirmationDialogButtonData(
                        text = getString(R.string.action_cancel)))
                .build()
                .show(childFragmentManager, ConfirmationDialog.TAG)
    }

    private fun pickTime(currentTime: LocalTime, onPicked: (LocalTime) -> Unit) {
        TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val time = LocalTime.of(hourOfDay, minute)
                    onPicked(time)
                },
                currentTime.hour,
                currentTime.minute,
                true
        ).show()
    }

    private fun pickDate(currentDate: LocalDate, onPicked: (LocalDate) -> Unit) {
        DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    val selectedDate = LocalDate(year, month, day)
                    onPicked(selectedDate)
                },
                currentDate.year,
                currentDate.month,
                currentDate.day
        ).show()
    }
}