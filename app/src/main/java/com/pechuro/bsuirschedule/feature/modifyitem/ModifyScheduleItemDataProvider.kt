package com.pechuro.bsuirschedule.feature.modifyitem

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.addIfEmpty
import com.pechuro.bsuirschedule.ext.getWeekDay
import com.pechuro.bsuirschedule.ext.requireValue
import java.text.SimpleDateFormat
import java.util.*

class ModifyScheduleItemDataProvider(
        private val context: Context,
        val schedule: Schedule,
        val scheduleItems: List<ScheduleItem>
) {

    val subjectData = MutableLiveData<String>()
    val lessonTypeData = MutableLiveData<String>()
    val subgroupNumberData = MutableLiveData<SubgroupNumber>()
    val noteData = MutableLiveData<String>()
    val startTimeData = MutableLiveData<String>()
    val endTimeData = MutableLiveData<String>()
    val priorityData = MutableLiveData<LessonPriority>()
    val weekDayData = MutableLiveData<WeekDay>()
    val weekNumberData = MutableLiveData<List<WeekNumber>>()
    val dateData = MutableLiveData<Date>()
    val auditoriesData = MutableLiveData<Set<Auditory>>()
    val employeesData = MutableLiveData<Set<Employee>>()
    val studentGroupsData = MutableLiveData<Set<Group>>()

    private val timeFormatter = SimpleDateFormat(SCHEDULE_ITEM_TIME_FORMAT_PATTERN, Locale.getDefault())

    init {
        val scheduleItem = scheduleItems.firstOrNull()

        subjectData.value = scheduleItem?.subject ?: ""

        val lessonType = scheduleItem?.lessonType ?: getDefaultLessonType(schedule)
        lessonTypeData.value = lessonType

        val subgroupNumber = scheduleItem?.subgroupNumber ?: SubgroupNumber.ALL
        subgroupNumberData.value = subgroupNumber

        noteData.value = scheduleItem?.note ?: ""

        val defaultTime = timeFormatter.format(Date())
        startTimeData.value = scheduleItem?.startTime ?: defaultTime
        endTimeData.value = scheduleItem?.endTime ?: defaultTime

        var priority = LessonPriority.getDefaultForLessonType(lessonType)
        var weekDay = Calendar.getInstance().getWeekDay()
        var weekNumber = listOf(WeekNumber.calculateCurrentWeekNumber())
        var date = Date()
        when (scheduleItem) {
            is Lesson -> {
                priority = scheduleItem.priority
                weekDay = scheduleItem.weekDay
                val resultWeekNumbers = scheduleItems.filterIsInstance<Lesson>().map { it.weekNumber }
                if (resultWeekNumbers.isNotEmpty()) {
                    weekNumber = resultWeekNumbers
                }
            }
            is Exam -> {
                date = scheduleItem.date
            }
        }
        priorityData.value = priority
        weekDayData.value = weekDay
        weekNumberData.value = weekNumber
        dateData.value = date

        auditoriesData.value = scheduleItem?.auditories?.toSet() ?: emptySet()

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
        employeesData.value = employees.toSet()
        studentGroupsData.value = studentGroups.toSet()
    }

    fun addEmloyee(employee: Employee) {
        val current = employeesData.value ?: emptySet()
        employeesData.value = current + employee
    }

    fun removeEmployee(employee: Employee) {
        val current = employeesData.value ?: emptySet()
        employeesData.value = current - employee
    }

    fun addGroup(group: Group) {
        val current = studentGroupsData.value ?: emptySet()
        studentGroupsData.value = current + group
    }

    fun removeGroup(group: Group) {
        val current = studentGroupsData.value ?: emptySet()
        studentGroupsData.value = current - group
    }

    fun addAuditory(auditory: Auditory) {
        val current = auditoriesData.value ?: emptySet()
        auditoriesData.value = current + auditory
    }

    fun removeAuditory(auditory: Auditory) {
        val current = auditoriesData.value ?: emptySet()
        auditoriesData.value = current - auditory
    }

    fun getResultScheduleItem(): List<ScheduleItem> {
        val subject = subjectData.requireValue
        val subgroupNumber = subgroupNumberData.requireValue
        val lessonType = lessonTypeData.requireValue
        val note = noteData.requireValue
        val startTime = startTimeData.requireValue
        val endTime = endTimeData.requireValue
        val auditories = auditoriesData.requireValue.toList()
        val employees = employeesData.requireValue.toList()
        val studentGroups = studentGroupsData.requireValue.toList()
        val isAddedByUser = scheduleItems.isEmpty()
        val weekNumbers = weekNumberData.requireValue.addIfEmpty { WeekNumber.calculateCurrentWeekNumber() }
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
                        priority = priorityData.requireValue,
                        weekDay = weekDayData.requireValue,
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
                        priority = priorityData.requireValue,
                        weekDay = weekDayData.requireValue,
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
                    date = dateData.requireValue,
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
                    date = dateData.requireValue,
                    studentGroups = studentGroups
            ))
        }
    }

    private fun getDefaultLessonType(schedule: Schedule): String {
        val isClassesSchedule = schedule is Schedule.GroupClasses || schedule is Schedule.EmployeeClasses
        val arrayResId = if (isClassesSchedule) R.array.lesson_types else R.array.exam_types
        return context.resources.getStringArray(arrayResId).first()
    }
}