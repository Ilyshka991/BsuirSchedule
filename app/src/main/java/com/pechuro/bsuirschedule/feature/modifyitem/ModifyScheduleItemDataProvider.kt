package com.pechuro.bsuirschedule.feature.modifyitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.addIfEmpty
import com.pechuro.bsuirschedule.ext.getWeekDay
import com.pechuro.bsuirschedule.ext.requireValue
import java.text.SimpleDateFormat
import java.util.*

class ModifyScheduleItemDataProvider(
        lessonTypes: Array<String>,
        val schedule: Schedule,
        val scheduleItems: List<ScheduleItem>
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

    private val _startTimeData = MutableLiveData<String>()
    val startTimeData: LiveData<String>
        get() = _startTimeData

    private val _endTimeData = MutableLiveData<String>()
    val endTimeData: LiveData<String>
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

    private val _dateData = MutableLiveData<Date>()
    val dateData: LiveData<Date>
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

    private val timeFormatter = SimpleDateFormat(SCHEDULE_ITEM_TIME_FORMAT_PATTERN, Locale.getDefault())

    init {
        val scheduleItem = scheduleItems.firstOrNull()

        setSubject(scheduleItem?.subject ?: "")

        val lessonType = scheduleItem?.lessonType ?: lessonTypes.first()
        setLessonType(lessonType)

        val subgroupNumber = scheduleItem?.subgroupNumber ?: SubgroupNumber.ALL
        setSubgroupNumber(subgroupNumber)

        setNote(scheduleItem?.note ?: "")

        val defaultTime = timeFormatter.format(Date())
        _startTimeData.value = scheduleItem?.startTime ?: defaultTime
        _endTimeData.value = scheduleItem?.endTime ?: defaultTime

        var priority = LessonPriority.getDefaultForLessonType(lessonType)
        var weekDay = Calendar.getInstance().getWeekDay()
        var weekNumbers = listOf(WeekNumber.calculateCurrentWeekNumber())
        var date = Date()
        when (scheduleItem) {
            is Lesson -> {
                priority = scheduleItem.priority
                weekDay = scheduleItem.weekDay
                val resultWeekNumbers = scheduleItems.filterIsInstance<Lesson>().map { it.weekNumber }
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

    fun setStartTime(value: Date) {
        _startTimeData.value = timeFormatter.format(value)
    }

    fun setEndTime(value: Date) {
        _endTimeData.value = timeFormatter.format(value)
    }

    fun setDate(value: Date) {
        _dateData.value = value
    }

    fun getResultScheduleItem(): List<ScheduleItem> {
        val subject = _subjectData.requireValue
        val subgroupNumber = _subgroupNumberData.requireValue
        val lessonType = _lessonTypeData.requireValue
        val note = _noteData.requireValue
        val startTime = _startTimeData.requireValue
        val endTime = _endTimeData.requireValue
        val auditories = _auditoriesData.requireValue.toList()
        val employees = _employeesData.requireValue.toList()
        val studentGroups = _studentGroupsData.requireValue.toList()
        val isAddedByUser = scheduleItems.isEmpty()
        val weekNumbers = _weekNumberData.requireValue.addIfEmpty { WeekNumber.calculateCurrentWeekNumber() }
        return when (schedule) {
            is Schedule.GroupClasses -> weekNumbers.map { weekNumber ->
                Lesson.GroupLesson(
                        id = 0,
                        subject = subject,
                        subgroupNumber = subgroupNumber,
                        lessonType = lessonType,
                        note = note,
                        startTime = startTime,
                        endTime = endTime,
                        auditories = auditories,
                        isAddedByUser = isAddedByUser,
                        priority = _priorityData.requireValue,
                        weekDay = _weekDayData.requireValue,
                        weekNumber = weekNumber,
                        employees = employees
                )
            }
            is Schedule.EmployeeClasses -> weekNumbers.map { weekNumber ->
                Lesson.EmployeeLesson(
                        id = 0,
                        subject = subject,
                        subgroupNumber = subgroupNumber,
                        lessonType = lessonType,
                        note = note,
                        startTime = startTime,
                        endTime = endTime,
                        auditories = auditories,
                        isAddedByUser = isAddedByUser,
                        priority = _priorityData.requireValue,
                        weekDay = _weekDayData.requireValue,
                        weekNumber = weekNumber,
                        studentGroups = studentGroups
                )
            }
            is Schedule.GroupExams -> listOf(Exam.GroupExam(
                    id = 0,
                    subject = subject,
                    subgroupNumber = subgroupNumber,
                    lessonType = lessonType,
                    note = note,
                    startTime = startTime,
                    endTime = endTime,
                    auditories = auditories,
                    isAddedByUser = isAddedByUser,
                    date = _dateData.requireValue,
                    employees = employees
            )
            )
            is Schedule.EmployeeExams -> listOf(Exam.EmployeeExam(
                    id = 0,
                    subject = subject,
                    subgroupNumber = subgroupNumber,
                    lessonType = lessonType,
                    note = note,
                    startTime = startTime,
                    endTime = endTime,
                    auditories = auditories,
                    isAddedByUser = isAddedByUser,
                    date = _dateData.requireValue,
                    studentGroups = studentGroups
            ))
        }
    }
}