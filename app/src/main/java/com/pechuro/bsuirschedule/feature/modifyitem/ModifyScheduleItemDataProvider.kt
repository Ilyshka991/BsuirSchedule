package com.pechuro.bsuirschedule.feature.modifyitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.addIfEmpty
import com.pechuro.bsuirschedule.ext.getWeekDay
import com.pechuro.bsuirschedule.ext.requireValue
import java.util.*

class ModifyScheduleItemDataProvider(
        val initialSchedule: Schedule,
        val initialItems: List<ScheduleItem>,
        private val lessonTypes: Array<String>
) {

    private val _subjectData = MutableLiveData<String>()
    val subjectData: LiveData<String>
        get() = _subjectData

    private val _lessonTypeData = MutableLiveData<String>()
    val lessonTypeData: LiveData<String>
        get() = _lessonTypeData

    private val _subgroupNumberData = MutableLiveData<SubgroupNumber>()
    val subgroupNumberData: LiveData<SubgroupNumber>
        get() = _subgroupNumberData

    private val _noteData = MutableLiveData<String>()
    val noteData: LiveData<String>
        get() = _noteData

    private val _startTimeData = MutableLiveData<LocalTime>()
    val startTimeData: LiveData<LocalTime>
        get() = _startTimeData

    private val _endTimeData = MutableLiveData<LocalTime>()
    val endTimeData: LiveData<LocalTime>
        get() = _endTimeData

    private val _priorityData = MutableLiveData<LessonPriority>()
    val priorityData: LiveData<LessonPriority>
        get() = _priorityData

    private val _weekDayData = MutableLiveData<WeekDay>()
    val weekDayData: LiveData<WeekDay>
        get() = _weekDayData

    private val _weekNumberData = MutableLiveData<SortedSet<WeekNumber>>()
    val weekNumberData: LiveData<SortedSet<WeekNumber>>
        get() = _weekNumberData

    private val _dateData = MutableLiveData<LocalDate>()
    val dateData: LiveData<LocalDate>
        get() = _dateData

    private val _auditoriesData = MutableLiveData<Set<Auditory>>()
    val auditoriesData: LiveData<Set<Auditory>>
        get() = _auditoriesData

    private val _employeesData = MutableLiveData<Set<Employee>>()
    val employeesData: LiveData<Set<Employee>>
        get() = _employeesData

    private val _studentGroupsData = MutableLiveData<Set<Group>>()
    val studentGroupsData: LiveData<Set<Group>>
        get() = _studentGroupsData

    init {
        initData()
    }

    fun hasChanges(): Boolean {
        val resultItems = getResultScheduleItems()
        val isItemsTheSame = resultItems.containsAll(initialItems) && initialItems.containsAll(resultItems)
        return !isItemsTheSame
    }

    fun setSubject(value: String) {
        _subjectData.value = value
    }

    fun setLessonType(value: String) {
        _lessonTypeData.value = value
    }

    fun setSubgroupNumber(value: SubgroupNumber) {
        _subgroupNumberData.value = value
    }

    fun setNote(value: String) {
        _noteData.value = value
    }

    fun setPriority(value: LessonPriority) {
        _priorityData.value = value
    }

    fun setWeekDay(value: WeekDay) {
        _weekDayData.value = value
    }

    fun addEmloyee(employee: Employee) {
        val current = _employeesData.value ?: emptySet()
        _employeesData.value = current + employee
    }

    fun removeEmployee(employee: Employee) {
        val current = _employeesData.value ?: emptySet()
        _employeesData.value = current - employee
    }

    fun addGroup(group: Group) {
        val current = _studentGroupsData.value ?: emptySet()
        _studentGroupsData.value = current + group
    }

    fun removeGroup(group: Group) {
        val current = _studentGroupsData.value ?: emptySet()
        _studentGroupsData.value = current - group
    }

    fun addAuditory(auditory: Auditory) {
        val current = _auditoriesData.value ?: emptySet()
        _auditoriesData.value = current + auditory
    }

    fun removeAuditory(auditory: Auditory) {
        val current = _auditoriesData.value ?: emptySet()
        _auditoriesData.value = current - auditory
    }

    fun addWeekNumber(weekNumber: WeekNumber) {
        val currentWeeks = _weekNumberData.value ?: sortedSetOf()
        _weekNumberData.value = (currentWeeks + weekNumber).toSortedSet()
    }

    fun removeWeekNumber(weekNumber: WeekNumber) {
        val currentWeeks = _weekNumberData.value ?: sortedSetOf()
        _weekNumberData.value = (currentWeeks - weekNumber).toSortedSet()
    }

    fun checkWeekNumbers() {
        val currentWeeks = _weekNumberData.value ?: sortedSetOf()
        if (currentWeeks.isEmpty()) {
            _weekNumberData.value = WeekNumber.values().toSortedSet()
        }
    }

    fun setStartTime(value: LocalTime) {
        _startTimeData.value = value
    }

    fun setEndTime(value: LocalTime) {
        _endTimeData.value = value
    }

    fun setDate(value: LocalDate) {
        _dateData.value = value
    }

    private fun initData() {
        val scheduleItem = initialItems.firstOrNull()

        setSubject(scheduleItem?.subject ?: "")

        val lessonType = scheduleItem?.lessonType ?: lessonTypes.first()
        setLessonType(lessonType)

        val subgroupNumber = scheduleItem?.subgroupNumber ?: SubgroupNumber.ALL
        setSubgroupNumber(subgroupNumber)

        setNote(scheduleItem?.note ?: "")

        val defaultTime = LocalTime.current()
        val startTime = scheduleItem?.startTime ?: defaultTime
        setStartTime(startTime)
        val endTime = scheduleItem?.endTime ?: defaultTime
        setEndTime(endTime)

        var priority = LessonPriority.getDefaultForLessonType(lessonType)
        var weekDay = Calendar.getInstance().getWeekDay()
        var weekNumbers = listOf(WeekNumber.calculateCurrentWeekNumber())
        var date = LocalDate.current()
        when (scheduleItem) {
            is Lesson -> {
                priority = scheduleItem.priority
                weekDay = scheduleItem.weekDay
                val resultWeekNumbers = initialItems.filterIsInstance<Lesson>().map { it.weekNumber }
                if (resultWeekNumbers.isNotEmpty()) {
                    weekNumbers = resultWeekNumbers
                }
            }
            is Exam -> {
                date = scheduleItem.date
            }
        }
        setPriority(priority)
        setWeekDay(weekDay)
        weekNumbers.toSet().forEach {
            addWeekNumber(it)
        }
        setDate(date)

        _auditoriesData.value = scheduleItem?.auditories?.toSet() ?: emptySet()

        val employees: List<Employee>
        val studentGroups: List<Group>
        when (scheduleItem) {
            is Lesson.GroupLesson -> {
                employees = scheduleItem.employees
                studentGroups = emptyList()
            }
            is Lesson.EmployeeLesson -> {
                employees = emptyList()
                studentGroups = scheduleItem.studentGroups
            }
            is Exam.GroupExam -> {
                employees = scheduleItem.employees
                studentGroups = emptyList()
            }
            is Exam.EmployeeExam -> {
                employees = emptyList()
                studentGroups = scheduleItem.studentGroups
            }
            else -> {
                employees = emptyList()
                studentGroups = emptyList()
            }
        }
        _employeesData.value = employees.toSet()
        _studentGroupsData.value = studentGroups.toSet()
    }

    fun getResultScheduleItems(): List<ScheduleItem> {
        val subject = subjectData.requireValue
        val subgroupNumber = subgroupNumberData.requireValue
        val lessonType = lessonTypeData.requireValue
        val note = noteData.requireValue
        val startTime = startTimeData.requireValue
        val endTime = endTimeData.requireValue
        val auditories = auditoriesData.requireValue.toList()
        val employees = employeesData.requireValue.toList()
        val studentGroups = studentGroupsData.requireValue.toList()
        val weekNumbers = weekNumberData.requireValue.addIfEmpty { WeekNumber.calculateCurrentWeekNumber() }
        val priority = priorityData.requireValue
        val weekDay = weekDayData.requireValue
        val date = _dateData.requireValue
        return when (initialSchedule) {
            is Schedule.GroupClasses -> weekNumbers.map { weekNumber ->
                val initialItem = initialItems.find { (it as? Lesson.GroupLesson)?.weekNumber == weekNumber }
                Lesson.GroupLesson(
                        id = initialItem?.id ?: 0,
                        subject = subject,
                        subgroupNumber = subgroupNumber,
                        lessonType = lessonType,
                        note = note,
                        startTime = startTime,
                        endTime = endTime,
                        auditories = auditories,
                        isAddedByUser = initialItem?.isAddedByUser ?: true,
                        priority = priority,
                        weekDay = weekDay,
                        weekNumber = weekNumber,
                        employees = employees
                )
            }
            is Schedule.EmployeeClasses -> weekNumbers.map { weekNumber ->
                val initialItem = initialItems.find { (it as? Lesson.GroupLesson)?.weekNumber == weekNumber }
                Lesson.EmployeeLesson(
                        id = initialItem?.id ?: 0,
                        subject = subject,
                        subgroupNumber = subgroupNumber,
                        lessonType = lessonType,
                        note = note,
                        startTime = startTime,
                        endTime = endTime,
                        auditories = auditories,
                        isAddedByUser = initialItem?.isAddedByUser ?: true,
                        priority = priority,
                        weekDay = weekDay,
                        weekNumber = weekNumber,
                        studentGroups = studentGroups
                )
            }
            is Schedule.GroupExams -> {
                val initialItem = initialItems.getOrNull(0)
                listOf(Exam.GroupExam(
                        id = initialItem?.id ?: 0,
                        subject = subject,
                        subgroupNumber = subgroupNumber,
                        lessonType = lessonType,
                        note = note,
                        startTime = startTime,
                        endTime = endTime,
                        auditories = auditories,
                        isAddedByUser = initialItem == null,
                        date = date,
                        employees = employees
                )
                )
            }
            is Schedule.EmployeeExams -> {
                val initialItem = initialItems.getOrNull(0)
                listOf(Exam.EmployeeExam(
                        id = initialItem?.id ?: 0,
                        subject = subject,
                        subgroupNumber = subgroupNumber,
                        lessonType = lessonType,
                        note = note,
                        startTime = startTime,
                        endTime = endTime,
                        auditories = auditories,
                        isAddedByUser = initialItem == null,
                        date = date,
                        studentGroups = studentGroups
                ))
            }
        }
    }
}